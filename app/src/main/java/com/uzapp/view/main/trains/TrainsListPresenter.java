package com.uzapp.view.main.trains;

import android.os.Bundle;
import android.util.Log;

import com.uzapp.R;
import com.uzapp.network.ApiErrorUtil;
import com.uzapp.network.ApiInterface;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.WagonType;
import com.uzapp.pojo.prices.Prices;
import com.uzapp.pojo.trains.TrainSearchResult;
import com.uzapp.util.Constants;
import com.uzapp.view.BaseView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by viktoria on 2/1/17.
 */

public class TrainsListPresenter {
    private SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_FORTMAT);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EE, d MMM");
    private SimpleDateFormat inputDateFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_FORTMAT);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM");
    private long stationFromCode, stationToCode, date;
    private int departureDate, arrivalDate;
    private TrainsListView view;
    private ApiInterface api;
    private List<com.uzapp.pojo.trains.Train> trains = new ArrayList<>();

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

    private void checkNoContent(List<com.uzapp.pojo.trains.Train> trains) {
        boolean needToShowNoContentView = trains == null || trains.size() == 0;
        String noContentText = view.getContext().getString(R.string.trains_no_places, simpleDateFormat.format(new Date(date)));
        view.showNoContent(needToShowNoContentView, needToShowNoContentView ? noContentText : "");
    }

    int getTrainsCount() {
        return trains.size();
    }

    void onInfoBtnClicked(Train train) {
        com.uzapp.pojo.trains.Train selectedTrain = trains.get(train.getPosition());
        view.showRouteFragment(selectedTrain, stationFromCode, stationToCode, date);
    }

    void onWagonItemClicked(Train train, WagonType wagonType) {
        com.uzapp.pojo.trains.Train selectedTrain = trains.get(train.getPosition());
        departureDate = (int) selectedTrain.getDepartureDate();
        arrivalDate = (int) selectedTrain.getArrivalDate();
        view.showProgress(true);
        Call<Prices> call = api.getPrices(stationFromCode, stationToCode, selectedTrain.getNumber(), (long) (date * Constants.MILI));
        call.enqueue(new PriceCallback(wagonType));
    }

    private List<Train> prepareTrainForAdapter(List<com.uzapp.pojo.trains.Train> trains) {
        List<Train> trainForAdapters = new ArrayList<>(trains.size());
        for (int i = 0; i < trains.size(); i++) {
            com.uzapp.pojo.trains.Train train = trains.get(i);
            Train trainForAdapter = new Train(this.trains.size() + i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(train.getDepartureDate() * 1000);
            Date departureDate = calendar.getTime();
            calendar.setTimeInMillis(train.getArrivalDate() * 1000);
            Date arrivalDate = calendar.getTime();
            trainForAdapter.setDepartureTime(timeFormat.format(departureDate));
            trainForAdapter.setDepartureDate(dateFormat.format(departureDate));
            trainForAdapter.setArrivalDate(dateFormat.format(arrivalDate));
            trainForAdapter.setArrivalTime(timeFormat.format(arrivalDate));

            Date travelTimeDate = parseDate(train.getTravelTime());
            if (travelTimeDate != null) {
                calendar.setTime(travelTimeDate);
                int hours = calendar.get(Calendar.HOUR);
                int min = calendar.get(Calendar.MINUTE);
                trainForAdapter.setTravelTime(view.getContext().getString(R.string.train_travel_time, hours, min));
            } else {
                trainForAdapter.setTravelTime("");
            }
            trainForAdapter.setTrainName(train.getNumber());
            trainForAdapter.setStationFrom(train.getStationFromName());
            trainForAdapter.setStationTo(train.getStationToName());
            trainForAdapter.setTrainPlaces(prepareTrainPlacesForAdapter(train.getPlaces()));
            trainForAdapters.add(trainForAdapter);
        }
        return trainForAdapters;
    }

    private List<TrainPlace> prepareTrainPlacesForAdapter(List<com.uzapp.pojo.trains.TrainPlace> trainPlaces) {
        List<TrainPlace> trainPlaceList = new ArrayList<>();
        for (com.uzapp.pojo.trains.TrainPlace trainPlace : trainPlaces) {
            if (trainPlace.getTotal() == 0) continue;
            String availablePlaceCount = String.valueOf(trainPlace.getTotal());
            String minPrice = view.getContext().getString(R.string.trains_place_min_price, Math.round(trainPlace.getCost()),
                    trainPlace.getCostCurrency());
            String wagonTypeStr = view.getContext().getString(trainPlace.getType().getLongNameStringRes());
            trainPlaceList.add(new TrainPlace(availablePlaceCount, minPrice, wagonTypeStr, trainPlace.getType()));
        }
        return trainPlaceList;
    }

    private Date parseDate(String date) {
        try {
            return inputDateFormat.parse(date);
        } catch (ParseException e) {
            Log.d(TrainsListAdapter.class.getName(), e.getMessage());
            return null;
        }
    }

    private Callback<TrainSearchResult> callback = new Callback<TrainSearchResult>() {
        @Override
        public void onResponse(Call<TrainSearchResult> call, Response<TrainSearchResult> response) {
            if (view == null) return;
            if (response.isSuccessful()) {
                List<com.uzapp.pojo.trains.Train> trains = response.body().getTrains();
                List<Train> trainForAdapterList = prepareTrainForAdapter(trains);
                trains.addAll(response.body().getTrains());
                view.showTrainsList(date, trainForAdapterList);
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

        void showRouteFragment(com.uzapp.pojo.trains.Train train, long stationFromCode, long stationToCode, long date);

        void showWagonPlaceFragment(Prices prices, int position, int departureDate, int arrivalDate, long selectDate, WagonType type);
    }
}
