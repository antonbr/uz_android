package com.uzapp.view.main.wagon.adapter.tarpan;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
 * Created by viktoria on 2/8/17.
 */

public class TarpanFirstClassAdapter extends SimpleWagonAdapter {
    protected static final int CUPBOARD_VIEW_TYPE = 3;
    protected static final int TABLES_VIEW_TYPE = 4;
    protected static final int FIRST_ROW_TOILET_VIEW_TYPE = 5;
    protected static final int LAST_ROW_TOILET_VIEW_TYPE = 6;
    protected int toiletRowPlacesCount = 2;
    protected int usualRowPlacesCount = 4;
    protected int totalPlacesCount = 64;
    protected int tablePositionFirst = 6;
    protected int tablePositionSecond = 16;
    protected int cupboardPosition = 11;

    public TarpanFirstClassAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == CUPBOARD_VIEW_TYPE || viewType == TABLES_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_image, parent, false);
            viewHolder = new ImageItemHolder(view);
        } else if (viewType == USUAL_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_tarpan_c1, parent, false);
            viewHolder = new TarpanFirstClassItemHolder(view);
        } else if (viewType == FIRST_ROW_TOILET_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_tarpan_c1_toilet_left, parent, false);
            viewHolder = new TarpanToiletLeftFirstClassItemHolder(view);
        } else if (viewType == LAST_ROW_TOILET_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_tarpan_c1_toilet_right, parent, false);
            viewHolder = new TarpanToiletRightFirstClassItemHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case CUPBOARD_VIEW_TYPE:
                ImageItemHolder cupboardItemHolder = (ImageItemHolder) holder;
                cupboardItemHolder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tarpan_cupboard_luggage));
                break;
            case TABLES_VIEW_TYPE:
                ImageItemHolder tableItemHolder = (ImageItemHolder) holder;
                tableItemHolder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tarpan_tables));
                break;
            case HEADER_VIEW_TYPE:
                bindImageHolder((ImageItemHolder) holder, R.drawable.head_tarpan_c1_02);
                break;
            case FOOTER_VIEW_TYPE:
                bindImageHolder((ImageItemHolder) holder, R.drawable.footer_tarpan_c1_02);
                break;
            case FIRST_ROW_TOILET_VIEW_TYPE:
                bindFirstRowToilet((TarpanToiletLeftFirstClassItemHolder) holder, position);
                break;
            case LAST_ROW_TOILET_VIEW_TYPE:
                bindLastRowToilet((TarpanToiletRightFirstClassItemHolder) holder, position);
                break;
            case USUAL_VIEW_TYPE:
                bindRows(((TarpanFirstClassItemHolder) holder).buttonsList, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    void bindFirstRowToilet(TarpanToiletLeftFirstClassItemHolder itemHolder, int position) {
        int size = itemHolder.buttonsList.size();
        for (int i = 0; i < itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i);
            initPlaceButton(placeBtn, size - i);
        }
    }

    void bindRows(List<Button> buttonList, int position) {
        int size = buttonList.size();
        for (int i = 0; i < buttonList.size(); i++) {
            Button placeBtn = buttonList.get(i);
            int iter = i == 0 ? 1 : i;
            iter = i == 1 ? 0 : iter;
            int placeNumber = size - iter + getRowPosition(position) * size + toiletRowPlacesCount;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    int getRowPosition(int position) {
        int rowPosition = position - 2;
        if (position > tablePositionSecond) {
            rowPosition = rowPosition - 3;
        } else if (position > cupboardPosition) {
            rowPosition = rowPosition - 2;
        } else if (position > tablePositionFirst) {
            rowPosition--;
        }
        return rowPosition;
    }

    void bindLastRowToilet(TarpanToiletRightFirstClassItemHolder itemHolder, int position) {
        for (int i = 0; i < itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i);
            int placeNumber = i + 1 + getRowPosition(position) * usualRowPlacesCount + toiletRowPlacesCount;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    @Override
    public int getItemCount() {
        return (totalPlacesCount - toiletRowPlacesCount * 2) / usualRowPlacesCount + 7;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return FIRST_ROW_TOILET_VIEW_TYPE;
        } else if (position == getItemCount() - 2) {
            return LAST_ROW_TOILET_VIEW_TYPE;
        } else if (position == cupboardPosition) {
            return CUPBOARD_VIEW_TYPE;
        } else if (position == tablePositionFirst || position == tablePositionSecond) {
            return TABLES_VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    class TarpanFirstClassItemHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace})
        List<Button> buttonsList;

        public TarpanFirstClassItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }

    class TarpanToiletLeftFirstClassItemHolder extends UsualItemHolder {
        @BindViews({R.id.thirdPlace, R.id.fourthPlace})
        List<Button> buttonsList;

        public TarpanToiletLeftFirstClassItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.thirdPlace, R.id.fourthPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }

    class TarpanToiletRightFirstClassItemHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace})
        List<Button> buttonsList;

        public TarpanToiletRightFirstClassItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }
}
