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

public class SkodaFirstLastWagonFirstFloorAdapter extends SimpleWagonAdapter {
    private static final int STAIRS_VIEW_TYPE = 3;
    private static final int INVALID_SECTION_VIEW_TYPE = 4;
    private static final int TWO_PLACES_SECTION_VIEW_TYPE = 5;
    private static final int THREE_PLACES_SECTION_VIEW_TYPE = 6;
    private int threePlacesRowsCount = 1;
    private int threePlacesCount = 3;
    private int twoPlacesRowsCount = 2;
    private int twoPlacesCount = 2;
    private int firstPlaceNumber = 24;
    private int lastPlaceNumber = 42;
    private int usualPlacesCount = 4;

    public SkodaFirstLastWagonFirstFloorAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
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
                view = inflater.inflate(R.layout.item_tarpan_c1, parent, false);
                viewHolder = new UsualRowItemHolder(view);
                break;
            case INVALID_SECTION_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_invalid_section, parent, false);
                viewHolder = new InvalidViewHolder(view);
                break;
            case TWO_PLACES_SECTION_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_two_places, parent, false);
                viewHolder = new TwoPlacesViewHolder(view);
                break;
            case THREE_PLACES_SECTION_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_before_right_stairs, parent, false);
                viewHolder = new ThreePlacesViewHolder(view);
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
            case TWO_PLACES_SECTION_VIEW_TYPE:
                bindTwoPlacesRows((TwoPlacesViewHolder) holder, position);
                break;
            case USUAL_VIEW_TYPE:
                bindUsualPlaces((UsualRowItemHolder) holder, position);
                break;
            case THREE_PLACES_SECTION_VIEW_TYPE:
                bindThreePlacesRows((ThreePlacesViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    private void bindUsualPlaces(UsualRowItemHolder holder, int position) {
        bindLeftTwoPlaces(holder.buttonsList.subList(0, 2), position);
        bindRightTwoPlaces(holder.buttonsList, position);
    }

    private void bindTwoPlacesRows(TwoPlacesViewHolder holder, int position) {
        int rowPosition = getRowPosition(position);
        for (int i = 1; i <= holder.buttonsList.size(); i++) {
            Button button = holder.buttonsList.get(i - 1);
            int placeNumber = firstPlaceNumber + twoPlacesCount - i + rowPosition * twoPlacesCount;
            initPlaceButton(button, placeNumber);
        }
    }

    private void bindThreePlacesRows(ThreePlacesViewHolder holder, int position) {
        bindLeftTwoPlaces(holder.buttonsList.subList(0, 2), position);
        bindRightTwoPlaces(holder.buttonsList, position);
    }

    private void bindLeftTwoPlaces(List<Button> buttonList, int position) {
        int rowPosition = getRowPosition(position);
        for (int i = 1; i <= buttonList.size(); i++) {
            Button button = buttonList.get(i - 1);
            int placeNumber = firstPlaceNumber + twoPlacesRowsCount * twoPlacesCount + rowPosition * twoPlacesCount + twoPlacesCount - i;
            initPlaceButton(button, placeNumber);
        }
    }

    private void bindRightTwoPlaces(List<Button> buttonList, int position) {
        int rowPosition = getRowPosition(position);
        for (int i = 2; i < buttonList.size(); i++) {
            Button button = buttonList.get(i);
            int placeNumber = lastPlaceNumber - rowPosition * twoPlacesCount + twoPlacesCount - i;
            initPlaceButton(button, placeNumber);
        }
    }

    private int getRowPosition(int position) {
        if (position <= 3) {
            return position - 2;
        } else return position - 4;
    }

    @Override
    public int getItemCount() {
        return (lastPlaceNumber - firstPlaceNumber - threePlacesRowsCount * threePlacesCount - twoPlacesCount * twoPlacesRowsCount + 1) / usualPlacesCount
                + threePlacesRowsCount + twoPlacesRowsCount + 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return INVALID_SECTION_VIEW_TYPE;
        } else if (position == 2 || position == 3) {
            return TWO_PLACES_SECTION_VIEW_TYPE;
        } else if (position == getItemCount() - 3) {
            return THREE_PLACES_SECTION_VIEW_TYPE;
        } else if (position == getItemCount() - 2) {
            return STAIRS_VIEW_TYPE;
        } else return super.getItemViewType(position);
    }

    class UsualRowItemHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace})
        List<Button> buttonsList;

        public UsualRowItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }

    class InvalidViewHolder extends RecyclerView.ViewHolder {

        public InvalidViewHolder(View itemView) {
            super(itemView);
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
}
