package com.uzapp.view.main.wagon.adapter.tarpan;

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
 * Created by viktoria on 2/9/17.
 */

public class TarpanSecondClassFirstLastWagonAdapter extends SimpleWagonAdapter {
    private final int TABLES_VIEW_TYPE = 3;
    private int tableRowPosition = 3;
    private int totalPlacesCount = 20;
    private int usualRowPlacesCount = 4;

    public TarpanSecondClassFirstLastWagonAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TABLES_VIEW_TYPE:
                View v1 = inflater.inflate(R.layout.item_image, parent, false);
                viewHolder = new ImageItemHolder(v1);
                break;
            case USUAL_VIEW_TYPE:
                View view = inflater.inflate(R.layout.item_tarpan_c1, parent, false);
                viewHolder = new TarpanSecondClassItemHolder(view);
                break;
            default:
                viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case HEADER_VIEW_TYPE:
                bindImageHolder((ImageItemHolder) holder, R.drawable.tarpan_head_c2);
                break;
            case FOOTER_VIEW_TYPE:
                bindImageHolder((ImageItemHolder) holder, R.drawable.footer_tarpan_c1_02);
                break;
            case TABLES_VIEW_TYPE:
                bindImageHolder((ImageItemHolder) holder, R.drawable.tarpan_tables);
                break;
            case USUAL_VIEW_TYPE:
                bindRow(((TarpanSecondClassItemHolder) holder).buttonsList, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    private void bindRow(List<Button> buttonList, int position) {
        int size = buttonList.size();
        for (int i = 0; i < buttonList.size(); i++) {
            Button placeBtn = buttonList.get(i);
            int iter = i == 0 ? 1 : i;
            iter = i == 1 ? 0 : iter;
            int placeNumber = size - iter + getRowPosition(position) * size;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    private int getRowPosition(int position) {
        int rowPosition = position - 1;
        if (position > tableRowPosition) {
            rowPosition = rowPosition - 1;
        }
        return rowPosition;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == tableRowPosition) {
            return TABLES_VIEW_TYPE;
        } else return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return totalPlacesCount / usualRowPlacesCount + 3;
    }

    class TarpanSecondClassItemHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace})
        List<Button> buttonsList;

        public TarpanSecondClassItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }
}
