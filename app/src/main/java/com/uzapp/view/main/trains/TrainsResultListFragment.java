package com.uzapp.view.main.trains;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.Train;
import com.uzapp.pojo.TrainSearchResult;
import com.uzapp.pojo.prices.Prices;
import com.uzapp.util.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.wagon.fragment.WagonPlaceFragment;
import com.uzapp.view.utils.SpaceItemDecoration;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 27.07.16.
 */
public class TrainsResultListFragment extends Fragment implements TrainsListAdapter.OnTrainClickListener {
    private static final String TAG = TrainsResultListFragment.class.getName();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM");
    @BindView(R.id.trainsList) RecyclerView trainsList;
    @BindView(R.id.noContent) TextView noContent;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private Unbinder unbinder;
    private long stationFromCode, stationToCode, date;
    private Call<TrainSearchResult> trainSearchCall;
    private TrainsListAdapter trainsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.train_result_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initArguments();
        trainsAdapter = new TrainsListAdapter(getContext(), this);
        trainsList.setAdapter(trainsAdapter);
        trainsList.setLayoutManager(new LinearLayoutManager(getContext()));
        trainsList.addItemDecoration(new SpaceItemDecoration((int) getResources().getDimension(R.dimen.small_padding)));
        loadTrains();
        return view;
    }

    public static TrainsResultListFragment getInstance(long stationFromCode, long stationToCode, long date) {
        TrainsResultListFragment fragment = new TrainsResultListFragment();
        Bundle args = new Bundle();
        args.putLong("stationFromCode", stationFromCode);
        args.putLong("stationToCode", stationToCode);
        args.putLong("date", date);
        fragment.setArguments(args);
        return fragment;
    }

    public int getTrainsCount() {
        if (trainsAdapter == null) {
            return 0;
        }
        return trainsAdapter.getItemCount();
    }

    private void initArguments() {
        Bundle args = getArguments();
        stationFromCode = args.getLong("stationFromCode");
        stationToCode = args.getLong("stationToCode");
        date = args.getLong("date");
    }


    private void loadTrains() {
        showProgress(true);
        trainSearchCall = ApiManager.getApi(getContext()).searchTrains(stationFromCode, stationToCode, date);
        trainSearchCall.enqueue(callback);
    }

    private void showNoContentIfNeeded() {
        if (trainsAdapter.getItemCount() > 0) {
            noContent.setVisibility(View.GONE);
        } else {
            noContent.setVisibility(View.VISIBLE);
            noContent.setText(getString(R.string.trains_no_places, simpleDateFormat.format(new Date(date))));
        }

    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private Callback<TrainSearchResult> callback = new Callback<TrainSearchResult>() {
        @Override
        public void onResponse(Call<TrainSearchResult> call, Response<TrainSearchResult> response) {
            if (response.isSuccessful()) {
                trainsAdapter.addTrains(response.body().getTrains());

                Fragment parentFragment = getParentFragment();
                if (parentFragment != null && parentFragment instanceof SelectTrainFragment) {
                    ((SelectTrainFragment) parentFragment).onTrainsLoaded(date, trainsAdapter.getItemCount());
                }
                showNoContentIfNeeded();
            } else {
                String error = ApiErrorUtil.parseError(response);
                CommonUtils.showMessage(getView(), error);
                showNoContentIfNeeded();
            }
            showProgress(false);
        }

        @Override
        public void onFailure(Call<TrainSearchResult> call, Throwable t) {
            if (getView() != null && t != null) {
                CommonUtils.showMessage(getView(), t.getMessage());
                showNoContentIfNeeded();
                showProgress(false);
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (trainSearchCall != null) {
            trainSearchCall.cancel();
        }
        unbinder.unbind();
    }

    @Override
    public void onShareBtnClicked(Train train) {

    }

    @Override
    public void onInfoBtnClicked(Train train) {
        RouteFragment fragment = RouteFragment.getInstance(train, stationFromCode, stationToCode, date);
        ((MainActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    public void onWagonItemClicked(Train train, String wagonType, String wagonClass) {
//        Toast.makeText(getContext(), "On item clicked, see logs", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "train number: " + train.getNumber() + " wagon type: " + wagonType + " wagon class: " + wagonClass
                + " station from code: " + stationFromCode + " station to code: " + stationToCode + " date: " + date);

      /*  train use parcel annotations to be easily passed via intent:
         intent.putExtra("train", Parcels.wrap(train));
         Train train = Parcels.unwrap(intent.getParcelableExtra("train"));*/

        //wagon type and wagon class are in short form, full form can be found in string arrays.
        // maybe we should better refactor to enums

        //backend uses mocks for trains, that's why the result is always the same
        Call<Prices> call = ApiManager.getApi(getActivity()).getPrices(stationFromCode, stationToCode, train.getNumber(), date);
        call.enqueue(pricesCallback);
    }

    private Callback<Prices> pricesCallback = new Callback<Prices>() {
        @Override
        public void onResponse(Call<Prices> call, Response<Prices> response) {
            if (response.isSuccessful()) {
                Prices prices = response.body();
                ((MainActivity) getActivity()).replaceFragment(WagonPlaceFragment.newInstance(prices, 0), true);
            } else {
                String error = ApiErrorUtil.parseError(response);
                CommonUtils.showMessage(getView(), error);
            }
        }

        @Override
        public void onFailure(Call<Prices> call, Throwable t) {
            if (getView() != null && t != null) {
                CommonUtils.showMessage(getView(), t.getMessage());
            }
        }
    };
}
