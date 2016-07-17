package com.uzapp.view.search.station;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.PopularStation;
import com.uzapp.pojo.Station;
import com.uzapp.util.Constants;
import com.uzapp.view.utils.DividerItemDecoration;
import com.uzapp.view.utils.SearchEditText;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 13.07.16.
 */
public class StationSearchFragment extends Fragment implements StationsSearchResultAdapter.OnStationClickListener, SearchEditText.ContentChangedListener {
    @BindView(R.id.cityEditText) SearchEditText cityEditText;
    @BindView(R.id.searchProgress) ProgressBar searchProgress;
    @BindView(R.id.stationsList) RecyclerView stationsList;
    @BindView(R.id.stationsHeader) TextView stationsHeader;
    private Unbinder unbinder;
    private StationsSearchResultAdapter adapter;
    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.station_search_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        adapter = new StationsSearchResultAdapter(this);
        stationsList.setLayoutManager(new LinearLayoutManager(getContext()));
        stationsList.setAdapter(adapter);
        stationsList.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.stations_list_divider, R.dimen.big_padding));
        cityEditText.setContentChangedListener(this);
        showPopularStations();
        return view;
    }

    private void searchStations(String query) {
        stationsHeader.setText(R.string.search_result);
        searchProgress.setVisibility(View.VISIBLE);
        Call<List<Station>> call = ApiManager.getApi(getContext()).searchStations(query);
        call.enqueue(searchCallback);
    }

    private void showPopularStations() {
        RealmResults<PopularStation> popularStations = realm.where(PopularStation.class).findAll().sort("accessTime", Sort.DESCENDING);
        if (popularStations.size() > 0) {
            List<Station> stationList = new ArrayList<Station>(popularStations.size());
            for (PopularStation popularStation : popularStations) {
                stationList.add(new Station(popularStation.getCode(), popularStation.getName(), popularStation.getRailway()));
            }
            adapter.setStations(stationList);
            stationsHeader.setText(R.string.popular_destinations);
        } else{
            stationsHeader.setText("");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getContext()).build();
        realm = Realm.getInstance(realmConfig);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Callback<List<Station>> searchCallback = new Callback<List<Station>>() {
        @Override
        public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
            searchProgress.setVisibility(View.GONE);
            if (response.isSuccessful()) {
                adapter.setStations(response.body());
            }
        }

        @Override
        public void onFailure(Call<List<Station>> call, Throwable t) {
            searchProgress.setVisibility(View.GONE);
        }
    };

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
        } else if (popularStation == null) {
            PopularStation oldestStation = realm.where(PopularStation.class).findAll().sort("accessTime",Sort.DESCENDING).last();
            realm.beginTransaction();
            oldestStation.setValues(station.getCode(), station.getName(), station.getRailway(),
                    Calendar.getInstance().getTime());
            realm.commitTransaction();
        }
    }

    @Override
    public void onItemClick(Station station) {
        saveToPopularStations(station);
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            Intent i = new Intent();
            i.putExtra("station", Parcels.wrap(station));
            targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        }
        getActivity().onBackPressed();
    }

    @Override
    public void onSearchLetterEntered(String msg) {
        if (msg.length() >= Constants.SEARCH_MIN_LENGTH) {
            searchStations(msg);
        } else {
            adapter.clearStations();
            showPopularStations();
        }
    }
}
