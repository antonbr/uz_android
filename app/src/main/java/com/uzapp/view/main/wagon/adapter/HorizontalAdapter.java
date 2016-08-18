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
import com.uzapp.util.Constants;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.wagon.fragment.WagonPlaceFragment;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vladimir on 01.08.2016.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private Context context;
    private List<Wagon> listWagon;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        int total = listWagon.get(position).getPlacesPrices().getTotal();

        String typeWagon = listWagon.get(position).getTypeCode();
        int totalPlaces = getProgressPlaces(typeWagon);
        int progress = (total * 100 / totalPlaces);

        holder.txtNumberWagon.setText(listWagon.get(position).getNumber());
        holder.txtTotalPlaces.setText(Integer.toString(total));
        holder.progressBarPlaces.setProgress(progress);

        holder.layoutCountWagons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((MainActivity) context).getSupportFragmentManager();
                WagonPlaceFragment wagonPlaceFragment = (WagonPlaceFragment) manager.findFragmentById(R.id.fragmentContainer);
                wagonPlaceFragment.addWagonView(listWagon, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listWagon.size();
    }

    /**
     * @param typeWagon
     * @return places for type wagon
     */
    private int getProgressPlaces(String typeWagon) {
        if (typeWagon.equalsIgnoreCase(Constants.TYPE_LUX)) {
            return 20;
        } else if (typeWagon.equalsIgnoreCase(Constants.TYPE_KUPE)) {
            return 40;
        } else {
            return 60;
        }
    }
}