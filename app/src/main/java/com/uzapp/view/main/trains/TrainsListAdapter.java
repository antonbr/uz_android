package com.uzapp.view.main.trains;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.pojo.WagonType;
import com.uzapp.pojo.trains.Train;
import com.uzapp.pojo.trains.TrainPlace;
import com.uzapp.util.Constants;
import com.uzapp.view.common.VerticalDividerItemDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vika on 26.07.16.
 */
public class TrainsListAdapter extends RecyclerView.Adapter<TrainsListAdapter.TrainHolder> {
    private SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_FORTMAT);
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DAY_MONTH_WEEK_FORMAT);
    private SimpleDateFormat inputDateFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_FORTMAT);
    private List<Train> trainList = new ArrayList<>();
    private Context context;
    private OnTrainClickListener onTrainClickListener;

    public TrainsListAdapter(Context context, OnTrainClickListener onTrainClickListener) {
        this.context = context;
        this.onTrainClickListener = onTrainClickListener;
    }

    protected interface OnTrainClickListener {
        void onShareBtnClicked(Train train);

        void onInfoBtnClicked(Train train);

        void onWagonItemClicked(Train train, WagonType wagonType, String wagonClass);
    }

    public void addTrains(List<Train> trainList) {
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(train.getDepartureDate() * 1000);
        Date departureDate = calendar.getTime();
        calendar.setTimeInMillis(train.getArrivalDate() * 1000);
        Date arrivalDate = calendar.getTime();
        holder.departureTime.setText(timeFormat.format(departureDate));
        holder.departureDay.setText(dateFormat.format(departureDate));
        holder.arrivalTime.setText(timeFormat.format(arrivalDate));
        holder.arrivalDay.setText(dateFormat.format(arrivalDate));
        Date travelTimeDate = parseDate(train.getTravelTime());
        if (travelTimeDate != null) {
            calendar.setTime(travelTimeDate);
            int hours = calendar.get(Calendar.HOUR);
            int min = calendar.get(Calendar.MINUTE);
            holder.travelTime.setText(context.getString(R.string.train_travel_time, hours, min));
        } else {
            holder.travelTime.setText("");
        }
        holder.trainName.setText(train.getNumber());
        holder.stationFrom.setText(train.getStationFromName());
        holder.stationTo.setText(train.getStationToName());
        final WagonTypesAdapter.WagonTypeClickListener listener = new WagonTypesAdapter.WagonTypeClickListener() {
            @Override
            public void onWagonTypeClicked(WagonType wagonType, String wagonClass) {
                onTrainClickListener.onWagonItemClicked(train, wagonType, wagonClass);
            }
        };
        List<TrainPlace> trainPlaces = train.getPlaces();
        Iterator<TrainPlace> iterator = trainPlaces.iterator();
        while (iterator.hasNext()) {
            TrainPlace trainPlace = iterator.next();
            if (trainPlace.getTotal() == 0) {
                iterator.remove();
            }
        }
        WagonTypesAdapter placeTypesAdapter = new WagonTypesAdapter(context, listener, trainPlaces);
        holder.placeTypesList.setAdapter(placeTypesAdapter);
        holder.placeTypesList.setLayoutManager(new LinearLayoutManager(context));
        VerticalDividerItemDecoration itemDecoration = new VerticalDividerItemDecoration(context,
                R.drawable.divider_hint_color_horizontal, holder.padding, holder.padding);
        holder.placeTypesList.addItemDecoration(itemDecoration);
        holder.placeTypesList.setNestedScrollingEnabled(false);
        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrainClickListener.onInfoBtnClicked(train);
            }
        });
    }

    private Date parseDate(String date) {
        try {
            return inputDateFormat.parse(date);
        } catch (ParseException e) {
            Log.d(TrainsListAdapter.class.getName(), e.getMessage());
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return trainList.size();
    }

    public class TrainHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.departureTime) TextView departureTime;
        @BindView(R.id.departureDay) TextView departureDay;
        @BindView(R.id.arrivalTime) TextView arrivalTime;
        @BindView(R.id.arrivalDay) TextView arrivalDay;
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
