package com.uzapp.view.main.wagon.adapter.tarpan;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
 * Created by viktoria on 2/8/17.
 */

public class TarpanSecondClassAdapter extends TarpanFirstClassAdapter {

    public TarpanSecondClassAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
        totalPlacesCount = 94;
        usualRowPlacesCount = 5;
        tablePositionFirst = 7;
        tablePositionSecond = 19;
        cupboardPosition = 13;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == USUAL_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_tarpan_c2, parent, false);
            viewHolder = new TarpanSecondClassItemHolder(view);
        } else if (viewType == FIRST_ROW_TOILET_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_tarpan_c2_toilet_left, parent, false);
            viewHolder = new TarpanToiletLeftFirstClassItemHolder(view);
        } else if (viewType == LAST_ROW_TOILET_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_tarpan_c2_toilet_right, parent, false);
            viewHolder = new TarpanToiletRightFirstClassItemHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case USUAL_VIEW_TYPE:
                bindRows(((TarpanSecondClassItemHolder) holder).buttonsList, position);
                break;
            case CUPBOARD_VIEW_TYPE:
                ImageItemHolder cupboardItemHolder = (ImageItemHolder) holder;
                cupboardItemHolder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tarpan_c2_luggage));
                break;
            case TABLES_VIEW_TYPE:
                ImageItemHolder tableItemHolder = (ImageItemHolder) holder;
                tableItemHolder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tarpan_c2_tables));
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }


    class TarpanSecondClassItemHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace, R.id.fifthPlace})
        List<Button> buttonsList;

        public TarpanSecondClassItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace, R.id.fifthPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }

}
