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
 * Created by viktoria on 2/2/17.
 */

public class WagonKupeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int PLACES_IN_KUPE = 4;
    private final int HEADER_VIEW_TYPE = 0, USUAL_VIEW_TYPE = 1, FOOTER_VIEW_TYPE = 2;
    private Wagon wagon;
    private List<Integer> availablePlaces;
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public WagonKupeAdapter(Wagon wagon, List<Integer> availablePlaces, Context context) {
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
                View v3 = inflater.inflate(R.layout.item_fragment_kupe, parent, false);
                viewHolder = new KupeItemHolder(v3);
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
                int placeLowStandardLeft = 1 + (position - 1) * PLACES_IN_KUPE;
                int placeUpperStandardLeft = 2 + (position - 1) * PLACES_IN_KUPE;
                int placeLowStandardRight = 3 + (position - 1) * PLACES_IN_KUPE;
                int placeUpperStandardRight = 4 + (position - 1) * PLACES_IN_KUPE;
                KupeItemHolder kupeItemHolder = (KupeItemHolder) holder;
                kupeItemHolder.btnPlaceLowStandardLeft.setText(Integer.toString(placeLowStandardLeft));
                kupeItemHolder.btnPlaceUpperStandardLeft.setText(Integer.toString(placeUpperStandardLeft));
                kupeItemHolder.btnPlaceLowStandardRight.setText(Integer.toString(placeLowStandardRight));
                kupeItemHolder.btnPlaceUpperStandardRight.setText(Integer.toString(placeUpperStandardRight));
                kupeItemHolder.btnPlaceLowStandardLeft.setEnabled(availablePlaces.contains(placeLowStandardLeft));
                kupeItemHolder.btnPlaceUpperStandardLeft.setEnabled(availablePlaces.contains(placeUpperStandardLeft));
                kupeItemHolder.btnPlaceLowStandardRight.setEnabled(availablePlaces.contains(placeLowStandardRight));
                kupeItemHolder.btnPlaceUpperStandardRight.setEnabled(availablePlaces.contains(placeUpperStandardRight));
                kupeItemHolder.btnPlaceLowStandardLeft.setSelected(selectedItems.get(placeLowStandardLeft));
                kupeItemHolder.btnPlaceUpperStandardLeft.setSelected(selectedItems.get(placeUpperStandardLeft));
                kupeItemHolder.btnPlaceLowStandardRight.setSelected(selectedItems.get(placeLowStandardRight));
                kupeItemHolder.btnPlaceUpperStandardRight.setSelected(selectedItems.get(placeUpperStandardRight));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return wagon.getPlacesCount() / PLACES_IN_KUPE + 2;
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

    class KupeItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnPlaceLowStandardLeft) Button btnPlaceLowStandardLeft;
        @BindView(R.id.btnPlaceUpperStandardLeft) Button btnPlaceUpperStandardLeft;
        @BindView(R.id.btnPlaceLowStandardRight) Button btnPlaceLowStandardRight;
        @BindView(R.id.btnPlaceUpperStandardRight) Button btnPlaceUpperStandardRight;

        public KupeItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.btnPlaceLowStandardLeft, R.id.btnPlaceUpperStandardLeft, R.id.btnPlaceLowStandardRight, R.id.btnPlaceUpperStandardRight})
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
