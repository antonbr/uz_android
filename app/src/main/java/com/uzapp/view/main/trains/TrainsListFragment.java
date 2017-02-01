package com.uzapp.view.main.trains;

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
import com.uzapp.pojo.WagonType;
import com.uzapp.pojo.prices.Prices;
import com.uzapp.pojo.trains.Train;
import com.uzapp.view.BaseFragment;
import com.uzapp.view.common.SpaceItemDecoration;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.wagon.fragment.WagonPlaceFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by vika on 27.07.16.
 */
public class TrainsListFragment extends BaseFragment implements TrainsListAdapter.OnTrainClickListener, TrainsListPresenter.TrainsListView {
    private static final String TAG = TrainsListFragment.class.getName();
    @BindView(R.id.trainsList) RecyclerView trainsList;
    @BindView(R.id.noContent) TextView noContent;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private Unbinder unbinder;
    private TrainsListAdapter trainsAdapter;
    private TrainsListPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.train_result_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        trainsAdapter = new TrainsListAdapter(getContext(), this);
        trainsList.setAdapter(trainsAdapter);
        trainsList.setLayoutManager(new LinearLayoutManager(getContext()));
        trainsList.addItemDecoration(new SpaceItemDecoration((int) getResources().getDimension(R.dimen.small_padding)));
        presenter = new TrainsListPresenter(getArguments());
        presenter.attachView(this);
        return view;
    }

    public static TrainsListFragment getInstance(long stationFromCode, long stationToCode, long date) {
        TrainsListFragment fragment = new TrainsListFragment();
        fragment.setArguments(TrainsListPresenter.getArgs(stationFromCode, stationToCode, date));
        return fragment;
    }

    public int getTrainsCount() {
        return presenter.getTrainsCount();
    }

    @Override
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void onShareBtnClicked(Train train) {

    }

    @Override
    public void onInfoBtnClicked(Train train) {
        presenter.onInfoBtnClicked(train);
    }

    @Override
    public void showRouteFragment(Train train, long stationFromCode, long stationToCode, long date) {
        RouteFragment fragment = RouteFragment.getInstance(train, stationFromCode, stationToCode, date);
        ((MainActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    public void showWagonPlaceFragment(Prices prices, int position, int departureDate, int arrivalDate, long selectDate, WagonType type) {
        ((MainActivity) getActivity()).replaceFragment(WagonPlaceFragment
                .newInstance(prices, 0, departureDate, arrivalDate, selectDate, type), true);
    }

    @Override
    public void onWagonItemClicked(Train train, WagonType wagonType, String wagonClass) {
        presenter.onWagonItemClicked(train, wagonType);
    }

    @Override
    public void showTrainsList(long date, List<Train> trains) {
        trainsAdapter.addTrains(trains);
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof TabbedTrainFragment) {
            ((TabbedTrainFragment) parentFragment).onTrainsLoaded(date, trainsAdapter.getItemCount());
        }
    }

    @Override
    public void showNoContent(boolean needToShowNoContentView, String noContentText) {
        noContent.setVisibility(needToShowNoContentView ? View.VISIBLE : View.GONE);
        noContent.setText(noContentText);
    }

}
