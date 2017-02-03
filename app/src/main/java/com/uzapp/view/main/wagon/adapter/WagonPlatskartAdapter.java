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

public class WagonPlatskartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int PLACES_IN_PLATSKART_SECTION_MAIN = 4;
    private final int PLACES_IN_PLATSKART_SECTION_SIDE = 2;
    private final int HEADER_VIEW_TYPE = 0, USUAL_VIEW_TYPE = 1, FOOTER_VIEW_TYPE = 2;
    private Wagon wagon;
    private List<Integer> availablePlaces;
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public WagonPlatskartAdapter(Wagon wagon, List<Integer> availablePlaces, Context context) {
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
                View v3 = inflater.inflate(R.layout.item_fragment_platskart, parent, false);
                viewHolder = new PlatskartItemHolder(v3);
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
                int placeLowStandardLeft = 1 + (position - 1) * PLACES_IN_PLATSKART_SECTION_MAIN;
                int placeUpperStandardLeft = 2 + (position - 1) * PLACES_IN_PLATSKART_SECTION_MAIN;
                int placeLowStandardRight = 3 + (position - 1) * PLACES_IN_PLATSKART_SECTION_MAIN;
                int placeUpperStandardRight = 4 + (position - 1) * PLACES_IN_PLATSKART_SECTION_MAIN;
                int placeLowSide = wagon.getPlacesCount() - 1 - (position - 1) * PLACES_IN_PLATSKART_SECTION_SIDE;
                int placeUpperSide = wagon.getPlacesCount() - (position - 1) * PLACES_IN_PLATSKART_SECTION_SIDE;

                PlatskartItemHolder platskartItemHolder = (PlatskartItemHolder) holder;
                platskartItemHolder.btnPlaceLowStandardLeft.setText(Integer.toString(placeLowStandardLeft));
                platskartItemHolder.btnPlaceUpperStandardLeft.setText(Integer.toString(placeUpperStandardLeft));
                platskartItemHolder.btnPlaceLowStandardRight.setText(Integer.toString(placeLowStandardRight));
                platskartItemHolder.btnPlaceUpperStandardRight.setText(Integer.toString(placeUpperStandardRight));
                platskartItemHolder.btnPlaceLowSide.setText(Integer.toString(placeLowSide));
                platskartItemHolder.btnPlaceUpperSide.setText(Integer.toString(placeUpperSide));

                platskartItemHolder.btnPlaceLowStandardLeft.setEnabled(availablePlaces.contains(placeLowStandardLeft));
                platskartItemHolder.btnPlaceUpperStandardLeft.setEnabled(availablePlaces.contains(placeUpperStandardLeft));
                platskartItemHolder.btnPlaceLowStandardRight.setEnabled(availablePlaces.contains(placeLowStandardRight));
                platskartItemHolder.btnPlaceUpperStandardRight.setEnabled(availablePlaces.contains(placeUpperStandardRight));
                platskartItemHolder.btnPlaceLowSide.setEnabled(availablePlaces.contains(placeLowSide));
                platskartItemHolder.btnPlaceUpperSide.setEnabled(availablePlaces.contains(placeUpperSide));

                platskartItemHolder.btnPlaceLowStandardLeft.setSelected(selectedItems.get(placeLowStandardLeft));
                platskartItemHolder.btnPlaceUpperStandardLeft.setSelected(selectedItems.get(placeUpperStandardLeft));
                platskartItemHolder.btnPlaceLowStandardRight.setSelected(selectedItems.get(placeLowStandardRight));
                platskartItemHolder.btnPlaceUpperStandardRight.setSelected(selectedItems.get(placeUpperStandardRight));
                platskartItemHolder.btnPlaceLowSide.setSelected(selectedItems.get(placeLowSide));
                platskartItemHolder.btnPlaceUpperSide.setSelected(selectedItems.get(placeUpperSide));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return wagon.getPlacesCount() / (PLACES_IN_PLATSKART_SECTION_MAIN + PLACES_IN_PLATSKART_SECTION_SIDE) + 2;
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

    class PlatskartItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnPlaceLowStandardLeft) Button btnPlaceLowStandardLeft;
        @BindView(R.id.btnPlaceUpperStandardLeft) Button btnPlaceUpperStandardLeft;
        @BindView(R.id.btnPlaceLowStandardRight) Button btnPlaceLowStandardRight;
        @BindView(R.id.btnPlaceUpperStandardRight) Button btnPlaceUpperStandardRight;
        @BindView(R.id.btnPlaceLowSide) Button btnPlaceLowSide;
        @BindView(R.id.btnPlaceUpperSide) Button btnPlaceUpperSide;

        public PlatskartItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.btnPlaceLowStandardLeft, R.id.btnPlaceUpperStandardLeft, R.id.btnPlaceLowStandardRight, R.id.btnPlaceUpperStandardRight,
                R.id.btnPlaceLowSide, R.id.btnPlaceUpperSide})
        void onClickPlaceBtn(Button button) {
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
