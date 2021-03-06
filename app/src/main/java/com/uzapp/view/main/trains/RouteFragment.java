package com.uzapp.view.main.trains;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.Train;
import com.uzapp.pojo.route.RouteCountry;
import com.uzapp.pojo.route.RouteResponse;
import com.uzapp.pojo.route.RouteStation;
import com.uzapp.network.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 28.07.16.
 */
public class RouteFragment extends Fragment {
    private SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_FORTMAT);
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DAY_MONTH_YEAR_FORMAT);
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.trainName) TextView trainName;
    @BindView(R.id.stationFrom) TextView stationFrom;
    @BindView(R.id.stationTo) TextView stationTo;
    @BindView(R.id.departureFullDate) TextView departureFullDate;
    @BindView(R.id.departureTime) TextView departureTime;
    @BindView(R.id.arrivalFullDate) TextView arrivalFullDate;
    @BindView(R.id.arrivalTime) TextView arrivalTime;
    @BindView(R.id.trainRouteListView) RecyclerView trainRouteListView;
    @BindDimen(R.dimen.trains_route_divider_padding_left) int dividerPaddingLeft;
    private Unbinder unbinder;
    private RouteAdapter adapter;
    private Train train;
    private long stationFromCode, stationToCode, date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.train_route_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initExtras();
        setupViews();
        setupList();
        loadTrainRoute();
        return view;
    }

    public static RouteFragment getInstance(Train train, long stationFromCode, long stationToCode, long date) {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        args.putLong("stationFromCode", stationFromCode);
        args.putLong("stationToCode", stationToCode);
        args.putLong("date", date);
        args.putParcelable("train", Parcels.wrap(train));
        fragment.setArguments(args);
        return fragment;
    }

    private void initExtras() {
        Bundle args = getArguments();
        stationFromCode = args.getLong("stationFromCode");
        stationToCode = args.getLong("stationToCode");
        date = args.getLong("date");
        train = Parcels.unwrap(args.getParcelable("train"));
    }

    private void setupViews() {
        toolbarTitle.setText(R.string.train_route_title);
        trainName.setText(train.getNumber());
        stationFrom.setText(train.getStationFromName());
        stationTo.setText(train.getStationToName());
        Date departureDate = new Date(TimeUnit.SECONDS.toMillis(train.getDepartureDate()));
        Date arrivalDate = new Date(TimeUnit.SECONDS.toMillis(train.getArrivalDate()));
        arrivalFullDate.setText(dateFormat.format(arrivalDate));
        arrivalTime.setText(timeFormat.format(arrivalDate));
        departureFullDate.setText(dateFormat.format(departureDate));
        departureTime.setText(timeFormat.format(departureDate));
    }

    private void setupList() {
        adapter = new RouteAdapter(getContext());
        trainRouteListView.setLayoutManager(new LinearLayoutManager(getContext()));
        trainRouteListView.setAdapter(adapter);
        trainRouteListView.addItemDecoration(new RouteVerticalAndHorizontalDivider(getContext()));
//        //todo add api request and remove test data
//        List<RouteStation> stationList = new ArrayList<>();
//        RouteStation routeStation = new RouteStation();
//        routeStation.setArrivalTime("12:30:01");
//        routeStation.setDepartureTime("12:34:08");
//        routeStation.setDistance(29);
//        routeStation.setName("Тарасовка");
//        for (int i = 0; i < 15; i++) {
//            stationList.add(routeStation);
//        }
    }

    private void loadTrainRoute() {
        Call<RouteResponse> routeResponseCall = ApiManager.getApi(getContext()).getTrainRoute(stationFromCode, stationToCode, train.getNumber(), train.getDepartureDate());
        routeResponseCall.enqueue(routeCallback);
    }

    @OnClick(R.id.ticketCloseBtn)
    void onCloseBtnClicked() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.shareBtn)
    void onShareBtnClicked() {
//TODO add sharing feature in next sprints
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Callback<RouteResponse> routeCallback = new Callback<RouteResponse>() {
        @Override
        public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
            if (response.isSuccessful()) {
                RouteResponse routeResponse = response.body();
                List<RouteCountry> routeCountries = routeResponse.getCountries();
                List<RouteStation> routeStations = new ArrayList<>();
                for (RouteCountry country : routeCountries) {
                    routeStations.addAll(country.getStations());
                }
                adapter.setRouteStations(routeStations);
                adapter.notifyDataSetChanged();
            } else {
                String error = ApiErrorUtil.getErrorMessage(response, getActivity());
                CommonUtils.showSnackbar(getView(), error);
            }
        }

        @Override
        public void onFailure(Call<RouteResponse> call, Throwable t) {
            if (getView() != null && t != null) {
                String error = ApiErrorUtil.getErrorMessage(t, getActivity());
                CommonUtils.showSnackbar(getView(), error);
            }
        }
    };

}
