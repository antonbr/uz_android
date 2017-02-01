package com.uzapp.view.main.search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.uzapp.R;
import com.uzapp.network.ApiErrorUtil;
import com.uzapp.network.ApiInterface;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.route.RouteHistoryItem;
import com.uzapp.pojo.route.Station;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.BaseView;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by viktoria on 1/30/17.
 */
class SearchPresenter implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private SimpleDateFormat monthFormatter = new SimpleDateFormat("LLLL", Locale.getDefault());
    private SearchView view;
    private Station nearestStation, fromStation, toStation;
    private Date firstDate, secondDate;
    private List<Date> dates;
    private Map<Integer, String> monthPositionMap;
    private GoogleApiClient googleApiClient;
    private ApiInterface api;

    SearchPresenter(Bundle args) {
        if (args != null && args.containsKey("fromStation") && args.containsKey("toStation")) {
            fromStation = Parcels.unwrap(args.getParcelable("fromStation"));
            toStation = Parcels.unwrap(args.getParcelable("toStation"));
        }
    }

    static Bundle getArgs(RouteHistoryItem routeHistoryItem) {
        Bundle args = new Bundle();
        if (routeHistoryItem != null) {
            args.putParcelable("fromStation", Parcels.wrap(new Station(routeHistoryItem.getStationFromCode(), routeHistoryItem.getStationFromName(), null)));
            args.putParcelable("toStation", Parcels.wrap(new Station(routeHistoryItem.getStationToCode(), routeHistoryItem.getStationToName(), null)));
        }
        return args;
    }

    void attachView(SearchView baseView) {
        this.view = baseView;
        googleApiClient = new GoogleApiClient.Builder(view.getContext(), this, this).addApi(LocationServices.API).build();
        googleApiClient.connect();
        api = ApiManager.getApi(view.getContext());
        view.setPathFromText(fromStation == null ? null : fromStation.getName());
        view.setPathToText(toStation == null ? null: toStation.getName());
        initDates();
        checkAllFieldsFilled();
    }

    void detachView() {
        this.view = null;
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    void onDateViewReady() {
        if (firstDate != null && secondDate != null) {
            view.setSelectedDateLayoutVisible(firstDate, secondDate, monthFormatter.format(firstDate), monthFormatter.format(secondDate));
        } else if (firstDate != null && !firstDate.equals(dates.get(dates.size() - 1))) {
            view.setBackRouteToggleEnabled(true);
            view.setMonthName(monthFormatter.format(firstDate));
        }
    }

    void onLocationBtnClicked(boolean isLocationBtnChecked) {
        if (isLocationBtnChecked && nearestStation != null) {
            fromStation = nearestStation;
            view.setPathFromText(nearestStation.getName());
        } else if (!isLocationBtnChecked) {
            view.setPathFromText("");
        } else {
            loadNearestStation();
        }
    }

    private void loadNearestStation() {
        if (googleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                view.requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_ACCESS_FINE_LOCATION);
            } else {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (lastLocation != null) {
                    double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
                    Call<List<Station>> call = api.getNearestStations(lat, lon);
                    call.enqueue(nearestStationCallback);
                } else {
                    view.setLocationEnabled(false);
                    //todo location not found message
                }
            }
        }
    }

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadNearestStation();
                } else {
                    view.setLocationEnabled(false);
                }
                break;
        }
    }

    private void initDates() {
        dates = new ArrayList<Date>();
        monthPositionMap = new LinkedHashMap<>();
        Calendar calendar = CommonUtils.getCalendar();
        dates.add(calendar.getTime());
        String currentMonthName = monthFormatter.format(calendar.getTime());
        monthPositionMap.put(0, currentMonthName);

        int lastMonth = calendar.get(Calendar.MONTH);
        for (int i = 0; i < Constants.MAX_DAYS; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date date = calendar.getTime();
            dates.add(date);
            if (calendar.get(Calendar.MONTH) != lastMonth) {
                lastMonth = calendar.get(Calendar.MONTH);
                monthPositionMap.put(dates.indexOf(date), monthFormatter.format(calendar.getTime()));
            }
        }
        view.setMonthName(currentMonthName);
        view.initDatePickerList(dates);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    void onSelectStationFromResult(Intent data) {
        fromStation = Parcels.unwrap(data.getParcelableExtra("station"));
        view.setPathFromText(fromStation.getName());
        view.setLocationEnabled(false);
    }

    void onSelectStationToResult(Intent data) {
        toStation = Parcels.unwrap(data.getParcelableExtra("station"));
        view.setPathToText(toStation.getName());
    }

    void onSelectFirstDateResult(Intent data) {
        firstDate = (Date) data.getSerializableExtra("date");
        final int selectedPosition = dates.indexOf(firstDate);
        view.setSelectedFirstPosition(selectedPosition);
        view.scrollToSelectedPosition(selectedPosition);
        view.setBackRouteToggleEnabled(!firstDate.equals(dates.get(dates.size() - 1)));
        checkAllFieldsFilled();
    }

    void onSelectSecondDateResult(Intent data) {
        secondDate = (Date) data.getSerializableExtra("date");
        view.setSelectedDateLayoutVisible(firstDate, secondDate, monthFormatter.format(firstDate), monthFormatter.format(secondDate));
    }

    void checkAllFieldsFilled() {
        if (fromStation != null && toStation != null && firstDate != null &&
                (!view.isBackRouteToggleChecked() || secondDate != null)) {
            view.setFindBtnEnabled(true);
        } else {
            view.setFindBtnEnabled(false);
        }
    }

    void seeFullCalendarBtnClicked() {
        Calendar calendar = CommonUtils.getCalendar();
        if (view.isBackRouteToggleChecked()) {
            calendar.setTime(firstDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        view.showFullCalendarFragment(calendar.getTime());
    }

    void onResetBtnClicked() {
        fromStation = null;
        toStation = null;
        view.setPathFromText("");
        view.setPathToText("");
        view.setLocationEnabled(false);
        firstDate = null;
        secondDate = null;
        view.setSelectingSecondDate(false);
        view.setSelectedFirstPosition(-1);
        view.setSelectedSecondPosition(-1);
        view.setSelectedDateLayoutInvisible();
        view.setBackRouteToggleChecked(false);
        view.setBackRouteToggleEnabled(false);

    }

    void onResetDateBtnClicked() {
        secondDate = null;
        view.setSelectedDateLayoutInvisible();
    }

    void onDateItemClick(int position, Date date) {
        if (!view.isBackRouteToggleChecked()) {
            firstDate = date;
            view.setSelectedFirstPosition(position);
            view.setBackRouteToggleEnabled(!firstDate.equals(dates.get(dates.size() - 1)));
            checkAllFieldsFilled();
        } else if (view.isBackRouteToggleChecked() && date.after(firstDate)) {
            secondDate = date;
            view.setSelectedDateLayoutVisible(firstDate, secondDate, monthFormatter.format(firstDate), monthFormatter.format(secondDate));
        }
    }

    void onMonthScrollStateChanged(int firstVisiblePosition) {
        ListIterator<Integer> iterator = new ArrayList<>(monthPositionMap.keySet()).listIterator(monthPositionMap.size());
        while (iterator.hasPrevious()) {
            Integer key = iterator.previous();
            if (firstVisiblePosition >= key) {
                view.setMonthName(monthPositionMap.get(key));
                break;
            }
        }
    }

    void onFindTicketsBtnClicked() {
        if (fromStation.getCode() == toStation.getCode()) {
            view.showError(view.getContext().getString(R.string.search_stations_not_valid));
        } else {
            long secondDateTime = secondDate == null ? 0 : secondDate.getTime();
            view.showSelectTrainFragment(fromStation.getCode(), toStation.getCode(),
                    firstDate.getTime(), secondDateTime);
        }
    }

    private Callback<List<Station>> nearestStationCallback = new Callback<List<Station>>() {
        @Override
        public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
            if (view == null) return;
            if (response.isSuccessful() && response.body().size() > 0) {
                List<Station> result = response.body();
                nearestStation = result.get(0);
                //view.setLocationEnabled(true); todo
                view.setPathFromText(nearestStation.getName());
                fromStation = nearestStation;
            } else if (response.isSuccessful() && response.body().size() == 0) {
                //TODO show message no stations found
                view.setLocationEnabled(false);
            } else {
                String error = ApiErrorUtil.getErrorMessage(response, view.getContext());
                view.showError(error);
            }
        }

        @Override
        public void onFailure(Call<List<Station>> call, Throwable t) {
            if (view == null) return;
            String error = ApiErrorUtil.getErrorMessage(t, view.getContext());
            view.showError(error);
        }
    };

    interface SearchView extends BaseView {
        void setPathFromText(String pathFrom);

        void setPathToText(String pathTo);

        void initDatePickerList(List<Date> dates);

        void requestPermission(String[] permissions, int requestCode);

        void setLocationEnabled(boolean isEnabled);

        void setFindBtnEnabled(boolean isEnables);

        boolean isBackRouteToggleChecked();

        void showFullCalendarFragment(Date date);

        void showSelectTrainFragment(long stationFromCode, long stationToCode, long firstDate, long secondDate);

        void setBackRouteToggleChecked(boolean isChecked);

        void setBackRouteToggleEnabled(boolean isEnabled);

        void setSelectedDateLayoutVisible(Date firstDate, Date secondDate, String firstMonth, String secondMonth);

        void setSelectedDateLayoutInvisible();

        void setMonthName(String title);

        void setSelectedFirstPosition(int selectedFirstPosition);

        void setSelectedSecondPosition(int selectedSecondPosition);

        void setSelectingSecondDate(boolean isSelectingSecondDate);

        void scrollToSelectedPosition(int position);
    }
}
