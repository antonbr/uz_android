package com.uzapp.view.main.wagon.adapter;

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
 * Created by viktoria on 2/7/17.
 */

public class HyundaiSecondClassLastWagonAdapter extends HyundaiSecondClassAdapter {
    public HyundaiSecondClassLastWagonAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
        totalPlacesCount = 61;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == USUAL_VIEW_TYPE || viewType == LAST_SECTION_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_hyundai_c2_right, parent, false);
            viewHolder = new HyundaiSecondClassItemHolder(view);
        } else if (viewType == LUGGAGE_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_hyundai_c2_luggage_right, parent, false);
            viewHolder = new HyundaiLuggageSecondClassItemHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    protected void bindUsualPlaces(HyundaiSecondClassItemHolder itemHolder, int position) {
        int size = itemHolder.buttonsList.size();
        for (int i = 0; i < size; i++) {
            Button placeBtn = itemHolder.buttonsList.get(i);
            int placeNumber = (getItemCount() - position - 3) * size + LAST_SECTION_PLACES_COUNT+(size-i);
            placeBtn.setText(String.valueOf(placeNumber));
            placeBtn.setEnabled(availablePlaces.contains(placeNumber));
            placeBtn.setSelected(selectedItems.get(placeNumber));
        }
    }

    @Override
    protected void bindLastSection(HyundaiSecondClassItemHolder itemHolder, int position) {
        int lastPlaceNumber = 0;
        for (int i = itemHolder.buttonsList.size() - 1; i >= 0; i--) {
            Button placeBtn = itemHolder.buttonsList.get(i);
            if (placeBtn.getId() == LAST_SECTION_NOT_VISIBLE_ID) {
                placeBtn.setVisibility(View.INVISIBLE);
                continue;
            }
            int placeNumber = ++lastPlaceNumber;
            placeBtn.setText(String.valueOf(placeNumber));
            placeBtn.setEnabled(availablePlaces.contains(placeNumber));
            placeBtn.setSelected(selectedItems.get(placeNumber));
        }
    }

    @Override
    protected void bindLuggagePlaces(HyundaiLuggageSecondClassItemHolder itemHolder, int position) {
        int size = itemHolder.buttonsList.size();
        for (int i = 0; i < size; i++) {
            Button placeBtn = itemHolder.buttonsList.get(i);
            int placeNumber = (getItemCount() - position - 3) * (PLACES_LEFT+PLACES_RIGHT) + LAST_SECTION_PLACES_COUNT+(size-i);
            placeBtn.setText(String.valueOf(placeNumber));
            placeBtn.setEnabled(availablePlaces.contains(placeNumber));
            placeBtn.setSelected(selectedItems.get(placeNumber));
        }
    }

}
