package com.uzapp.view.main.trains;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.pojo.WagonType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vika on 27.07.16.
 */
public class WagonTypesAdapter extends RecyclerView.Adapter<WagonTypesAdapter.PlaceTypeHolder> {
    private List<TrainPlace> placeList = new ArrayList<>();
    private Context context;
    private WagonTypeClickListener listener;


    interface WagonTypeClickListener {
        void onWagonTypeClicked(WagonType wagonType);
    }

    WagonTypesAdapter(Context context, WagonTypeClickListener listener, List<TrainPlace> placeList) {
        this.placeList = placeList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public PlaceTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_type_item_view, parent, false);
        return new PlaceTypeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceTypeHolder holder, int position) {
        final TrainPlace trainPlace = placeList.get(position);
        holder.availablePlaceCount.setText(trainPlace.getAvailablePlaceCount());
        holder.minPlacePrice.setText(trainPlace.getMinPrice());
        holder.placeTypeName.setText(trainPlace.getWagonTypeStr());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onWagonTypeClicked(trainPlace.getWagonType());
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    class PlaceTypeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.placeTypeName) TextView placeTypeName;
        @BindView(R.id.minPlacePrice) TextView minPlacePrice;
        @BindView(R.id.availablePlaceCount) TextView availablePlaceCount;

        PlaceTypeHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
