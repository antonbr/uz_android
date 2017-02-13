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
 * Created by viktoria on 2/10/17.
 */

public class SkodaSecondWagonSecondFloorAdapter extends SimpleWagonAdapter {
    protected final int STAIRS_LEFT_VIEW_TYPE = 3;
    protected final int STAIRS_RIGHT_VIEW_TYPE = 4;
    protected final int HEADER_PLACES_VIEW_TYPE = 5;
    protected final int FOOTER_PLACES_VIEW_TYPE = 6;
    private int placesCount = 62;
    private int usualRowPlacesCount = 4;
    private int headerFooterRowPlacesCount = 11;

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
            case FOOTER_PLACES_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_second_floor_footer_places, parent, false);
                viewHolder = new HeaderFooterPlacesRowItemHolder(view);
                break;
            case HEADER_PLACES_VIEW_TYPE:
                view = inflater.inflate(R.layout.item_skoda_second_floor_header_places, parent, false);
                viewHolder = new HeaderFooterPlacesRowItemHolder(view);
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
            case FOOTER_PLACES_VIEW_TYPE:
            case HEADER_PLACES_VIEW_TYPE:
                bindHeaderFooter((HeaderFooterPlacesRowItemHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    private void bindUsualPlacesRow(UsualRowItemHolder itemHolder, int position) {
        int size = itemHolder.buttonsList.size();
        for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i - 1);
            int placeNumber = size - i + getPlacesBefore(position)+1;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    private void bindHeaderFooter(HeaderFooterPlacesRowItemHolder itemHolder, int position) {
        for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i - 1);
            int placeNumber = getPlacesBefore(position) + i;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    private int getPlacesBefore(int position) {
        if (position < 3) {
            return 0;
        } else {
            return headerFooterRowPlacesCount + (position - 3) * usualRowPlacesCount;
        }
    }

    @Override
    public int getItemCount() {
        return (placesCount - headerFooterRowPlacesCount * 2) / usualRowPlacesCount
                + 4 + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return STAIRS_LEFT_VIEW_TYPE;
        } else if (position == getItemCount() - 2) {
            return STAIRS_RIGHT_VIEW_TYPE;
        } else if (position == 2) {
            return HEADER_PLACES_VIEW_TYPE;
        } else if (position == getItemCount() - 3) {
            return FOOTER_PLACES_VIEW_TYPE;
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

    class HeaderFooterPlacesRowItemHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace, R.id.fifthPlace, R.id.sixthPlace,
                R.id.seventhPlace, R.id.eighthPlace, R.id.ninethPlace, R.id.tenthPlace, R.id.eleventhPlace})
        List<Button> buttonsList;

        public HeaderFooterPlacesRowItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace, R.id.fifthPlace, R.id.sixthPlace,
                R.id.seventhPlace, R.id.eighthPlace, R.id.ninethPlace, R.id.tenthPlace, R.id.eleventhPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }
}
