package com.uzapp.view.main.wagon.adapter.hyundai;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uzapp.R;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by viktoria on 2/7/17.
 */

public class HyundaiSecondClassFirstWagonAdapter extends HyundaiSecondClassAdapter {
    protected static final int DISABLED_PLACE_VIEW_TYPE = 5;
    protected static final int DISABLED_PLACE_COUNT_IN_SECTION = 2;

    public HyundaiSecondClassFirstWagonAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
        totalPlacesCount = 56;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == DISABLED_PLACE_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_hyundai_c2_disabled, parent, false);
            viewHolder = new HyundaiDisabledSecondClassItemHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == DISABLED_PLACE_VIEW_TYPE) {
            HyundaiDisabledSecondClassItemHolder itemHolder = (HyundaiDisabledSecondClassItemHolder) holder;
            bindDisabledSection(itemHolder, position);
        } else if (holder.getItemViewType() == HEADER_VIEW_TYPE) {
            ImageItemHolder headerItemHolder = (ImageItemHolder) holder;
            bindImageHolder(headerItemHolder, R.drawable.ic_header_hyundai_disabled);
        } else if (holder.getItemViewType() == FOOTER_VIEW_TYPE) {
            ImageItemHolder footerItemHolder = (ImageItemHolder) holder;
            bindImageHolder(footerItemHolder, R.drawable.ic_footer_hyundai_no_toilet );
        } else {
            super.onBindViewHolder(holder, position);
        }
    }


    private void bindDisabledSection(HyundaiDisabledSecondClassItemHolder holder, int position) {
        for (int i = 1; i <= holder.buttonsList.size(); i++) {
            Button placeBtn = holder.buttonsList.get(i - 1);
            int placeNumber = (position - 1) * DISABLED_PLACE_COUNT_IN_SECTION + i;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    @Override
    protected void bindUsualPlaces(HyundaiSecondClassItemHolder itemHolder, int position) {
        int size = itemHolder.buttonsList.size();
        for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i - 1);
            int placeNumber = i + (position - 3) * size + DISABLED_PLACE_COUNT_IN_SECTION * 2;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    @Override
    protected void bindLuggagePlaces(HyundaiLuggageSecondClassItemHolder itemHolder, int position) {
        for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i - 1);
            int placeNumber = i + (position - 3) * (PLACES_LEFT + PLACES_RIGHT) + DISABLED_PLACE_COUNT_IN_SECTION * 2;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1 || position == 2) {
            return DISABLED_PLACE_VIEW_TYPE;
        } else if (position == getItemCount() - 2) {
            return LUGGAGE_VIEW_TYPE;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return (totalPlacesCount - LUGGAGE_SECTION_PLACES - DISABLED_PLACE_COUNT_IN_SECTION * 2) / (PLACES_LEFT + PLACES_RIGHT) + 5;
    }

    class HyundaiDisabledSecondClassItemHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace})
        List<Button> buttonsList;

        public HyundaiDisabledSecondClassItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }
}
