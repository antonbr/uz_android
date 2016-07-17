package com.uzapp.view.search;

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
import com.uzapp.pojo.StationSearchResult;
import com.uzapp.util.Constants;
import com.uzapp.view.search.utils.DividerItemDecoration;
import com.uzapp.view.search.utils.SearchEditText;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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
        return view;
    }

    private void searchStations(String query) {
        stationsHeader.setText(R.string.search_result);
        searchProgress.setVisibility(View.VISIBLE);
        Call<List<StationSearchResult>> call = ApiManager.getApi(getContext()).searchStations(query);
        call.enqueue(searchCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Callback<List<StationSearchResult>> searchCallback = new Callback<List<StationSearchResult>>() {
        @Override
        public void onResponse(Call<List<StationSearchResult>> call, Response<List<StationSearchResult>> response) {
            searchProgress.setVisibility(View.GONE);
            if (response.isSuccessful()) {
                adapter.setStations(response.body());
            }
        }

        @Override
        public void onFailure(Call<List<StationSearchResult>> call, Throwable t) {
            searchProgress.setVisibility(View.GONE);
        }
    };

    @Override
    public void onItemClick(StationSearchResult station) {
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
        } else{
            adapter.clearStations();
            stationsHeader.setText(R.string.popular_destinations);
        }
    }
}
