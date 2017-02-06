package com.uzapp.view.main.wagon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

import butterknife.BindView;
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
            int placeLowStandardLeft = 1 + (position - 1) * PLACES_IN_PLATSKART_SECTION_MAIN;
            int placeUpperStandardLeft = 2 + (position - 1) * PLACES_IN_PLATSKART_SECTION_MAIN;
            int placeLowStandardRight = 3 + (position - 1) * PLACES_IN_PLATSKART_SECTION_MAIN;
            int placeUpperStandardRight = 4 + (position - 1) * PLACES_IN_PLATSKART_SECTION_MAIN;
            int placeLowSide = wagon.getPlacesCount() - 1 - (position - 1) * PLACES_IN_PLATSKART_SECTION_SIDE;
            int placeUpperSide = wagon.getPlacesCount() - (position - 1) * PLACES_IN_PLATSKART_SECTION_SIDE;

            PlatskartItemHolder platskartItemHolder = (PlatskartItemHolder) holder;
            platskartItemHolder.btnPlaceLowStandardLeft.setText(Integer.toString(placeLowStandardLeft));
            platskartItemHolder.btnPlaceUpperStandardLeft.setText(Integer.toString(placeUpperStandardLeft));
            platskartItemHolder.btnPlaceLowStandardRight.setText(Integer.toString(placeLowStandardRight));
            platskartItemHolder.btnPlaceUpperStandardRight.setText(Integer.toString(placeUpperStandardRight));
            platskartItemHolder.btnPlaceLowSide.setText(Integer.toString(placeLowSide));
            platskartItemHolder.btnPlaceUpperSide.setText(Integer.toString(placeUpperSide));

            platskartItemHolder.btnPlaceLowStandardLeft.setEnabled(availablePlaces.contains(placeLowStandardLeft));
            platskartItemHolder.btnPlaceUpperStandardLeft.setEnabled(availablePlaces.contains(placeUpperStandardLeft));
            platskartItemHolder.btnPlaceLowStandardRight.setEnabled(availablePlaces.contains(placeLowStandardRight));
            platskartItemHolder.btnPlaceUpperStandardRight.setEnabled(availablePlaces.contains(placeUpperStandardRight));
            platskartItemHolder.btnPlaceLowSide.setEnabled(availablePlaces.contains(placeLowSide));
            platskartItemHolder.btnPlaceUpperSide.setEnabled(availablePlaces.contains(placeUpperSide));

            platskartItemHolder.btnPlaceLowStandardLeft.setSelected(selectedItems.get(placeLowStandardLeft));
            platskartItemHolder.btnPlaceUpperStandardLeft.setSelected(selectedItems.get(placeUpperStandardLeft));
            platskartItemHolder.btnPlaceLowStandardRight.setSelected(selectedItems.get(placeLowStandardRight));
            platskartItemHolder.btnPlaceUpperStandardRight.setSelected(selectedItems.get(placeUpperStandardRight));
            platskartItemHolder.btnPlaceLowSide.setSelected(selectedItems.get(placeLowSide));
            platskartItemHolder.btnPlaceUpperSide.setSelected(selectedItems.get(placeUpperSide));
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return wagon.getPlacesCount() / (PLACES_IN_PLATSKART_SECTION_MAIN + PLACES_IN_PLATSKART_SECTION_SIDE) + 2;
    }


    class PlatskartItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnPlaceLowStandardLeft) Button btnPlaceLowStandardLeft;
        @BindView(R.id.btnPlaceUpperStandardLeft) Button btnPlaceUpperStandardLeft;
        @BindView(R.id.btnPlaceLowStandardRight) Button btnPlaceLowStandardRight;
        @BindView(R.id.btnPlaceUpperStandardRight) Button btnPlaceUpperStandardRight;
        @BindView(R.id.btnPlaceLowSide) Button btnPlaceLowSide;
        @BindView(R.id.btnPlaceUpperSide) Button btnPlaceUpperSide;

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
