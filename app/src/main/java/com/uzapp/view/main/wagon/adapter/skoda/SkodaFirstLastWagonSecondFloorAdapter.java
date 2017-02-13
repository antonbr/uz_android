package com.uzapp.view.main.wagon.adapter.skoda;

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
 * Created by viktoria on 2/13/17.
 */

public class SkodaFirstLastWagonSecondFloorAdapter extends SimpleWagonAdapter {
    private static final int STAIRS_VIEW_TYPE = 3;
    private static final int TWO_PLACES_VIEW_TYPE = 4;
    private static final int TABLES_ROW_VIEW_TYPE = 5;
    private static final int TABLE_ROW_FIRST_POSITION = 2;
    private static final int TABLE_ROW_SECOND_POSITION = 9;
    private int totalPlacesCount = 23;
    private int usualPlacesCount = 3;
    private int twoPlacesCount = 2;


    public SkodaFirstLastWagonSecondFloorAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case STAIRS_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_image, parent, false);
                viewHolder = new ImageItemHolder(view);
                break;
            case USUAL_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_after_left_stairs, parent, false);
                viewHolder = new ThreePlacesViewHolder(view);
                break;
            case TWO_PLACES_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_two_places_passage, parent, false);
                viewHolder = new TwoPlacesViewHolder(view);
                break;
            case TABLES_ROW_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_image, parent, false);
                viewHolder = new ImageItemHolder(view);
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
                bindImageHolder((ImageItemHolder) holder, R.drawable.skoda_header_toilet);
                break;
            case FOOTER_VIEW_TYPE:
                bindImageHolder((ImageItemHolder) holder, R.drawable.skoda_foot);
                break;
            case STAIRS_VIEW_TYPE:
                bindImageHolder((ImageItemHolder) holder, R.drawable.skoda_stairs_right);
                break;
            case TWO_PLACES_VIEW_TYPE:
                bindTwoPlaces((TwoPlacesViewHolder) holder, position);
                break;
            case USUAL_VIEW_TYPE:
                bindUsualPlaces((ThreePlacesViewHolder) holder, position);
                break;
            case TABLES_ROW_VIEW_TYPE:
                bindImageHolder((ImageItemHolder) holder, R.drawable.tarpan_c2_tables);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    private void bindTwoPlaces(TwoPlacesViewHolder holder, int position) {
        int rowPosition = getRowPosition(position);
        int leftPlaceNumber = totalPlacesCount - rowPosition;
        initPlaceButton(holder.buttonsList.get(0), leftPlaceNumber);
        initPlaceButton(holder.buttonsList.get(1), leftPlaceNumber-1);
    }

    private void bindUsualPlaces(ThreePlacesViewHolder holder, int position) {
        int rowPosition = getRowPosition(position);
        bindRightPlaces(holder.buttonsList.subList(1, holder.buttonsList.size()), position);
        initPlaceButton(holder.buttonsList.get(0), totalPlacesCount - rowPosition);
    }

    private void bindRightPlaces(List<Button> buttonList, int position) {
        int rowPosition = getRowPosition(position);
        for (int i = 0; i < buttonList.size(); i++) {
            Button button = buttonList.get(i);
            int placeNumber = rowPosition * twoPlacesCount + buttonList.size() - i;
            initPlaceButton(button, placeNumber);
        }
    }

    private int getRowPosition(int position) {
        int rowPosition = position - 1;
        if (position > TABLE_ROW_FIRST_POSITION && position < TABLE_ROW_SECOND_POSITION) {
            return rowPosition - 1;
        } else if (position > TABLE_ROW_SECOND_POSITION) {
            return rowPosition - 2;
        } else return rowPosition;
    }

    @Override
    public int getItemCount() {
        return (totalPlacesCount - twoPlacesCount) / usualPlacesCount + 6;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == TABLE_ROW_FIRST_POSITION || position == TABLE_ROW_SECOND_POSITION) {
            return TABLES_ROW_VIEW_TYPE;
        } else if (position == getItemCount() - 2) {
            return STAIRS_VIEW_TYPE;
        } else if (position == getItemCount() - 3) {
            return TWO_PLACES_VIEW_TYPE;
        } else return super.getItemViewType(position);
    }

    class ThreePlacesViewHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace})
        List<Button> buttonsList;

        public ThreePlacesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }

    class TwoPlacesViewHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace})
        List<Button> buttonsList;

        public TwoPlacesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }
}
