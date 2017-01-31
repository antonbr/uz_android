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
import com.uzapp.pojo.route.Station;
import com.uzapp.util.CommonUtils;
import com.uzapp.view.common.VerticalDividerItemDecoration;
import com.uzapp.view.main.MainActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vika on 13.07.16.
 */
public class StationSearchFragment extends Fragment implements StationsSearchResultAdapter.OnStationClickListener, SearchEditText.ContentChangedListener, StationSearchPresenter.StationSearchView {
    @BindView(R.id.cityEditText) SearchEditText cityEditText;
    @BindView(R.id.searchProgress) ProgressBar searchProgress;
    @BindView(R.id.stationsList) RecyclerView stationsList;
    @BindView(R.id.stationsHeader) TextView stationsHeader;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindDimen(R.dimen.big_padding) int padding;
    private Unbinder unbinder;
    private StationsSearchResultAdapter adapter;
    private StationSearchPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.station_search_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        adapter = new StationsSearchResultAdapter(this);
        stationsList.setLayoutManager(new LinearLayoutManager(getContext()));
        stationsList.setAdapter(adapter);
        stationsList.addItemDecoration(new VerticalDividerItemDecoration(getContext(), R.drawable.list_divider, padding, 0));
        cityEditText.setContentChangedListener(this);
        presenter = new StationSearchPresenter();
        presenter.attachView(this, getArguments());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).hideNavigationBar();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    @OnClick(R.id.ticketCloseBtn)
    void onCloseBtnClick() {
        cityEditText.hideKeyboard();
        getActivity().onBackPressed();
    }

    @OnClick(R.id.okBtn)
    void onOkBtnClick() {
        presenter.onOkBtnClick();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }

    @Override
    public void onStationItemClick(Station station) {
       presenter.onStationItemClick(station);
    }

    @Override
    public void onSearchLetterEntered(String msg) {
        if (getView() != null) {
           presenter.onSearchLetterEntered(msg);
        }
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public void showStations(List<Station> stationList) {
        adapter.setStations(stationList);
    }

    @Override
    public void clearStations() {
        adapter.clearStations();
    }

    @Override
    public void setStationsHeaderText(String text) {
        stationsHeader.setText(text);
    }

    @Override
    public void setCityName(String text) {
        cityEditText.setText(text);
        cityEditText.setSelection(text.length());
    }

    @Override
    public void hideKeyboard() {
        cityEditText.hideKeyboard();
    }

    @Override
    public void returnResult(Station selectedStation) {
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            Intent i = new Intent();
            i.putExtra("station", Parcels.wrap(selectedStation));
            targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        }
        getActivity().onBackPressed();
    }

    @Override
    public void showProgress(boolean isLoading) {
        searchProgress.setVisibility(isLoading?View.VISIBLE:View.GONE);
    }

    @Override
    public void showError(String error) {
        CommonUtils.showSnackbar(getView(), error);
    }
}
