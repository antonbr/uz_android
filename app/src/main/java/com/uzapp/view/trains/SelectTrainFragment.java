package com.uzapp.view.trains;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.TrainSearchResult;
import com.uzapp.view.utils.SpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 26.07.16.
 */
public class SelectTrainFragment extends Fragment {
    private static final String TAG = SelectTrainFragment.class.getName();
    @BindView(R.id.trainsList) RecyclerView trainsList;
    private Unbinder unbinder;
    private long stationFromCode, stationToCode, firstDate, secondDate;
    private boolean isGoingBack = false;
    private Call<TrainSearchResult> trainSearchCall;
    private TrainsAdapter trainsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_train_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initArguments();
        trainsAdapter = new TrainsAdapter(getContext());
        trainsList.setAdapter(trainsAdapter);
        trainsList.setLayoutManager(new LinearLayoutManager(getContext()));
        trainsList.addItemDecoration(new SpaceItemDecoration((int) getResources().getDimension(R.dimen.small_padding)));
        loadTrainsFirstWay();
        return view;
    }

    /*
    second date here is date for return back option, in api there is date2 with other meaning
     */
    public static SelectTrainFragment getInstance(long stationFromCode, long stationToCode, long firstDate, long secondDate) {
        SelectTrainFragment fragment = new SelectTrainFragment();
        Bundle args = new Bundle();
        args.putLong("stationFromCode", stationFromCode);
        args.putLong("stationToCode", stationToCode);
        args.putLong("firstDate", firstDate);
        if (secondDate != 0) {
            args.putLong("secondDate", secondDate);
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void initArguments() {
        Bundle args = getArguments();
        stationFromCode = args.getLong("stationFromCode");
        stationToCode = args.getLong("stationToCode");
        firstDate = args.getLong("firstDate");
        if (args.containsKey("secondDate")) {
            secondDate = args.getLong("secondDate");
            isGoingBack = true;
        }
    }

    private void loadTrainsFirstWay() {
        trainSearchCall = ApiManager.getApi(getContext()).searchTrains(stationFromCode, stationToCode, firstDate);
        trainSearchCall.enqueue(callback);
    }

    private Callback<TrainSearchResult> callback = new Callback<TrainSearchResult>() {
        @Override
        public void onResponse(Call<TrainSearchResult> call, Response<TrainSearchResult> response) {
            if (response.isSuccessful()) {
                trainsAdapter.addTrains(response.body().getTrains());
            } else {
                Log.d(TAG, response.message());
                Snackbar.make(getView(), response.message(), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<TrainSearchResult> call, Throwable t) {
            if (getView() != null && t != null) {
                Log.d(TAG, t.getMessage());
                Snackbar.make(getView(), t.getMessage(), Snackbar.LENGTH_LONG).show();
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
}
