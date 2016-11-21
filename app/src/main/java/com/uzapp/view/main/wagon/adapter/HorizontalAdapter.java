package com.uzapp.view.main.wagon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.wagon.fragment.WagonPlaceFragment;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vladimir on 01.08.2016.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private Context context;
    private List<Wagon> listWagon;
//    private int oldPosition = 0;
    private int selectedPosition = 0;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtNumberWagon) TextView txtNumberWagon;
        @BindView(R.id.txtTotalPlaces) TextView txtTotalPlaces;
        @BindView(R.id.layoutCountWagons) LinearLayout layoutCountWagons;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.layoutCountWagons)
        void onClickLayoutCountWagons() {
            FragmentManager manager = ((MainActivity) context).getSupportFragmentManager();
            WagonPlaceFragment wagonPlaceFragment = (WagonPlaceFragment) manager.findFragmentById(R.id.fragmentContainer);
            wagonPlaceFragment.addWagonView(listWagon, getAdapterPosition());

            selectedPosition = getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    public HorizontalAdapter(Context context, List<Wagon> listWagon) {
        this.context = context;
        this.listWagon = listWagon;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_wagon, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        int total = listWagon.get(position).getPlacesPrices().getTotal();

        if (position == selectedPosition) {
            holder.layoutCountWagons.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_item_wagon_pressed));
            holder.txtTotalPlaces.setBackgroundColor(ContextCompat.getColor(context, R.color.accentColor));
        } else {
            holder.layoutCountWagons.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_item_wagon));
            holder.txtTotalPlaces.setBackgroundColor(ContextCompat.getColor(context, R.color.bgBorderColor));
        }

        holder.txtNumberWagon.setText(listWagon.get(position).getNumber());
        holder.txtTotalPlaces.setText(Integer.toString(total));
    }

    @Override
    public int getItemCount() {
        return listWagon.size();
    }
}