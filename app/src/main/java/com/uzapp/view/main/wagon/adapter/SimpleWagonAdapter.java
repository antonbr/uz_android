package com.uzapp.view.main.wagon.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.uzapp.R;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by viktoria on 2/3/17.
 */
/*
wagon with tarpan_head_c2 and footer
 */
public abstract class SimpleWagonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final int HEADER_VIEW_TYPE = 0, USUAL_VIEW_TYPE = 1, FOOTER_VIEW_TYPE = 2;
    protected Wagon wagon;
    protected List<Integer> availablePlaces;
    protected Context context;
    protected SparseBooleanArray selectedItems = new SparseBooleanArray();
    private OnPlaceSelectionListener listener;

    public interface OnPlaceSelectionListener {
        void onItemSelected(SparseBooleanArray selectedItems, Wagon wagon, int placeNumber, String placeType);
    }

    public SimpleWagonAdapter(Wagon wagon, List<Integer> availablePlaces, Context context, OnPlaceSelectionListener listener) {
        this.wagon = wagon;
        this.availablePlaces = availablePlaces;
        this.context = context;
        this.listener = listener;
    }

    public void initPlaceButton(Button placeBtn, int placeNumber) {
        placeBtn.setText(String.valueOf(placeNumber));
        placeBtn.setEnabled(availablePlaces.contains(placeNumber));
        placeBtn.setSelected(selectedItems.get(placeNumber));
    }

    public void toggleSelection(int placeNumber, int parentPosition, String placeType) {
        if (selectedItems.get(placeNumber, false)) {
            selectedItems.delete(placeNumber);
        } else {
            selectedItems.put(placeNumber, true);
        }
        notifyItemChanged(parentPosition);

        listener.onItemSelected(selectedItems, wagon, placeNumber, placeType);
    }

    void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    int getSelectedItemCount() {
        return selectedItems.size();
    }

    public SparseBooleanArray getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(SparseBooleanArray selectedItems) {
        if (selectedItems == null) return;
        this.selectedItems = selectedItems;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case HEADER_VIEW_TYPE:
            case FOOTER_VIEW_TYPE:
                View v1 = inflater.inflate(R.layout.item_image, parent, false);
                viewHolder = new ImageItemHolder(v1);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case HEADER_VIEW_TYPE:
                ImageItemHolder headerItemHolder = (ImageItemHolder) holder;
                bindImageHolder(headerItemHolder, R.drawable.ic_head_lux);
                break;
            case FOOTER_VIEW_TYPE:
                ImageItemHolder footerItemHolder = (ImageItemHolder) holder;
                bindImageHolder(footerItemHolder, R.drawable.ic_footer_lux);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW_TYPE;
        } else if (position == getItemCount() - 1) {
            return FOOTER_VIEW_TYPE;
        }
        return USUAL_VIEW_TYPE;
    }

    public void bindImageHolder(ImageItemHolder imageItemHolder, int drawableRes) {
        imageItemHolder.image.setImageDrawable(ContextCompat.getDrawable(context, drawableRes));
    }

    protected class ImageItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image) public ImageView image;

        public ImageItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public abstract class UsualItemHolder extends RecyclerView.ViewHolder {
        public UsualItemHolder(View itemView) {
            super(itemView);
        }

        public void passClickButton(Button button) {
            int placeNumber = Integer.valueOf(button.getText().toString());
            toggleSelection(placeNumber, getAdapterPosition(), null); //todo
        }
    }
}
