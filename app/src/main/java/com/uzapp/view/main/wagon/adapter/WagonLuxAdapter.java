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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by viktoria on 2/3/17.
 */

public class WagonLuxAdapter extends LyingWagonBaseAdapter {
    private final int PLACES_IN_LUX = 2;

    public WagonLuxAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
       super(wagon,availablePlaces, context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == USUAL_VIEW_TYPE) {
            View view =inflater.inflate(R.layout.item_fragment_lux, parent, false);
            viewHolder = new LuxItemHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == USUAL_VIEW_TYPE) {
            int placeLowStandardLeft = 1 + (position - 1) * PLACES_IN_LUX;
            int placeLowStandardRight = 2 + (position - 1) * PLACES_IN_LUX;
            LuxItemHolder luxItemHolder = (LuxItemHolder) holder;
            luxItemHolder.btnPlaceLowStandardLeft.setText(Integer.toString(placeLowStandardLeft));
            luxItemHolder.btnPlaceLowStandardRight.setText(Integer.toString(placeLowStandardRight));
            luxItemHolder.btnPlaceLowStandardLeft.setEnabled(availablePlaces.contains(placeLowStandardLeft));
            luxItemHolder.btnPlaceLowStandardRight.setEnabled(availablePlaces.contains(placeLowStandardRight));
            luxItemHolder.btnPlaceLowStandardLeft.setSelected(selectedItems.get(placeLowStandardLeft));
            luxItemHolder.btnPlaceLowStandardRight.setSelected(selectedItems.get(placeLowStandardRight));
        } else{
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return wagon.getPlacesCount() / PLACES_IN_LUX + 2;
    }

    class LuxItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnPlaceLowStandardLeft) Button btnPlaceLowStandardLeft;
        @BindView(R.id.btnPlaceLowStandardRight) Button btnPlaceLowStandardRight;

        public LuxItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.btnPlaceLowStandardLeft, R.id.btnPlaceLowStandardRight})
        void onClickPlaceLowStandardLeftBtn(Button button) {
            toggleSelection(Integer.valueOf(button.getText().toString()), getAdapterPosition(), context.getString(R.string.filter_bottom)); //todo
        }

    }
}

