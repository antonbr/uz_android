package com.uzapp.view.search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.uzapp.MainActivity;
import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.Station;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.search.date.PickDateFragment;
import com.uzapp.view.search.station.StationSearchFragment;

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

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 13.07.16.
 */
public class SearchFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DatePickerAdapter.OnDateClickListener {
    protected static final int SELECT_STATION_FROM_REQUEST_CODE = 11;
    protected static final int SELECT_STATION_TO_REQUEST_CODE = 12;
    protected static final int SELECT_FIRST_DATE = 13;
    protected static final int SELECT_SECOND_DATE = 14;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private SimpleDateFormat monthFormatter = new SimpleDateFormat("LLLL", Locale.getDefault());
    private SimpleDateFormat dayOfMonthFormatter = new SimpleDateFormat("dd");
    private SimpleDateFormat dayOfWeekFormatter = new SimpleDateFormat("EE");
    @BindView(R.id.pathFrom) EditText pathFrom;
    @BindView(R.id.pathTo) EditText pathTo;
    @BindView(R.id.useLocationBtn) CheckableImageView useLocationBtn;
    @BindView(R.id.backRouteToggle) ToggleButton backRouteToggle;
    @BindView(R.id.datePickerList) RecyclerView datePickerList;
    @BindView(R.id.monthName) TextView monthName;
    @BindView(R.id.findTicketsBtn) Button findTicketsBtn;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.selectedDateLayout) RelativeLayout selectedDateLayout;
    @BindView(R.id.firstMonthName) TextView firstMonthName;
    @BindView(R.id.secondMonthName) TextView secondMonthName;
    @BindView(R.id.firstDate) DateItemView firstDateLayout;
    @BindView(R.id.secondDate) DateItemView secondDateLayout;
    @BindView(R.id.inDays) TextView inDays;
    @BindView(R.id.seeFullCalendarBtn) Button seeFullCalendarBtn;
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    @BindDimen(R.dimen.small_padding) int datePickerItemPadding;
    private Unbinder unbinder;
    private GoogleApiClient googleApiClient;
    private Station nearestStation, fromStation, toStation;
    private Date firstDate, secondDate;
    private Map<Integer, String> monthPositionMap;
    private LinearLayoutManager datePickerLayoutManager;
    private DatePickerAdapter datePickerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText(R.string.search_title);
        initDatePickerList();
        return view;
    }

    @OnClick(R.id.useLocationBtn)
    void onLocationBtnClicked() {
        useLocationBtn.toggle();
        pathFrom.setTranslationY(useLocationBtn.isChecked() ? hintPadding : 0);
        if (useLocationBtn.isChecked() && nearestStation != null) {
            fromStation = nearestStation;
            pathFrom.setText(nearestStation.getName());
        } else if (!useLocationBtn.isChecked()) {
            pathFrom.setText("");
        } else if (nearestStation == null) {
            loadNearestStation();
        }
    }

    @OnClick({R.id.pathTo, R.id.pathFrom})
    void onSelectPathFromClick(View view) {
        StationSearchFragment fragment = StationSearchFragment.getInstance(getString(view.getId() == R.id.pathFrom ? R.string.search_path_from : R.string.search_path_to));
        fragment.setTargetFragment(this, view.getId() == R.id.pathFrom ? SELECT_STATION_FROM_REQUEST_CODE : SELECT_STATION_TO_REQUEST_CODE);
        ((MainActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
    }

    @OnTextChanged({R.id.pathTo, R.id.pathFrom})
    void onPathTextChanged() {
        checkAllFieldsFilled();
    }

    @OnCheckedChanged(R.id.backRouteToggle)
    void onBackRouteToggleChanged() {
        checkAllFieldsFilled();
    }

    @OnClick(R.id.seeFullCalendarBtn)
    void seeFullCalendarBtn() {
        PickDateFragment fragment = new PickDateFragment();
        fragment.setTargetFragment(this, backRouteToggle.isChecked() ? SELECT_SECOND_DATE : SELECT_FIRST_DATE);
        ((MainActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
    }

    @OnClick(R.id.findTicketsBtn)
    void onFindTicketsBtnClicked() {
        Snackbar.make(getView(), R.string.search_stations_not_valid, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.resetBtn)
    void onResetBtnClicked() {
        fromStation = null;
        toStation = null;
        pathFrom.getText().clear();
        pathTo.getText().clear();
        useLocationBtn.setChecked(false);
        firstDate = null;
        secondDate = null;
        datePickerAdapter.setSelectedFirstPosition(-1);
        datePickerAdapter.setSelectedSecondPosition(-1);
        setSelectedDateLayoutVisibility(false);
        backRouteToggle.setChecked(false);
        backRouteToggle.setEnabled(false);

    }

    @OnClick(R.id.resetDateBtn)
    void onResetDateBtnClicked() {
        secondDate = null;
        setSelectedDateLayoutVisibility(false);
    }

    private boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ACCESS_FINE_LOCATION);
    }


    private void loadNearestStation() {
        if (googleApiClient.isConnected()) {
            if (isLocationPermissionGranted()) {
                requestLocationPermission();
            } else {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (lastLocation != null) {
                    double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
                    Call<List<Station>> call = ApiManager.getApi(getContext()).getNearestStations(lat, lon);
                    call.enqueue(nearestStationCallback);
                } else {
                    useLocationBtn.setChecked(false);
                    //todo location not found message
                }

            }
        }
    }

    private void initDatePickerList() {
        datePickerLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        datePickerList.setLayoutManager(datePickerLayoutManager);

        final List<Date> dates = new ArrayList<Date>();
        monthPositionMap = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        dates.add(calendar.getTime());
        String currentMonthName = monthFormatter.format(calendar.getTime());
        monthPositionMap.put(0, currentMonthName);
        monthName.setText(currentMonthName);
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
        //datePickerList.addItemDecoration(new HorizontalSpaceItemDecoration(datePickerItemPadding));
        datePickerList.addOnScrollListener(dateScrollListener);
        datePickerList.post(new Runnable() {
            @Override
            public void run() {
                if (datePickerList != null) {
                    int itemWidth = datePickerList.getWidth() / Constants.DAYS_TO_SHOW_SMALL_CALENDAR;
                    datePickerAdapter = new DatePickerAdapter(dates, itemWidth, SearchFragment.this);
                    datePickerList.setAdapter(datePickerAdapter);
                    firstDateLayout.setViewWidth(itemWidth);
                    secondDateLayout.setViewWidth(itemWidth);
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleApiClient = new GoogleApiClient.Builder(getContext(), this, this).addApi(LocationServices.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadNearestStation();
                } else {
                    useLocationBtn.setChecked(false);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_STATION_FROM_REQUEST_CODE) {
            fromStation = Parcels.unwrap(data.getParcelableExtra("station"));
            pathFrom.setText(fromStation.getName());
        } else if (requestCode == SELECT_STATION_TO_REQUEST_CODE) {
            toStation = Parcels.unwrap(data.getParcelableExtra("station"));
            pathTo.setText(toStation.getName());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private Callback<List<Station>> nearestStationCallback = new Callback<List<Station>>() {
        @Override
        public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
            if (response.isSuccessful() && response.body().size() > 0) {
                List<Station> result = response.body();
                nearestStation = result.get(0);
                if (useLocationBtn.isChecked()) {
                    pathFrom.setText(nearestStation.getName());
                    fromStation = nearestStation;
                }
            } else if (response.isSuccessful() && response.body().size() == 0) {
                //TODO show message no stations found
                useLocationBtn.setChecked(false);
            } else {
                //TODO
            }
        }

        @Override
        public void onFailure(Call<List<Station>> call, Throwable t) {
            //TODO
        }
    };

    private RecyclerView.OnScrollListener dateScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int firstVisiblePosition = datePickerLayoutManager.findFirstCompletelyVisibleItemPosition();
                ListIterator<Integer> iterator = new ArrayList(monthPositionMap.keySet()).listIterator(monthPositionMap.size());
                while (iterator.hasPrevious()) {
                    Integer key = iterator.previous();
                    if (firstVisiblePosition >= key) {
                        monthName.setText(monthPositionMap.get(key));
                        break;
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    @Override
    public void onDateItemClick(int position, Date date) {
        if (!backRouteToggle.isChecked()) {
            firstDate = date;
            datePickerAdapter.setSelectedFirstPosition(position);
            backRouteToggle.setEnabled(true);
            checkAllFieldsFilled();
        } else if (backRouteToggle.isChecked() && date.after(firstDate)) {
            secondDate = date;
            datePickerAdapter.setSelectedSecondPosition(position); //todo not necessary
            setSelectedDateLayoutVisibility(true);
        }
    }

    private void checkAllFieldsFilled() {
        if (fromStation != null && toStation != null && firstDate != null &&
                ((backRouteToggle.isChecked() && secondDate != null) || !backRouteToggle.isChecked())) {
            findTicketsBtn.setEnabled(true);
        } else {
            findTicketsBtn.setEnabled(false);
        }
    }

    private void setSelectedDateLayoutVisibility(boolean visible) {
        selectedDateLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
        datePickerList.setVisibility(visible ? View.GONE : View.VISIBLE);
        monthName.setVisibility(visible ? View.GONE : View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) seeFullCalendarBtn.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, visible ? R.id.selectedDateLayout : R.id.datePickerList);
        seeFullCalendarBtn.setLayoutParams(layoutParams);
        backRouteToggle.setEnabled(!visible);
        if (visible) {
            firstDateLayout.bindDate(firstDate);
            secondDateLayout.bindDate(secondDate);
            firstDateLayout.setSelectedDayBackground();
            secondDateLayout.setSelectedDayBackground();
            firstMonthName.setText(monthFormatter.format(firstDate));
            secondMonthName.setText(monthFormatter.format(secondDate));
            int daysBetween = CommonUtils.getDaysDifference(secondDate, firstDate); //TODO second day must be after first one
            inDays.setText(getResources().getQuantityString(R.plurals.search_in_n_days, daysBetween, daysBetween));
        } else {
            secondDate = null;
            datePickerAdapter.setSelectedSecondPosition(-1);
        }
        checkAllFieldsFilled();
    }


}
