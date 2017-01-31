package com.uzapp.view.main.search.station;

import android.os.Bundle;

import com.uzapp.R;
import com.uzapp.network.ApiErrorUtil;
import com.uzapp.network.ApiInterface;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.route.PopularStation;
import com.uzapp.pojo.route.Station;
import com.uzapp.util.Constants;
import com.uzapp.view.BaseView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by viktoria on 1/31/17.
 */

class StationSearchPresenter {
    private Realm realm;
    private ApiInterface api;
    private Station selectedStation;
    private Call<List<Station>> searchStationCall;
    private StationSearchView view;

    void attachView(StationSearchView view, Bundle arguments) {
        this.view = view;
        realm = Realm.getDefaultInstance();
        api = ApiManager.getApi(view.getContext());
        showPopularStations();
        if (arguments != null && arguments.containsKey("title")) {
            view.setToolbarTitle(arguments.getString("title"));
        }
    }

    void detachView() {
        view = null;
        realm.close();
    }

    void onSearchLetterEntered(String msg) {
        if (selectedStation != null && !selectedStation.getName().equals(msg)) {
            selectedStation = null;
        }
        if (msg.length() >= Constants.SEARCH_MIN_LENGTH) {
            searchStations(msg);
        } else {
            view.clearStations();
            showPopularStations();
        }
    }

    void onStationItemClick(Station station) {
        selectedStation = station;
        view.setCityName(station.getName());
    }

    void onOkBtnClick() {
        if (selectedStation != null) {
            saveToPopularStations(selectedStation);
            view.hideKeyboard();
            view.returnResult(selectedStation);
        } else {
            //todo show message
        }
    }

    private void searchStations(String query) {
        view.setStationsHeaderText(view.getContext().getString(R.string.search_result));
        view.showProgress(true);
        searchStationCall = api.searchStations(query);
        searchStationCall.enqueue(searchCallback);
    }

    private void showPopularStations() {
        RealmResults<PopularStation> popularStations = realm.where(PopularStation.class).findAll().
                sort("accessTime", Sort.DESCENDING);
        if (popularStations.size() > 0) {
            List<Station> stationList = new ArrayList<Station>(popularStations.size());
            for (PopularStation popularStation : popularStations) {
                stationList.add(new Station(popularStation.getCode(), popularStation.getName(), popularStation.getRailway()));
            }
            view.showStations(stationList);
            view.setStationsHeaderText(view.getContext().getString(R.string.search_popular_destinations));
        } else {
            view.setStationsHeaderText("");
        }
    }

    private void saveToPopularStations(Station station) {
        //if first lookup of the station by user, save it, if not first - update access time,
        // if try to save more than limit - change the oldest station
        PopularStation popularStation = realm.where(PopularStation.class).equalTo("name", station.getName()).findFirst();
        long popularStationsCount = realm.where(PopularStation.class).count();
        if (popularStation == null && popularStationsCount < Constants.MAX_POPULAR_STATIONS_TO_SAVE) {
            realm.beginTransaction();
            PopularStation stationToSaveInDb = realm.createObject(PopularStation.class);
            stationToSaveInDb.setValues(station.getCode(), station.getName(), station.getRailway(),
                    Calendar.getInstance().getTime());
            realm.commitTransaction();
        } else if (popularStation != null) {
            realm.beginTransaction();
            popularStation.setAccessTime(Calendar.getInstance().getTime());
            realm.commitTransaction();
        } else {
            PopularStation oldestStation = realm.where(PopularStation.class).findAll().sort("accessTime", Sort.DESCENDING).last();
            realm.beginTransaction();
            oldestStation.setValues(station.getCode(), station.getName(), station.getRailway(),
                    Calendar.getInstance().getTime());
            realm.commitTransaction();
        }
    }

    private Callback<List<Station>> searchCallback = new Callback<List<Station>>() {
        @Override
        public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
            if (view == null) return;
            view.showProgress(false);
            if (response.isSuccessful()) {
                view.showStations(response.body());
            } else {
                String error = ApiErrorUtil.getErrorMessage(response, view.getContext());
                view.showError(error);
            }
        }


        @Override
        public void onFailure(Call<List<Station>> call, Throwable t) {
            if (view == null) return;
            view.showProgress(false);
            String error = ApiErrorUtil.getErrorMessage(t, view.getContext());
            view.showError(error);
        }
    };

    interface StationSearchView extends BaseView {
        void setToolbarTitle(String title);

        void showStations(List<Station> stationList);

        void clearStations();

        void setStationsHeaderText(String text);

        void setCityName(String text);

        void hideKeyboard();

        void returnResult(Station station);
    }
}
