package com.uzapp.util;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Set;

import retrofit2.Response;

/**
 * Created by vika on 21.08.16.
 */
public class ApiErrorUtil {

    public static String parseError(Response<?> response) {
        String errorMessage = "";
        try {
            String errorJson = response.errorBody().string();
            JsonObject jsonObject = (new JsonParser()).parse(errorJson).getAsJsonObject();
            if (jsonObject != null && jsonObject.has("status")) {
                //non-field error
                errorMessage = jsonObject.get("status").getAsString();
            } else if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                //field-related error
                Set<Map.Entry<String, JsonElement>> keyValueEntrys = jsonObject.entrySet();
                if (keyValueEntrys != null && keyValueEntrys.size() > 0) {
                    Map.Entry<String, JsonElement> firstKeyEntry = keyValueEntrys.iterator().next();
                    if (firstKeyEntry != null && firstKeyEntry.getValue().isJsonArray()) {
                        JsonArray jsonArray = firstKeyEntry.getValue().getAsJsonArray();
                        if (jsonArray != null) {
                            StringBuilder errorMesBuilder = new StringBuilder();
                            errorMesBuilder.append(firstKeyEntry.getKey()).append(": ");
                            for (int i = 0; i < jsonArray.size(); i++) {
                                if (errorMesBuilder.length() > 0) {
                                    errorMesBuilder.append(", ");
                                }
                                errorMesBuilder.append(jsonArray.get(i).getAsString());
                            }
                            errorMessage = errorMesBuilder.toString();
                        }
                    }
                }
            }
        } catch (IOException e) {
            Log.e(ApiErrorUtil.class.getName(), e.getMessage());
        }
        return errorMessage;
    }
}
