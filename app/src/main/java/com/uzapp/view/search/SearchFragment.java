package com.uzapp.view.search;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.StationSearchResult;
import com.uzapp.view.search.utils.CheckableImageView;

import java.util.List;

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
    private static final int PERMISSION_ACESS_FINE_LOCATION = 1;
    @BindView(R.id.pathFrom) EditText pathFrom;
    @BindView(R.id.pathTo) EditText pathTo;
    @BindView(R.id.useLocationBtn) CheckableImageView useLocationBtn;
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    private Unbinder unbinder;
    private GoogleApiClient googleApiClient;
    private StationSearchResult nearestStation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.useLocationBtn)
    void onLocationBtnClicked() {
        useLocationBtn.toggle();
        pathFrom.setTranslationY(useLocationBtn.isChecked() ? hintPadding : 0);
        if (useLocationBtn.isChecked() && nearestStation != null) {
            pathFrom.setText(nearestStation.getName());
        } else if (!useLocationBtn.isChecked()) {
            pathFrom.setText("");
        } else if (nearestStation == null) {
            loadNearestStation();
        }
    }

    private boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ACESS_FINE_LOCATION);
    }


    private void loadNearestStation() {
        if (googleApiClient.isConnected()) {
            if (isLocationPermissionGranted()) {
                requestLocationPermission();
            } else {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if(lastLocation!=null){
                    double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
                    //double lat = 50.43994904, lon = 30.48856926; //TODO nothing found in vinnytsia, use kyiv for now
                    Call<List<StationSearchResult>> call = ApiManager.getApi(getContext()).getNearestStations(lat, lon);
                    call.enqueue(nearestStationCallback);
                } else{
                    useLocationBtn.setChecked(false);
                    //todo location not found message
                }

            }
        }
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
            case PERMISSION_ACESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadNearestStation();
                } else {
                    useLocationBtn.setChecked(false);
                }
                break;
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

    private Callback<List<StationSearchResult>> nearestStationCallback = new Callback<List<StationSearchResult>>() {
        @Override
        public void onResponse(Call<List<StationSearchResult>> call, Response<List<StationSearchResult>> response) {
            if (response.isSuccessful() && response.body().size() > 0) {
                List<StationSearchResult> result = response.body();
                nearestStation = result.get(0);
                if (useLocationBtn.isChecked()) {
                    pathFrom.setText(nearestStation.getName());
                }
            } else if (response.isSuccessful() && response.body().size() == 0) {
                //TODO show message no stations found
                useLocationBtn.setChecked(false);
            } else {
                //TODO
            }
        }

        @Override
        public void onFailure(Call<List<StationSearchResult>> call, Throwable t) {
            //TODO
        }
    };
}
