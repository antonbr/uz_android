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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by viktoria on 2/2/17.
 */

public class WagonKupeAdapter extends SimpleWagonAdapter {
    private final int PLACES_IN_KUPE = 4;

    public WagonKupeAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == USUAL_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_kupe, parent, false);
            viewHolder = new KupeItemHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == USUAL_VIEW_TYPE) {
            int placeLowStandardLeft = 1 + (position - 1) * PLACES_IN_KUPE;
            int placeUpperStandardLeft = 2 + (position - 1) * PLACES_IN_KUPE;
            int placeLowStandardRight = 3 + (position - 1) * PLACES_IN_KUPE;
            int placeUpperStandardRight = 4 + (position - 1) * PLACES_IN_KUPE;
            KupeItemHolder kupeItemHolder = (KupeItemHolder) holder;
            kupeItemHolder.btnPlaceLowStandardLeft.setText(Integer.toString(placeLowStandardLeft));
            kupeItemHolder.btnPlaceUpperStandardLeft.setText(Integer.toString(placeUpperStandardLeft));
            kupeItemHolder.btnPlaceLowStandardRight.setText(Integer.toString(placeLowStandardRight));
            kupeItemHolder.btnPlaceUpperStandardRight.setText(Integer.toString(placeUpperStandardRight));
            kupeItemHolder.btnPlaceLowStandardLeft.setEnabled(availablePlaces.contains(placeLowStandardLeft));
            kupeItemHolder.btnPlaceUpperStandardLeft.setEnabled(availablePlaces.contains(placeUpperStandardLeft));
            kupeItemHolder.btnPlaceLowStandardRight.setEnabled(availablePlaces.contains(placeLowStandardRight));
            kupeItemHolder.btnPlaceUpperStandardRight.setEnabled(availablePlaces.contains(placeUpperStandardRight));
            kupeItemHolder.btnPlaceLowStandardLeft.setSelected(selectedItems.get(placeLowStandardLeft));
            kupeItemHolder.btnPlaceUpperStandardLeft.setSelected(selectedItems.get(placeUpperStandardLeft));
            kupeItemHolder.btnPlaceLowStandardRight.setSelected(selectedItems.get(placeLowStandardRight));
            kupeItemHolder.btnPlaceUpperStandardRight.setSelected(selectedItems.get(placeUpperStandardRight));
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return wagon.getPlacesCount() / PLACES_IN_KUPE + 2;
    }

    class KupeItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnPlaceLowStandardLeft) Button btnPlaceLowStandardLeft;
        @BindView(R.id.btnPlaceUpperStandardLeft) Button btnPlaceUpperStandardLeft;
        @BindView(R.id.btnPlaceLowStandardRight) Button btnPlaceLowStandardRight;
        @BindView(R.id.btnPlaceUpperStandardRight) Button btnPlaceUpperStandardRight;

        public KupeItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.btnPlaceLowStandardLeft, R.id.btnPlaceUpperStandardLeft, R.id.btnPlaceLowStandardRight, R.id.btnPlaceUpperStandardRight})
        void onClickPlaceLowStandardLeftBtn(Button button) {
            int placeNumber = Integer.valueOf(button.getText().toString());
            String placeType = (CommonUtils.isOdd(placeNumber))
                    ? context.getString(R.string.filter_bottom) : context.getString(R.string.filter_top);
            toggleSelection(placeNumber, getAdapterPosition(), placeType); //todo
        }
    }
}
