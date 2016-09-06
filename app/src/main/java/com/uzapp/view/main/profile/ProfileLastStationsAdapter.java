package com.uzapp.view.main.profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.pojo.Station;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 06.09.16.
 */
public class ProfileLastStationsAdapter extends RecyclerView.Adapter<ProfileLastStationsAdapter.StationHolder> {
    private List<Station> stationsList = new ArrayList<>();
    private OnStationClickListener listener;

    public interface OnStationClickListener {
        void onStationItemClick(Station station);
    }

    public ProfileLastStationsAdapter(OnStationClickListener listener) {
        this.listener = listener;
    }

    public class StationHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public StationHolder(View view) {
            super(view);
            name = (TextView) view;
        }
    }

    public void setStations(List<Station> stationsList) {
        this.stationsList.clear();
        this.stationsList.addAll(stationsList);
        notifyDataSetChanged();
    }


    public void clearStations() {
        this.stationsList.clear();
        notifyDataSetChanged();
    }

    @Override
    public StationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_station_item, parent, false);
        return new StationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StationHolder holder, int position) {
        final Station station = stationsList.get(position);
        holder.name.setText(station.getName());
        if (listener != null) {
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStationItemClick(station);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return stationsList.size();
    }
}