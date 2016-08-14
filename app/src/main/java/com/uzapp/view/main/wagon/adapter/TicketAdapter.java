package com.uzapp.view.main.wagon.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.wagon.fragment.WagonPlaceFragment;
import com.uzapp.view.main.wagon.model.Ticket;

import java.util.List;

/**
 * Created by Vladimir on 02.08.2016.
 */
public class TicketAdapter extends ArrayAdapter<Ticket> {

    private final Context context;
    private final List<Ticket> list;

    public TicketAdapter(Context context, List<Ticket> list) {
        super(context, -1, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_fragment_buy_reserve, parent, false);
        }
        Ticket ticketItem = getTicketItem(position);

        TextView txtWagonNumber = (TextView) view.findViewById(R.id.txtWagonNumber);
        TextView txtPlaceNumber = (TextView) view.findViewById(R.id.txtPlaceNumber);
        TextView txtUpperLowerShelf = (TextView) view.findViewById(R.id.txtUpperLowerShelf);
        TextView txtPriceTicket = (TextView) view.findViewById(R.id.txtPriceTicket);
        ImageView imageRemoveTicket = (ImageView) view.findViewById(R.id.imgBtnRemoveTicket);

        txtWagonNumber.setText(ticketItem.getWagonNumber());
        txtPlaceNumber.setText(ticketItem.getPlaceNumber());
        txtUpperLowerShelf.setText(", " + ticketItem.getPlaceType());
        txtPriceTicket.setText(Integer.toString(ticketItem.getTicketPrice()) + " "
                + context.getString(R.string.ticket_currency));

        imageRemoveTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

        return view;
    }

    private Ticket getTicketItem(int position) {
        return getItem(position);
    }

    private void removeItem(int position) {

        removeItemBtn(position);

        list.remove(position);
        notifyDataSetChanged();
        if (list.isEmpty()) {
            hideBuyReserveView();
        }
    }

    private void removeItemBtn(int position) {
        Ticket ticketItem = getTicketItem(position);
        FragmentManager manager = ((MainActivity) context).getSupportFragmentManager();
        WagonPlaceFragment fragment = (WagonPlaceFragment) manager.findFragmentById(R.id.fragmentContainer);
        fragment.ticketPlaceRemoveView(Integer.parseInt(ticketItem.getPlaceNumber()), ticketItem.getWagonNumber());
    }

    public void hideBuyReserveView() {
        LinearLayout layoutBuyReserveTicket = (LinearLayout) ((MainActivity) context).findViewById(R.id.layoutBuyReserveTicket);
        if (layoutBuyReserveTicket != null) {
            layoutBuyReserveTicket.setVisibility(View.GONE);
        }
    }
}

