package com.uzapp.view.main.wagon.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.wagon.model.Ticket;

import java.util.List;

/**
 * Created by Vladimir on 02.08.2016.
 */
public class TicketAdapter extends ArrayAdapter<Ticket> {

    private final Context context;
    private final List<Ticket> list;
    private RemoveTicketListener listener;

    public interface RemoveTicketListener {
        void onTicketRemove(int placeNumber, String wagonNumber);
    }

    public TicketAdapter(Context context, List<Ticket> list, RemoveTicketListener listener) {
        super(context, -1, list);
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
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
        if(!TextUtils.isEmpty(ticketItem.getPlaceType())) {
            txtUpperLowerShelf.setText(", " + ticketItem.getPlaceType());
        }else{
            txtUpperLowerShelf.setText("");
        }
        txtPriceTicket.setText(Double.toString(ticketItem.getTicketPrice()) + " "
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

    /**
     * @param position Remove item from ticket list
     */
    private void removeItem(int position) {
        removeItemBtn(position);
        list.remove(position);
        notifyDataSetChanged();
        if (list.isEmpty()) {
            hideBuyReserveView();
        }
    }

    /**
     * @param position Remove selected place in wagon
     */
    private void removeItemBtn(int position) {
        Ticket ticketItem = getTicketItem(position);
        listener.onTicketRemove(Integer.parseInt(ticketItem.getPlaceNumber()), ticketItem.getWagonNumber());
    }

    /**
     * Hide view with buy and reserve select tickets
     */
    public void hideBuyReserveView() {
        LinearLayout layoutBuyReserveTicket = (LinearLayout) ((MainActivity) context).findViewById(R.id.layoutBuyReserveTicket);
        if (layoutBuyReserveTicket != null) {
            layoutBuyReserveTicket.setVisibility(View.GONE);
        }
    }
}

