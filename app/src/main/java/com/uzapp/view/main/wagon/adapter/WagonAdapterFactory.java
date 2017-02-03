package com.uzapp.view.main.wagon.adapter;

import android.content.Context;

import com.uzapp.util.Constants;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

/**
 * Created by viktoria on 2/3/17.
 */

public class WagonAdapterFactory {

    public static LyingWagonBaseAdapter getWagonAdapter(Wagon wagon, List<Integer> availablePlaces, Context context,
                                                       LyingWagonBaseAdapter.OnPlaceSelectionListener listener) {
        LyingWagonBaseAdapter adapter = null;
        if (wagon.getTypeCode().equalsIgnoreCase(Constants.TYPE_KUPE)) {
            adapter = new WagonKupeAdapter(wagon, availablePlaces, context, listener);
        } else if (wagon.getTypeCode().equalsIgnoreCase(Constants.TYPE_LUX)) {
            adapter = new WagonLuxAdapter(wagon, availablePlaces, context, listener);
        } else if (wagon.getTypeCode().equalsIgnoreCase(Constants.TYPE_ECONOMY)) {
            adapter = new WagonPlatskartAdapter(wagon, availablePlaces, context, listener);
        }
        return adapter;
    }
}
