package com.uzapp.view.trains;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.pojo.RouteStation;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vika on 28.07.16.
 */
public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteStationHolder> {
    private SimpleDateFormat inputDateFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_SECONDS_FORMAT);
    private SimpleDateFormat outputDateFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_FORTMAT);
    private List<RouteStation> routeStations;
    private Context context;
    private int expandedPosition = -1;

    public RouteAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RouteStationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.train_route_item_view, parent, false);
        return new RouteStationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RouteStationHolder holder, final int position) {
        RouteStation station = routeStations.get(position);
        Date arrivalTime = parseDate(station.getArrivalTime());
        Date departureTime = parseDate(station.getDepartureTime());
        if (arrivalTime != null) {
            holder.time.setText(outputDateFormat.format(arrivalTime));
            holder.arrivalTime.setText(outputDateFormat.format(arrivalTime));
        } else {
            holder.time.setText("");
            holder.arrivalTime.setText("");
        }
        if (departureTime != null) {
            holder.departureTime.setText(outputDateFormat.format(departureTime));
        } else {
            holder.departureTime.setText("");
        }
        holder.stationName.setText(station.getName());
        holder.distance.setText(context.getString(R.string.train_route_distance_km, station.getDistance()));
        long stopTimeInMin = CommonUtils.getMinutesDifference(arrivalTime, departureTime);
        holder.stopTime.setText(context.getString(R.string.train_route_stop_time, stopTimeInMin));
        holder.expandedRouteLayout.setVisibility(position == expandedPosition ? View.VISIBLE : View.GONE);
        //TODO make first and last element bigger size

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandedPosition == position) {
                    expandedPosition = -1;
                    notifyItemChanged(position);
                } else {
                    // Check for an expanded view, collapse if you find one
                    if (expandedPosition >= 0) {
                        int prev = expandedPosition;
                        notifyItemChanged(prev);
                    }
                    // Set the current position to "expanded"
                    expandedPosition = position;
                    notifyItemChanged(expandedPosition);
                }
            }
        });
    }


    private Date parseDate(String date) {
        try {
            return inputDateFormat.parse(date);
        } catch (ParseException e) {
            Log.e(RouteAdapter.class.getName(), e.getMessage());
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return routeStations == null ? 0 : routeStations.size();
    }

    public void setRouteStations(List<RouteStation> routeStations) {
        this.routeStations = routeStations;
        notifyDataSetChanged();
    }

    public class RouteStationHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.time) TextView time;
        @BindView(R.id.stationName) TextView stationName;
        @BindView(R.id.stopTime) TextView stopTime;
        @BindView(R.id.arrivalTime) TextView arrivalTime;
        @BindView(R.id.departureTime) TextView departureTime;
        @BindView(R.id.distance) TextView distance;
        @BindView(R.id.expandedRouteLayout) ViewGroup expandedRouteLayout;
        @BindView(R.id.circle) View circle;
        @BindColor(R.color.textColorHint) int bigCircleColor;
        @BindColor(R.color.stationCircleColor) int smallCircleColor;

        public RouteStationHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
