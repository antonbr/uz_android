package com.uzapp.view.trains;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.pojo.TrainPlace;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vika on 27.07.16.
 */
public class PlaceTypesAdapter extends RecyclerView.Adapter<PlaceTypesAdapter.PlaceTypeHolder> {
    private List<TrainPlace> placeList = new ArrayList<>();
    private String[] shortCarriageTypeName, longCarriageTypeName;

    public PlaceTypesAdapter(Context context, List<TrainPlace> placeList) {
        this.placeList = placeList;
        shortCarriageTypeName = context.getResources().getStringArray(R.array.carriage_types_short);
        longCarriageTypeName = context.getResources().getStringArray(R.array.carriage_types_long);
    }

    @Override
    public PlaceTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_type_item_view, parent, false);
        return new PlaceTypeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceTypeHolder holder, int position) {
        TrainPlace trainPlace = placeList.get(position);
        holder.availablePlaceCount.setText(String.valueOf(trainPlace.getTotal()));
        String shortPlaceType = trainPlace.getType();
        String longPlaceType = null;
        for (int i = 0; i < shortCarriageTypeName.length; i++) {
            if (shortCarriageTypeName[i].equals(shortPlaceType)) {
                longPlaceType = longCarriageTypeName[i];
                break;
            }
        }
        if (longPlaceType != null) {
            holder.placeTypeName.setText(longPlaceType);
        }
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class PlaceTypeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.placeTypeName) TextView placeTypeName;
        @BindView(R.id.minPlacePrice) TextView minPlacePrice;
        @BindView(R.id.availablePlaceCount) TextView availablePlaceCount;

        public PlaceTypeHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
