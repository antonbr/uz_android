package com.uzapp.view.main.wagon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uzapp.R;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by viktoria on 2/3/17.
 */

public class WagonLuxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int PLACES_IN_LUX = 2;
    private final int HEADER_VIEW_TYPE = 0, USUAL_VIEW_TYPE = 1, FOOTER_VIEW_TYPE = 2;
    private Wagon wagon;
    private List<Integer> availablePlaces;
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public WagonLuxAdapter(Wagon wagon, List<Integer> availablePlaces, Context context) {
        this.wagon = wagon;
        this.availablePlaces = availablePlaces;
        this.context = context;
    }

    public void toggleSelection(int placeNumber, int parentPosition) {
        if (selectedItems.get(placeNumber, false)) {
            selectedItems.delete(placeNumber);
        } else {
            selectedItems.put(placeNumber, true);
        }
        notifyItemChanged(parentPosition);
    }

    void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    int getSelectedItemCount() {
        return selectedItems.size();
    }

//    List<Integer> getSelectedItems() {
//        List<Integer> items =
//                new ArrayList<Integer>(selectedItems.size());
//        for (int i = 0; i < selectedItems.size(); i++) {
//            items.add(photoList.get(selectedItems.keyAt(i)));
//        }
//        return items;
//    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case HEADER_VIEW_TYPE:
                View v1 = inflater.inflate(R.layout.kupe_header, parent, false);
                viewHolder = new HeaderItemHolder(v1);
                break;
            case FOOTER_VIEW_TYPE:
                View v2 = inflater.inflate(R.layout.kupe_footer, parent, false);
                viewHolder = new FooterItemHolder(v2);
                break;
            default:
                View v3 = inflater.inflate(R.layout.item_fragment_lux, parent, false);
                viewHolder = new LuxItemHolder(v3);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case HEADER_VIEW_TYPE:
                break;
            case FOOTER_VIEW_TYPE:
                break;
            default:
                int placeLowStandardLeft = 1 + (position - 1) * PLACES_IN_LUX;
                int placeLowStandardRight = 2 + (position - 1) * PLACES_IN_LUX;
                LuxItemHolder luxItemHolder = (LuxItemHolder) holder;
                luxItemHolder.btnPlaceLowStandardLeft.setText(Integer.toString(placeLowStandardLeft));
                luxItemHolder.btnPlaceLowStandardRight.setText(Integer.toString(placeLowStandardRight));
                luxItemHolder.btnPlaceLowStandardLeft.setEnabled(availablePlaces.contains(placeLowStandardLeft));
                luxItemHolder.btnPlaceLowStandardRight.setEnabled(availablePlaces.contains(placeLowStandardRight));
                luxItemHolder.btnPlaceLowStandardLeft.setSelected(selectedItems.get(placeLowStandardLeft));
                luxItemHolder.btnPlaceLowStandardRight.setSelected(selectedItems.get(placeLowStandardRight));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return wagon.getPlacesCount() / PLACES_IN_LUX + 2;
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

    class LuxItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnPlaceLowStandardLeft) Button btnPlaceLowStandardLeft;
        @BindView(R.id.btnPlaceLowStandardRight) Button btnPlaceLowStandardRight;

        public LuxItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.btnPlaceLowStandardLeft, R.id.btnPlaceLowStandardRight})
        void onClickPlaceLowStandardLeftBtn(Button button) {
            toggleSelection(Integer.valueOf(button.getText().toString()), getAdapterPosition()); //todo
        }

    }

    private class HeaderItemHolder extends RecyclerView.ViewHolder {

        public HeaderItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class FooterItemHolder extends RecyclerView.ViewHolder {

        public FooterItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

