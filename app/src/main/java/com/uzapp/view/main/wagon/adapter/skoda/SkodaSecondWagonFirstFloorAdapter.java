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

public class SkodaSecondWagonFirstFloorAdapter extends SimpleWagonAdapter {
    protected final int STAIRS_LEFT_VIEW_TYPE = 3;
    protected final int STAIRS_RIGHT_VIEW_TYPE = 4;
    protected final int AFTER_STAIRS_LEFT_VIEW_TYPE = 5;
    protected final int BEFORE_STAIRS_RIGHT_VIEW_TYPE = 6;
    protected int beforeAfterStairsRowPlacesCount = 3;
    protected int stairsRowPlacesCount = 1;
    protected int usualRowPlacesCount = 4;
    protected int stairsLeftPosition = 4;
    protected int stairsRightPosition = 17;
    protected int lastPlaceNumber = 134;
    protected int firstPlaceNumber = 63;

    public SkodaSecondWagonFirstFloorAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case USUAL_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_tarpan_c1, parent, false);
                viewHolder = new UsualRowItemHolder(view);
                break;
            case STAIRS_LEFT_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_stairs_left, parent, false);
                viewHolder = new StairsRowItemHolder(view);
                break;
            case STAIRS_RIGHT_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_stairs_right, parent, false);
                viewHolder = new StairsRowItemHolder(view);
                break;
            case AFTER_STAIRS_LEFT_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_after_left_stairs, parent, false);
                viewHolder = new BeforeAfterStairsRowItemHolder(view);
                break;
            case BEFORE_STAIRS_RIGHT_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_before_right_stairs, parent, false);
                viewHolder = new BeforeAfterStairsRowItemHolder(view);
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
                bindImageHolder((ImageItemHolder) holder, R.drawable.skoda_footer_toilet);
                break;
            case STAIRS_LEFT_VIEW_TYPE:
                bindLeftStairPlace((StairsRowItemHolder) holder, position);
                break;
            case STAIRS_RIGHT_VIEW_TYPE:
                bindRightStairPlace((StairsRowItemHolder) holder, position);
                break;
            case AFTER_STAIRS_LEFT_VIEW_TYPE:
                bindAfterLeftStairsPlace((BeforeAfterStairsRowItemHolder) holder, position);
                break;
            case BEFORE_STAIRS_RIGHT_VIEW_TYPE:
                bindBeforeRightStairsPlace((BeforeAfterStairsRowItemHolder) holder, position);
                break;
            case USUAL_VIEW_TYPE:
                bindUsualPlaces((UsualRowItemHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    private void bindUsualPlaces(UsualRowItemHolder itemHolder, int position) {
        int size = itemHolder.buttonsList.size();
        for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i - 1);
            int placeNumber = size - i + getPlacesBefore(position);
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    int getPlacesBefore(int position) {
        int lastPlace = 0;
        if (position < stairsLeftPosition) {
            lastPlace = firstPlaceNumber + (position - 1) * usualRowPlacesCount;
        } else if (position > stairsLeftPosition + 1 && position < stairsRightPosition) {
            lastPlace = firstPlaceNumber + (position - 3) * usualRowPlacesCount + stairsRowPlacesCount + beforeAfterStairsRowPlacesCount;
        } else if (position > stairsRightPosition) {
            lastPlace = firstPlaceNumber + (position - 5) * usualRowPlacesCount + stairsRowPlacesCount * 2 + beforeAfterStairsRowPlacesCount * 2;
        }
        return lastPlace;
    }

    void bindLeftStairPlace(StairsRowItemHolder itemHolder, int position) {
        int lastPlace = firstPlaceNumber + (position - 1) * usualRowPlacesCount;
        initPlaceButton(itemHolder.firstPlaceBtn, lastPlace);
    }

    void bindRightStairPlace(StairsRowItemHolder itemHolder, int position) {
        int lastPlace = getPlacesBefore(position - 1);
        initPlaceButton(itemHolder.firstPlaceBtn, lastPlace + beforeAfterStairsRowPlacesCount);
    }

    void bindAfterLeftStairsPlace(BeforeAfterStairsRowItemHolder itemHolder, int position) {
        int lastPlace = firstPlaceNumber + (position - 2) * usualRowPlacesCount + 1;
        int size = itemHolder.buttonsList.size();
        for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i - 1);
            int placeNumber = lastPlace + size - i;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    void bindBeforeRightStairsPlace(BeforeAfterStairsRowItemHolder itemHolder, int position) {
        int lastPlace = getPlacesBefore(position);
        int size = itemHolder.buttonsList.size();
        for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i - 1);
            int placeNumber = lastPlace + size - i;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    @Override
    public int getItemCount() {
        return (lastPlaceNumber - firstPlaceNumber + 1 - stairsRowPlacesCount * 2 - beforeAfterStairsRowPlacesCount * 2) / usualRowPlacesCount + 6;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == stairsLeftPosition) {
            return STAIRS_LEFT_VIEW_TYPE;
        } else if (position == stairsRightPosition) {
            return STAIRS_RIGHT_VIEW_TYPE;
        } else if (position == stairsLeftPosition + 1) {
            return AFTER_STAIRS_LEFT_VIEW_TYPE;
        } else if (position == stairsRightPosition - 1) {
            return BEFORE_STAIRS_RIGHT_VIEW_TYPE;
        }
        return super.getItemViewType(position);
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

    class StairsRowItemHolder extends UsualItemHolder {
        @BindView(R.id.firstPlace)
        Button firstPlaceBtn;

        public StairsRowItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.firstPlace)
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }

    class BeforeAfterStairsRowItemHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace})
        List<Button> buttonsList;

        public BeforeAfterStairsRowItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }

}
