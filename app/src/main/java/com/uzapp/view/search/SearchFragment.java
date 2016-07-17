package com.uzapp.view.search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.uzapp.MainActivity;
import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.Station;
import com.uzapp.util.Constants;
import com.uzapp.view.search.station.StationSearchFragment;
import com.uzapp.view.utils.CheckableImageView;
import com.uzapp.view.utils.HorizontalSpaceItemDecoration;

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
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 13.07.16.
 */
public class SearchFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected static final int SELECT_STATION_FROM_REQUEST_CODE = 11;
    protected static final int SELECT_STATION_TO_REQUEST_CODE = 12;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private SimpleDateFormat monthDateFormatter = new SimpleDateFormat("LLLL", Locale.getDefault());
    @BindView(R.id.pathFrom) EditText pathFrom;
    @BindView(R.id.pathTo) EditText pathTo;
    @BindView(R.id.useLocationBtn) CheckableImageView useLocationBtn;
    @BindView(R.id.backRouteBtn) ToggleButton backRouteBtn;
    @BindView(R.id.datePickerList) RecyclerView datePickerList;
    @BindView(R.id.monthName) TextView monthName;
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    private Unbinder unbinder;
    private GoogleApiClient googleApiClient;
    private Station nearestStation;
    private Station fromStation;
    private Station toStation;
    private Map<Integer, String> monthPositionMap;
    private LinearLayoutManager datePickerLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        StationSearchFragment fragment = new StationSearchFragment();
        fragment.setTargetFragment(this, view.getId() == R.id.pathFrom ? SELECT_STATION_FROM_REQUEST_CODE : SELECT_STATION_TO_REQUEST_CODE);
        ((MainActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
    }

    @OnClick(R.id.seeFullCalendarBtn)
    void seeFullCalendarBtn() {

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

        List<Date> dates = new ArrayList<Date>();
        monthPositionMap = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        dates.add(calendar.getTime());
        String currentMonthName = monthDateFormatter.format(calendar.getTime());
        monthPositionMap.put(0, currentMonthName);
        monthName.setText(currentMonthName);
        int lastMonth = calendar.get(Calendar.MONTH);
        for (int i = 0; i < Constants.MAX_DAYS; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1); //TODO check between years
            Date date = calendar.getTime();
            dates.add(date);
            if (calendar.get(Calendar.MONTH) != lastMonth) {
                lastMonth = calendar.get(Calendar.MONTH);
                monthPositionMap.put(dates.indexOf(date), monthDateFormatter.format(calendar.getTime()));
            }
        }
        DatePickerAdapter datePickerAdapter = new DatePickerAdapter(dates);
        datePickerList.setAdapter(datePickerAdapter);
        datePickerList.addItemDecoration(new HorizontalSpaceItemDecoration((int) getResources().getDimension(R.dimen.small_padding)));
        datePickerList.addOnScrollListener(dateScrollListener);
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

}
