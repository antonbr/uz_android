package com.uzapp.view.main.purchase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.view.main.wagon.model.Ticket;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vladimir on 19.08.2016.
 */
public class PurchasesAdapter extends RecyclerView.Adapter<PurchasesAdapter.PurchaseHolder>{

    private List<Wagon> wagonList;
    private List<Ticket> ticketList;
    private Context context;

    public PurchasesAdapter(Context context, List<Ticket> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
    }

    @Override
    public PurchaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fragment_purchase, parent, false);
        return new PurchaseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PurchaseHolder holder, int position) {
        final Ticket ticket = ticketList.get(position);
        holder.txtWagonNumber.setText(ticket.getWagonNumber());
        holder.txtPlaceNumber.setText(ticket.getPlaceNumber());
//        holder.txtPriceTicket.setText(ticket.getTicketPrice());
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class PurchaseHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtWagonNumber) TextView txtWagonNumber;
        @BindView(R.id.txtPlaceNumber) TextView txtPlaceNumber;
        @BindView(R.id.txtPriceTicket) TextView txtPriceTicket;

        public PurchaseHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
