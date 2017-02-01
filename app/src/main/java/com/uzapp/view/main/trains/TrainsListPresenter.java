package com.uzapp.view.main.trains;

import android.os.Bundle;

import com.uzapp.R;
import com.uzapp.network.ApiErrorUtil;
import com.uzapp.network.ApiInterface;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.WagonType;
import com.uzapp.pojo.prices.Prices;
import com.uzapp.pojo.trains.Train;
import com.uzapp.pojo.trains.TrainSearchResult;
import com.uzapp.util.Constants;
import com.uzapp.view.BaseView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by viktoria on 2/1/17.
 */

public class TrainsListPresenter {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM");
    private long stationFromCode, stationToCode, date;
    private int departureDate, arrivalDate;
    private TrainsListView view;
    private ApiInterface api;
    private List<Train> trains = new ArrayList<>();

    TrainsListPresenter(Bundle args) {
        stationFromCode = args.getLong("stationFromCode");
        stationToCode = args.getLong("stationToCode");
        date = args.getLong("date");
    }

    void attachView(TrainsListView view) {
        this.view = view;
        api = ApiManager.getApi(view.getContext());
        loadTrains();
    }

    void detachView() {
        this.view = null;
    }

    static Bundle getArgs(long stationFromCode, long stationToCode, long date) {
        Bundle args = new Bundle();
        args.putLong("stationFromCode", stationFromCode);
        args.putLong("stationToCode", stationToCode);
        args.putLong("date", date);
        return args;
    }
    private void loadTrains() {
        view.showProgress(true);
        Call<TrainSearchResult> trainSearchCall = api.searchTrains(stationFromCode, stationToCode, (long) (date * Constants.MILI));
        trainSearchCall.enqueue(callback);
    }

    private void checkNoContent(List<Train> trains) {
        boolean needToShowNoContentView = trains == null || trains.size() == 0;
        String noContentText = view.getContext().getString(R.string.trains_no_places, simpleDateFormat.format(new Date(date)));
        view.showNoContent(needToShowNoContentView, needToShowNoContentView ? noContentText : "");
    }

    int getTrainsCount() {
        return trains.size();
    }

    void onInfoBtnClicked(Train train) {
        view.showRouteFragment(train, stationFromCode, stationToCode, date);
    }

    void onWagonItemClicked(Train train, WagonType wagonType) {
        departureDate = (int) train.getDepartureDate();
        arrivalDate = (int) train.getArrivalDate();
        view.showProgress(true);
        Call<Prices> call = api.getPrices(stationFromCode, stationToCode, train.getNumber(), (long) (date * Constants.MILI));
        call.enqueue(new PriceCallback(wagonType));
    }

    private Callback<TrainSearchResult> callback = new Callback<TrainSearchResult>() {
        @Override
        public void onResponse(Call<TrainSearchResult> call, Response<TrainSearchResult> response) {
            if (view == null) return;
            if (response.isSuccessful()) {
                trains.addAll(response.body().getTrains());
                view.showTrainsList(date, trains);
                checkNoContent(trains);
            } else {
                String error = ApiErrorUtil.getErrorMessage(response, view.getContext());
                view.showError(error);
                checkNoContent(null);
            }
            view.showProgress(false);
        }

        @Override
        public void onFailure(Call<TrainSearchResult> call, Throwable t) {
            if (view == null) return;
            if (t != null) {
                String error = ApiErrorUtil.getErrorMessage(t, view.getContext());
                view.showError(error);
                view.showProgress(false);
            }
        }
    };

    private class PriceCallback implements Callback<Prices> {
        private WagonType wagonType;

        PriceCallback(WagonType wagonType) {
            this.wagonType = wagonType;
        }

        @Override
        public void onResponse(Call<Prices> call, Response<Prices> response) {
            if (view == null) return;
            view.showProgress(false);
            if (response.isSuccessful()) {
                Prices prices = response.body();
                //todo move call to wagon place fragment, refactor position and other params
                view.showWagonPlaceFragment(prices, 0, departureDate, arrivalDate, date, wagonType);
            } else {
                String error = ApiErrorUtil.getErrorMessage(response, view.getContext());
                view.showError(error);
            }
        }

        @Override
        public void onFailure(Call<Prices> call, Throwable t) {
            if (view == null) return;
            view.showProgress(false);
            if (t != null) {
                String error = ApiErrorUtil.getErrorMessage(t, view.getContext());
                view.showError(error);
            }
        }
    }

    public interface TrainsListView extends BaseView {
        void showTrainsList(long date, List<Train> trains);

        void showNoContent(boolean needToShowNoContentView, String noContentText);

        void showRouteFragment(Train train, long stationFromCode, long stationToCode, long date);

        void showWagonPlaceFragment(Prices prices, int position, int departureDate, int arrivalDate, long selectDate, WagonType type);
    }
}
