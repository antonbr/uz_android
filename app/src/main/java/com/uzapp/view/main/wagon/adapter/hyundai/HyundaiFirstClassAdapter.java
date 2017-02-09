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
 * Created by viktoria on 2/6/17.
 */

public class HyundaiFirstClassAdapter extends SimpleWagonAdapter {
    private static final int PLACES_LEFT = 2;
    private static final int PLACES_RIGHT = 2;

    public HyundaiFirstClassAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == USUAL_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_huyndai_c1, parent, false);
            viewHolder = new HyundaiFirstClassItemHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == USUAL_VIEW_TYPE) {
            HyundaiFirstClassItemHolder itemHolder = (HyundaiFirstClassItemHolder) holder;
            int size = itemHolder.buttonsList.size();
            for (int i = 1; i <= itemHolder.buttonsList.size(); i++) {
                Button placeBtn = itemHolder.buttonsList.get(i-1);
                int placeNumber = i + (position-1) * size;
                initPlaceButton(placeBtn, placeNumber);
            }
        } else if (holder.getItemViewType() == HEADER_VIEW_TYPE) {
            ImageItemHolder headerItemHolder = (ImageItemHolder) holder;
            bindImageHolder(headerItemHolder, R.drawable.ic_head_hyundai_c1);
        } else if (holder.getItemViewType() == FOOTER_VIEW_TYPE) {
            ImageItemHolder footerItemHolder = (ImageItemHolder) holder;
            bindImageHolder(footerItemHolder, R.drawable.ic_footer_hyundai_toilet);
        } else{
            super.onBindViewHolder(holder, position);
        }
    }


    @Override
    public int getItemCount() {
        return wagon.getPlacesCount() / (PLACES_LEFT + PLACES_RIGHT) + 2;
    }

    class HyundaiFirstClassItemHolder extends UsualItemHolder {
        @BindViews({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace})
        List<Button> buttonsList;

        public HyundaiFirstClassItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.firstPlace, R.id.secondPlace, R.id.thirdPlace, R.id.fourthPlace})
        void onClickPlaceBtn(Button button) {
            passClickButton(button);
        }
    }
}
