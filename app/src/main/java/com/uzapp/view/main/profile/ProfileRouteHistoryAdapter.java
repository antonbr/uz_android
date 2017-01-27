package com.uzapp.view.main.profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.pojo.route.RouteHistoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 06.09.16.
 */
public class ProfileRouteHistoryAdapter extends RecyclerView.Adapter<ProfileRouteHistoryAdapter.StationHolder> {
    private List<RouteHistoryItem> routeHistoryItems = new ArrayList<>();
    private OnStationClickListener listener;

    public interface OnStationClickListener {
        void onStationItemClick(RouteHistoryItem routeHistoryItem);
    }

    public ProfileRouteHistoryAdapter(OnStationClickListener listener) {
        this.listener = listener;
    }

    public class StationHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public StationHolder(View view) {
            super(view);
            name = (TextView) view;
        }
    }

    public void setStations(List<RouteHistoryItem> stationsList) {
        this.routeHistoryItems.clear();
        this.routeHistoryItems.addAll(stationsList);
        notifyDataSetChanged();
    }


    public void clearStations() {
        this.routeHistoryItems.clear();
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
        final RouteHistoryItem routeHistoryItem = routeHistoryItems.get(position);
        holder.name.setText(holder.itemView.getContext().getString(R.string.profile_route_history_item, routeHistoryItem.getStationFromName(), routeHistoryItem.getStationToName()));
        if (listener != null) {
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStationItemClick(routeHistoryItem);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return routeHistoryItems.size();
    }
}