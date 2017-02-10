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

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by viktoria on 2/10/17.
 */

public class SkodaSecondWagonSecondFloorAdapter extends SimpleWagonAdapter {
    protected final int STAIRS_LEFT_VIEW_TYPE = 3;
    protected final int STAIRS_RIGHT_VIEW_TYPE = 4;
    protected final int THREE_PLACES_LEFT_VIEW_TYPE = 5;
    protected final int THREE_PLACES_RIGHT_VIEW_TYPE = 6;
    protected final int ONE_PLACE_LEFT_VIEW_TYPE = 7;
    protected final int ONE_PLACE_RIGHT_VIEW_TYPE = 8;
    private int onePlaceRowPlacesCount = 1;
    private int threePlaceRowPlacesCount = 3;
    private int onePlaceRowsCount = 4;
    private int threePlaceRowsCount = 6;
    private int placesCount = 62;
    private int usualRowPlacesCount = 4;

    public SkodaSecondWagonSecondFloorAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case STAIRS_LEFT_VIEW_TYPE:
            case STAIRS_RIGHT_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_image, parent, false);
                viewHolder = new ImageItemHolder(view);
                break;
            case USUAL_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_tarpan_c1, parent, false);
                viewHolder = new UsualRowItemHolder(view);
                break;
            case THREE_PLACES_LEFT_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_three_places_right, parent, false);
                viewHolder = new ThreePlacesRowItemHolder(view);
                break;
            case THREE_PLACES_RIGHT_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_three_places_left, parent, false);
                viewHolder = new ThreePlacesRowItemHolder(view);
                break;
            case ONE_PLACE_RIGHT_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_one_place_right, parent, false);
                viewHolder = new OnePlaceRowItemHolder(view);
                break;
            case ONE_PLACE_LEFT_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_one_place_left, parent, false);
                viewHolder = new OnePlaceRowItemHolder(view);
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
                bindImageHolder((ImageItemHolder) holder, R.drawable.skoda_head);
                break;
            case FOOTER_VIEW_TYPE:
                bindImageHolder((ImageItemHolder) holder, R.drawable.skoda_foot);
                break;
            case STAIRS_LEFT_VIEW_TYPE:
                ImageItemHolder stairsLeftHolder = (ImageItemHolder) holder;
                bindImageHolder(stairsLeftHolder, R.drawable.skoda_stairs_left);
                break;
            case STAIRS_RIGHT_VIEW_TYPE:
                ImageItemHolder stairsRightHolder = (ImageItemHolder) holder;
                bindImageHolder(stairsRightHolder, R.drawable.skoda_stairs_right);
                break;
            case USUAL_VIEW_TYPE:
                bindUsualPlacesRow((UsualRowItemHolder) holder, position);
                break;
            case ONE_PLACE_LEFT_VIEW_TYPE:
            case ONE_PLACE_RIGHT_VIEW_TYPE:
                bindOnePlaceRow((OnePlaceRowItemHolder) holder, position);
                break;
            case THREE_PLACES_LEFT_VIEW_TYPE:
            case THREE_PLACES_RIGHT_VIEW_TYPE:
                bindThreePlacesRow((ThreePlacesRowItemHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    private void bindUsualPlacesRow(UsualRowItemHolder itemHolder, int position) {
        int size = itemHolder.buttonsList.size();
        for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i - 1);
            int placeNumber = size - i + getPlacesBefore(position);
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    private void bindThreePlacesRow(ThreePlacesRowItemHolder itemHolder, int position) {
        if (position - 2 < 6) {
            int size = itemHolder.buttonsList.size();
            for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
                Button placeBtn = itemHolder.buttonsList.get(i - 1);
                int placeNumber = size - i;
                if (i < 2) {
                    placeNumber += placeNumber + 5;
                }
                initPlaceButton(placeBtn, placeNumber);
            }
        }
    }

    private void bindOnePlaceRow(OnePlaceRowItemHolder itemHolder, int position) {
        if (position - 2 < 6) {
            initPlaceButton(itemHolder.firstPlaceBtn, position - 1);
        } else {

        }
    }

    int getPlacesBefore(int position) {
        int lastPlace = 0;
        lastPlace = (position - 2) * usualRowPlacesCount + 1;
        return lastPlace;
    }

    @Override
    public int getItemCount() {
        return (placesCount - onePlaceRowsCount * onePlaceRowPlacesCount - threePlaceRowPlacesCount * threePlaceRowsCount) / usualRowPlacesCount
                + 4 + onePlaceRowsCount + threePlaceRowsCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return STAIRS_LEFT_VIEW_TYPE;
        } else if (position == getItemCount() - 2) {
            return STAIRS_RIGHT_VIEW_TYPE;
        } else if (position == 2 || position == 4 || position == 5) {
            return THREE_PLACES_LEFT_VIEW_TYPE;
        } else if (position == 3 || position == 6) {
            return ONE_PLACE_RIGHT_VIEW_TYPE;
        } else if (position == getItemCount() - 3 || position == getItemCount() - 5 || position == getItemCount() - 6) {
            return THREE_PLACES_RIGHT_VIEW_TYPE;
        } else if (position == getItemCount() - 4 || position == getItemCount() - 7) {
            return ONE_PLACE_LEFT_VIEW_TYPE;
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

    class ThreePlacesRowItemHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace})
        List<Button> buttonsList;

        public ThreePlacesRowItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }

    class OnePlaceRowItemHolder extends UsualItemHolder {
        @BindView(R.id.firstPlace)
        Button firstPlaceBtn;

        public OnePlaceRowItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.firstPlace)
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }
}
