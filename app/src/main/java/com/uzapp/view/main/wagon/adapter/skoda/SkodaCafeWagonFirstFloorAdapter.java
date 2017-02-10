package com.uzapp.view.main.wagon.adapter.skoda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uzapp.R;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

/**
 * Created by viktoria on 2/10/17.
 */

public class SkodaCafeWagonFirstFloorAdapter extends SkodaSecondWagonFirstFloorAdapter {
    protected final int CAFE_VIEW_TYPE = 7;
    private int cafePosition = 1;

    public SkodaCafeWagonFirstFloorAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
        lastPlaceNumber = 121;
        stairsLeftPosition = 2;
        stairsRightPosition = 15;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case CAFE_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_image, parent, false);
                viewHolder = new ImageItemHolder(view);
                break;
            case STAIRS_LEFT_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_image, parent, false);
                viewHolder = new ImageItemHolder(view);
                break;
            default:
                viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    int getPlacesBefore(int position) {
        int lastPlace = 0;
        if (position < stairsRightPosition) {
            lastPlace = firstPlaceNumber + (position - 4) * usualRowPlacesCount  + beforeAfterStairsRowPlacesCount;
        } else {
            lastPlace = firstPlaceNumber + (position - 6) * usualRowPlacesCount + stairsRowPlacesCount + beforeAfterStairsRowPlacesCount * 2;
        }
        return lastPlace;
    }

    @Override
    void bindAfterLeftStairsPlace(BeforeAfterStairsRowItemHolder itemHolder, int position) {
        int size = itemHolder.buttonsList.size();
        for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i - 1);
            int placeNumber = firstPlaceNumber + size - i;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case CAFE_VIEW_TYPE:
                ImageItemHolder cafeItemHolder = (ImageItemHolder) holder;
                bindImageHolder(cafeItemHolder, R.drawable.skoda_cafe);
                break;
            case STAIRS_LEFT_VIEW_TYPE:
                ImageItemHolder stairsLeftHolder = (ImageItemHolder) holder;
                bindImageHolder(stairsLeftHolder, R.drawable.skoda_stairs_left);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return (lastPlaceNumber - firstPlaceNumber + 1 - stairsRowPlacesCount - beforeAfterStairsRowPlacesCount * 2) / usualRowPlacesCount + 7;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == cafePosition) {
            return CAFE_VIEW_TYPE;
        } else return super.getItemViewType(position);
    }
}
