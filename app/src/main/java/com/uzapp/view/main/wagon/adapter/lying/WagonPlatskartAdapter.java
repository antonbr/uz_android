package com.uzapp.view.main.wagon.adapter.lying;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.view.main.wagon.adapter.SimpleWagonAdapter;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by viktoria on 2/3/17.
 */

public class WagonPlatskartAdapter extends SimpleWagonAdapter {
    private final int PLACES_IN_PLATSKART_SECTION_MAIN = 4;
    private final int PLACES_IN_PLATSKART_SECTION_SIDE = 2;

    public WagonPlatskartAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == USUAL_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_platskart, parent, false);
            viewHolder = new PlatskartItemHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == USUAL_VIEW_TYPE) {

            List<Button> buttonList = ((PlatskartItemHolder) holder).buttonList;
            for (int i = 1; i <= buttonList.size()-2; i++) {
                int placeNumber = (position - 1) * PLACES_IN_PLATSKART_SECTION_MAIN + i;
                initPlaceButton(buttonList.get(i-1), placeNumber);
            }
            for(int i=buttonList.size()-1; i<=buttonList.size();i++){
                int placeNumber = wagon.getPlacesCount() - buttonList.size()+i - (position - 1) * PLACES_IN_PLATSKART_SECTION_SIDE;
                initPlaceButton(buttonList.get(i-1), placeNumber);
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return wagon.getPlacesCount() / (PLACES_IN_PLATSKART_SECTION_MAIN + PLACES_IN_PLATSKART_SECTION_SIDE) + 2;
    }


    class PlatskartItemHolder extends RecyclerView.ViewHolder {
        @BindViews({R.id.btnPlaceLowStandardLeft, R.id.btnPlaceUpperStandardLeft, R.id.btnPlaceLowStandardRight, R.id.btnPlaceUpperStandardRight,
                R.id.btnPlaceLowSide, R.id.btnPlaceUpperSide})
        List<Button> buttonList;

        public PlatskartItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.btnPlaceLowStandardLeft, R.id.btnPlaceUpperStandardLeft, R.id.btnPlaceLowStandardRight, R.id.btnPlaceUpperStandardRight,
                R.id.btnPlaceLowSide, R.id.btnPlaceUpperSide})
        void onClickPlaceBtn(Button button) {
            int placeNumber = Integer.valueOf(button.getText().toString());
            String placeType = (CommonUtils.isOdd(placeNumber))
                    ? context.getString(R.string.filter_bottom) : context.getString(R.string.filter_top);
            toggleSelection(placeNumber, getAdapterPosition(), placeType); //todo
        }
    }
}
