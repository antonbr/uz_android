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

public class TarpanSecondClassCafeAdapter extends SimpleWagonAdapter {
    private final int TABLES_VIEW_TYPE = 3;
    private final int FIRST_ROW_VIEW_TYPE = 4;
    private final int SMALL_ROW_VIEW_TYPE = 5;
    private int tableRowPosition = 5;
    private int totalPlacesCount = 45;
    private int usualRowPlacesCount = 5;
    private int firstRowPlacesCount = 3;
    private int smallRowPlacesCount = 4;
    private int smallRowsCount = 3;

    public TarpanSecondClassCafeAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case FIRST_ROW_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_tarpan_c2_first_row, parent, false);
                viewHolder = new TarpanFirstRowClassItemHolder(view);
                break;
            case TABLES_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_image, parent, false);
                viewHolder = new ImageItemHolder(view);
                break;
            case USUAL_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_tarpan_c2, parent, false);
                viewHolder = new TarpanSecondClassItemHolder(view);
                break;
            case SMALL_ROW_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_tarpan_c2_small, parent, false);
                viewHolder = new TarpanSmallItemHolder(view);
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
                bindImageHolder((ImageItemHolder) holder, R.drawable.tarpan_head_cafe);
                break;
            case FOOTER_VIEW_TYPE:
                bindImageHolder((ImageItemHolder) holder, R.drawable.tarpan_footer_cafe);
                break;
            case TABLES_VIEW_TYPE:
                bindImageHolder((ImageItemHolder) holder, R.drawable.tarpan_c2_tables);
                break;
            case USUAL_VIEW_TYPE:
                bindRow(((TarpanSecondClassItemHolder) holder).buttonsList, position);
                break;
            case FIRST_ROW_VIEW_TYPE:
                bindFirstRow((TarpanFirstRowClassItemHolder) holder);
                break;
            case SMALL_ROW_VIEW_TYPE:
                bindSmallRows((TarpanSmallItemHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }


    private void bindFirstRow(TarpanFirstRowClassItemHolder holder) {
        for (int i = 0; i < holder.buttonsList.size(); i++) {
            Button placeBtn = holder.buttonsList.get(i);
            int placeNumber = holder.buttonsList.size() - i;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    private void bindRow(List<Button> buttonList, int position) {
        int size = buttonList.size();
        for (int i = 0; i < buttonList.size(); i++) {
            Button placeBtn = buttonList.get(i);
            int iter = i == 0 ? 1 : i;
            iter = i == 1 ? 0 : iter;
            int placeNumber = size - iter + getRowPosition(position) * size + firstRowPlacesCount;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    private void bindSmallRows(TarpanSmallItemHolder itemHolder, int position) {
        int size = itemHolder.buttonsList.size();
        int placesBefore = (getItemCount() - 7) * usualRowPlacesCount+firstRowPlacesCount;
        for (int i = 0; i < itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i);
            int iter = i == 0 ? 1 : i;
            iter = i == 1 ? 0 : iter;
            int smallPosition = getSmallPosition(position);
            int placeNumber = size - iter + placesBefore + smallPosition * size;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    private int getRowPosition(int position) {
        int rowPosition = position - 2;
        if (position > tableRowPosition) {
            rowPosition = rowPosition - 1;
        }
        return rowPosition;
    }

    private int getSmallPosition(int position) {
        return position-getItemCount()+4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return FIRST_ROW_VIEW_TYPE;
        } else if (position == tableRowPosition) {
            return TABLES_VIEW_TYPE;
        } else if (position > getItemCount() - 5 && position < getItemCount() - 1) {
            return SMALL_ROW_VIEW_TYPE;
        } else return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return (totalPlacesCount - firstRowPlacesCount - smallRowPlacesCount * smallRowsCount) / usualRowPlacesCount + 4 + smallRowsCount;
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

    class TarpanFirstRowClassItemHolder extends UsualItemHolder {
        @BindViews({R.id.thirdPlace, R.id.fourthPlace, R.id.fifthPlace})
        List<Button> buttonsList;

        public TarpanFirstRowClassItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.thirdPlace, R.id.fourthPlace, R.id.fifthPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }

    class TarpanSmallItemHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace, R.id.fourthPlace, R.id.fifthPlace})
        List<Button> buttonsList;

        public TarpanSmallItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace, R.id.fourthPlace, R.id.fifthPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }
}
