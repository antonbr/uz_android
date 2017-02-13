package com.uzapp.view.main.wagon.adapter.lying;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uzapp.R;
import com.uzapp.view.main.wagon.adapter.SimpleWagonAdapter;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by viktoria on 2/3/17.
 */

public class WagonLuxAdapter extends SimpleWagonAdapter {
    private final int PLACES_IN_LUX = 2;

    public WagonLuxAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
       super(wagon,availablePlaces, context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == USUAL_VIEW_TYPE) {
            View view =inflater.inflate(R.layout.item_lux, parent, false);
            viewHolder = new LuxItemHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == USUAL_VIEW_TYPE) {
            List<Button> buttonList = ((LuxItemHolder) holder).buttonList;
            for (int i = 1; i <= buttonList.size(); i++) {
                int placeNumber = (position - 1) * PLACES_IN_LUX + i;
                initPlaceButton(buttonList.get(i-1), placeNumber);
            }
        } else{
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return wagon.getPlacesCount() / PLACES_IN_LUX + 2;
    }

    class LuxItemHolder extends RecyclerView.ViewHolder {
        @BindViews({R.id.btnPlaceLowStandardLeft, R.id.btnPlaceLowStandardRight})
        List<Button> buttonList;

        public LuxItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.btnPlaceLowStandardLeft, R.id.btnPlaceLowStandardRight})
        void onClickPlaceBtn(Button button) {
            toggleSelection(Integer.valueOf(button.getText().toString()), getAdapterPosition(), context.getString(R.string.filter_bottom));
        }

    }
}

