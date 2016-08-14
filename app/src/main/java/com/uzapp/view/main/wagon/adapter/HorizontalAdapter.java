package com.uzapp.view.main.wagon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.pojo.prices.Prices;
import com.uzapp.pojo.prices.WagonsPrices;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.wagon.fragment.WagonPlaceFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vladimir on 01.08.2016.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private Prices pricesObject;
    private Context context;
    private List<WagonsPrices> listWagons;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtNumberWagon) TextView txtNumberWagon;
        @BindView(R.id.txtTotalPlaces) TextView txtTotalPlaces;
        @BindView(R.id.layoutCountWagons) LinearLayout layoutCountWagons;
        @BindView(R.id.progressBarPlaces) ProgressBar progressBarPlaces;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public HorizontalAdapter(Context context, Prices pricesObject) {
        this.context = context;
        this.pricesObject = pricesObject;
        listWagons = pricesObject.getTrain().getWagons();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_wagon, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        int progress = (listWagons.get(position).getPlacesPrices().getTotal() * 100 / 60);

        holder.txtNumberWagon.setText(listWagons.get(position).getNumber());
        holder.txtTotalPlaces.setText(Integer.toString(listWagons.get(position).getPlacesPrices().getTotal()));
        holder.progressBarPlaces.setProgress(progress);

        holder.layoutCountWagons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((MainActivity) context).getSupportFragmentManager();
                manager.popBackStack();

                WagonPlaceFragment fragment = (WagonPlaceFragment) manager.findFragmentById(R.id.fragmentContainer);
                ((MainActivity) context).replaceFragment(fragment.newInstance(pricesObject, position), true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listWagons.size();
    }
}