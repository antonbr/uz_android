package com.uzapp.view.main.wagon.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.uzapp.R;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

/**
 * Created by viktoria on 2/7/17.
 */

public class HyundaiSecondClassKafeAdapter extends HyundaiSecondClassAdapter {

    public HyundaiSecondClassKafeAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, SimpleWagonAdapter.OnPlaceSelectionListener listener) {
        super(wagon, availablePlaces, context, listener);
        LAST_SECTION_NOT_VISIBLE_ID = R.id.secondPlace;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == FOOTER_VIEW_TYPE) {
            SimpleWagonAdapter.FooterItemHolder footerItemHolder = (SimpleWagonAdapter.FooterItemHolder) holder;
            footerItemHolder.footer.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_footer_hyunai_cafe));
        } else {
            super.onBindViewHolder(holder, position);
        }
    }


    @Override
    public int getItemCount() {
        return (51 - LUGGAGE_SECTION_PLACES - LAST_SECTION_PLACES_COUNT) / (PLACES_LEFT + PLACES_RIGHT) + 4;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
