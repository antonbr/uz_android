package com.uzapp.view.main.wagon.adapter.hyundai;

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
 * Created by viktoria on 2/7/17.
 */

public class HyundaiSecondClassAdapter extends SimpleWagonAdapter {
    protected static final int LUGGAGE_VIEW_TYPE = 3;
    protected static final int LAST_SECTION_VIEW_TYPE = 4;
    protected static final int PLACES_LEFT = 2;
    protected static final int PLACES_RIGHT = 3;
    protected static final int LUGGAGE_SECTION_PLACES = 2;
    protected static final int LAST_SECTION_PLACES_COUNT = 4;
    protected static int LAST_SECTION_NOT_VISIBLE_ID = R.id.thirdPlace;
    protected int totalPlacesCount = 81;

    public HyundaiSecondClassAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, SimpleWagonAdapter.OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == USUAL_VIEW_TYPE || viewType == LAST_SECTION_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_huyndai_c2, parent, false);
            viewHolder = new HyundaiSecondClassItemHolder(view);
        } else if (viewType == LUGGAGE_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_huyndai_c2_luggage, parent, false);
            viewHolder = new HyundaiLuggageSecondClassItemHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == USUAL_VIEW_TYPE) {
            bindUsualPlaces((HyundaiSecondClassItemHolder) holder, position);
        } else if (holder.getItemViewType() == LAST_SECTION_VIEW_TYPE) {
            bindLastSection((HyundaiSecondClassItemHolder) holder, position);
        } else if (holder.getItemViewType() == LUGGAGE_VIEW_TYPE) {
            bindLuggagePlaces((HyundaiLuggageSecondClassItemHolder) holder, position);
        } else if (holder.getItemViewType() == HEADER_VIEW_TYPE) {
            ImageItemHolder headerItemHolder = (ImageItemHolder) holder;
            bindImageHolder(headerItemHolder,R.drawable.ic_head_hyundai_c1 );
        } else if (holder.getItemViewType() == FOOTER_VIEW_TYPE) {
            ImageItemHolder footerItemHolder = (ImageItemHolder) holder;
            bindImageHolder(footerItemHolder,R.drawable.ic_footer_hyundai_toilet );
        } else{
            super.onBindViewHolder(holder, position);
        }
    }

    protected void bindUsualPlaces(HyundaiSecondClassItemHolder itemHolder, int position) {
        int size = itemHolder.buttonsList.size();
        for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i - 1);
            int placeNumber = i + (position - 2) * size + LUGGAGE_SECTION_PLACES;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    protected void bindLastSection(HyundaiSecondClassItemHolder itemHolder, int position) {
        int size = itemHolder.buttonsList.size();
        int lastPlaceNumber = (position - 2) * size + LUGGAGE_SECTION_PLACES;
        for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i - 1);
            if (placeBtn.getId() == LAST_SECTION_NOT_VISIBLE_ID) {
                placeBtn.setVisibility(View.INVISIBLE);
                continue;
            }
            int placeNumber = ++lastPlaceNumber;
            initPlaceButton(placeBtn, placeNumber);
        }
    }

    protected void bindLuggagePlaces(HyundaiLuggageSecondClassItemHolder itemHolder, int position) {
        for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
            Button placeBtn = itemHolder.buttonsList.get(i - 1);
            initPlaceButton(placeBtn, i);
        }
    }

    @Override
    public int getItemCount() {
        return (totalPlacesCount - LUGGAGE_SECTION_PLACES - LAST_SECTION_PLACES_COUNT) / (PLACES_LEFT + PLACES_RIGHT) + 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return LUGGAGE_VIEW_TYPE;
        } else if (position == getItemCount() - 2) {
            return LAST_SECTION_VIEW_TYPE;
        } else {
            return super.getItemViewType(position);
        }
    }

    class HyundaiSecondClassItemHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace, R.id.fifthPlace})
        List<Button> buttonsList;

        public HyundaiSecondClassItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace, R.id.fifthPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }

    class HyundaiLuggageSecondClassItemHolder extends UsualItemHolder {
        @BindViews({R.id.fourthPlace, R.id.fifthPlace})
        List<Button> buttonsList;

        public HyundaiLuggageSecondClassItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.fourthPlace, R.id.fifthPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }
}
