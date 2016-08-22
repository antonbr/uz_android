package com.uzapp.view.main.search.station;

import android.app.Activity;
import android.content.Context;
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
import com.uzapp.util.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.utils.VerticalDividerItemDecoration;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindDimen(R.dimen.big_padding) int padding;
    private Unbinder unbinder;
    private StationsSearchResultAdapter adapter;
    private Realm realm;
    private Station selectedStation;
    private Call<List<Station>> searchStationCall;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.station_search_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("title")) {
            toolbarTitle.setText(arguments.getString("title"));
        }
        adapter = new StationsSearchResultAdapter(this);
        stationsList.setLayoutManager(new LinearLayoutManager(getContext()));
        stationsList.setAdapter(adapter);
        stationsList.addItemDecoration(new VerticalDividerItemDecoration(getContext(), R.drawable.list_divider, padding, 0));
        cityEditText.setContentChangedListener(this);
        showPopularStations();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).hideNavigationBar();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) getActivity()).showNavigationBar();
    }

    public static StationSearchFragment getInstance(String title) {
        StationSearchFragment fragment = new StationSearchFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    private void searchStations(String query) {
        stationsHeader.setText(R.string.search_result);
        searchProgress.setVisibility(View.VISIBLE);
        searchStationCall = ApiManager.getApi(getContext()).searchStations(query);
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
            adapter.setStations(stationList);
            stationsHeader.setText(R.string.search_popular_destinations);
        } else {
            stationsHeader.setText("");
        }
    }

    @OnClick(R.id.closeBtn)
    void onCloseBtnClick() {
        cityEditText.hideKeyboard();
        getActivity().onBackPressed();
    }

    @OnClick(R.id.okBtn)
    void onOkBtnClick() {
        if (selectedStation != null) {
            saveToPopularStations(selectedStation);
            Fragment targetFragment = getTargetFragment();
            if (targetFragment != null) {
                Intent i = new Intent();
                i.putExtra("station", Parcels.wrap(selectedStation));
                targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
            }
            cityEditText.hideKeyboard();
            getActivity().onBackPressed();
        } else {
            //todo show message
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
        if (searchStationCall != null) {
            searchStationCall.cancel();
        }
        unbinder.unbind();
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
        } else if (popularStation == null) {
            PopularStation oldestStation = realm.where(PopularStation.class).findAll().sort("accessTime", Sort.DESCENDING).last();
            realm.beginTransaction();
            oldestStation.setValues(station.getCode(), station.getName(), station.getRailway(),
                    Calendar.getInstance().getTime());
            realm.commitTransaction();
        }
    }

    @Override
    public void onStationItemClick(Station station) {
        selectedStation = station;
        cityEditText.setText(selectedStation.getName());
        cityEditText.setSelection(selectedStation.getName().length());
    }

    @Override
    public void onSearchLetterEntered(String msg) {
        if (getView() != null) {
            if (selectedStation != null && !selectedStation.getName().equals(msg)) {
                selectedStation = null;
            }
            if (msg.length() >= Constants.SEARCH_MIN_LENGTH) {
                searchStations(msg);
            } else {
                adapter.clearStations();
                showPopularStations();
            }
        }
    }

    private Callback<List<Station>> searchCallback = new Callback<List<Station>>() {
        @Override
        public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
            if (getView() != null) {
                searchProgress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    adapter.setStations(response.body());
                } else {
                    String error = ApiErrorUtil.parseError(response);
                    CommonUtils.showMessage(getView(), error);
                }
            }
        }

        @Override
        public void onFailure(Call<List<Station>> call, Throwable t) {
            if (!call.isCanceled()) {
                searchProgress.setVisibility(View.GONE);
            }
        }
    };
}
