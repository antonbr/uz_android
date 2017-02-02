package com.uzapp.view.main.trains;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.pojo.WagonType;
import com.uzapp.view.common.VerticalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uzapp.R.id.arrivalDate;
import static com.uzapp.R.id.departureDate;

/**
 * Created by vika on 26.07.16.
 */
public class TrainsListAdapter extends RecyclerView.Adapter<TrainsListAdapter.TrainHolder> {
    private List<Train> trainList = new ArrayList<>();
    private Context context;
    private OnTrainClickListener onTrainClickListener;

    TrainsListAdapter(Context context, OnTrainClickListener onTrainClickListener) {
        this.context = context;
        this.onTrainClickListener = onTrainClickListener;
    }

    interface OnTrainClickListener {
        void onShareBtnClicked(Train train);

        void onInfoBtnClicked(Train train);

        void onWagonItemClicked(Train train, WagonType wagonType);
    }

    void showTrains(List<Train> trainList) {
        this.trainList = trainList;
        notifyDataSetChanged();
    }

    @Override
    public TrainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.train_item_view, parent, false);
        return new TrainHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrainHolder holder, final int position) {
        final Train train = trainList.get(position);

        holder.departureTime.setText(train.getDepartureTime());
        holder.departureDay.setText(train.getDepartureDate());
        holder.arrivalTime.setText(train.getArrivalTime());
        holder.arrivalDay.setText(train.getArrivalDate());
            holder.travelTime.setText(train.getTravelTime());
        holder.trainName.setText(train.getTrainName());
        holder.stationFrom.setText(train.getStationFrom());
        holder.stationTo.setText(train.getStationTo());

        final WagonTypesAdapter.WagonTypeClickListener listener = new WagonTypesAdapter.WagonTypeClickListener() {
            @Override
            public void onWagonTypeClicked(WagonType wagonType) {
                onTrainClickListener.onWagonItemClicked(train, wagonType);
            }
        };

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrainClickListener.onInfoBtnClicked(train);
            }
        });

        List<TrainPlace> trainPlaces = train.getTrainPlaces();
        WagonTypesAdapter placeTypesAdapter = new WagonTypesAdapter(context, listener, trainPlaces);
        holder.placeTypesList.setAdapter(placeTypesAdapter);
        holder.placeTypesList.setLayoutManager(new LinearLayoutManager(context));
        VerticalDividerItemDecoration itemDecoration = new VerticalDividerItemDecoration(context,
                R.drawable.divider_hint_color_horizontal, holder.padding, holder.padding);
        holder.placeTypesList.addItemDecoration(itemDecoration);
        holder.placeTypesList.setNestedScrollingEnabled(false);

    }

    @Override
    public int getItemCount() {
        return trainList.size();
    }

    public class TrainHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.departureTime) TextView departureTime;
        @BindView(departureDate) TextView departureDay;
        @BindView(R.id.arrivalTime) TextView arrivalTime;
        @BindView(arrivalDate) TextView arrivalDay;
        @BindView(R.id.travelTime) TextView travelTime;
        //        @BindView(R.id.ticketTypeText) TextView ticketTypeText;
//        @BindView(R.id.ticketTypeImage) ImageView ticketTypeImage;
        @BindView(R.id.trainName) TextView trainName;
        @BindView(R.id.stationFrom) TextView stationFrom;
        @BindView(R.id.stationTo) TextView stationTo;
        @BindView(R.id.infoButton) ImageButton infoButton;
        @BindView(R.id.shareButton) ImageButton shareButton;
        @BindView(R.id.placeTypesList) RecyclerView placeTypesList;
        @BindDimen(R.dimen.trains_padding_inside) int padding;

        public TrainHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
