package com.uzapp.view.main.wagon.adapter.tarpan;

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
 * Created by viktoria on 2/9/17.
 */

public class TarpanSecondClassEconomAdapter extends TarpanSecondClassAdapter {
    protected static final int FIRST_ROW_VIEW_TYPE = 7;
    protected static final int LAST_ROW_VIEW_TYPE = 8;
    private int firstRowPosition = 2;
    private int firstRowPlacesCount = 4;
    private int lastRowPlacesCount = 4;

    public TarpanSecondClassEconomAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
        totalPlacesCount = 112;
        cupboardPosition = Integer.MAX_VALUE; //no cupboard and no luggage
        tablePositionSecond = 20;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == FIRST_ROW_VIEW_TYPE || viewType == LAST_ROW_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_tarpan_c2, parent, false);
            viewHolder = new TarpanSecondClassItemHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case FIRST_ROW_VIEW_TYPE:
                ((TarpanSecondClassItemHolder) holder).buttonsList.get(2).setVisibility(View.INVISIBLE);
                bindFirstRow(((TarpanSecondClassItemHolder) holder).buttonsList, position);
                break;
            case LAST_ROW_VIEW_TYPE:
                ((TarpanSecondClassItemHolder) holder).buttonsList.get(2).setVisibility(View.INVISIBLE);
                bindLastRow(((TarpanSecondClassItemHolder) holder).buttonsList, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    private void bindFirstRow(List<Button> buttonList, int position) {
        int size = buttonList.size() - 1;
        int lastI = -1;
        for (int i = 0; i < buttonList.size(); i++) {
            Button placeBtn = buttonList.get(i);
            if (placeBtn.getVisibility() == View.INVISIBLE) continue;
            lastI++;
            int iter = lastI == 0 ? 1 : lastI;
            iter = lastI == 1 ? 0 : iter;
            int placeNumber = size - iter + getRowPosition(position) * size + toiletRowPlacesCount;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    @Override
    void bindRows(List<Button> buttonList, int position) {
        int size = buttonList.size();
        for (int i = 0; i < buttonList.size(); i++) {
            Button placeBtn = buttonList.get(i);
            int iter = i == 0 ? 1 : i;
            iter = i == 1 ? 0 : iter;
            int placeNumber = size - iter + getRowPosition(position) * size + toiletRowPlacesCount + firstRowPlacesCount;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    private void bindLastRow(List<Button> buttonList, int position) {
        int size = buttonList.size() - 1;
        int lastI = -1;
        for (int i = 0; i < buttonList.size(); i++) {
            Button placeBtn = buttonList.get(i);
            if (placeBtn.getVisibility() == View.INVISIBLE) continue;
            lastI++;
            int iter = lastI == 0 ? 1 : lastI;
            iter = lastI == 1 ? 0 : iter;
            int placeNumber = size - iter + getRowPosition(position) * usualRowPlacesCount + toiletRowPlacesCount + firstRowPlacesCount;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    @Override
    void bindLastRowToilet(TarpanToiletRightFirstClassItemHolder itemHolder, int position) {
        for (int i = 0; i < itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i);
            int placeNumber = i + 1 + getRowPosition(position) * usualRowPlacesCount + toiletRowPlacesCount + firstRowPlacesCount + lastRowPlacesCount;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    @Override
    int getRowPosition(int position) {
        int rowPosition = super.getRowPosition(position);
        if (position > firstRowPosition) {
            rowPosition--;
        }
        if (position > getItemCount() - firstRowPosition - 1) {
            rowPosition--;
        }
        return rowPosition;
    }

    @Override
    public int getItemCount() {
        return (totalPlacesCount - toiletRowPlacesCount * 2) / usualRowPlacesCount + 8;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == firstRowPosition) {
            return FIRST_ROW_VIEW_TYPE;
        } else if (position == getItemCount() - firstRowPosition - 1) {
            return LAST_ROW_VIEW_TYPE;
        } else
            return super.getItemViewType(position);
    }

}
