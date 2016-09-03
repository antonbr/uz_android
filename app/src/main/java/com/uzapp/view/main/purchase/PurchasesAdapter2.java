package com.uzapp.view.main.purchase;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.util.Constants;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.purchase.fragment.PreparePurchaseFragment;
import com.uzapp.view.main.wagon.model.Ticket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vladimir on 19.08.2016.
 */
public class PurchasesAdapter2 extends BaseAdapter {

    private SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_FORTMAT);
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DAY_MONTH_YEAR_FORMAT);

    private List<Ticket> ticketList;
    private Context context;
    public LayoutInflater layoutInflater;
    private String stationFrom, stationTo, trainName;

    public PurchasesAdapter2(Context context, List<Ticket> ticketList, String stationFrom,
                             String stationTo, String trainName) {
        this.context = context;
        this.ticketList = ticketList;
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.trainName = trainName;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ticketList.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_fragment_purchase, parent, false);
        }

        final Ticket ticket = ticketList.get(position);
        String price = String.valueOf(ticket.getTicketPrice())
                + " " + context.getString(R.string.ticket_currency);
        Date departureDate = new Date(TimeUnit.SECONDS.toMillis(ticket.getDepartureDate()));
        Date arrivalDate = new Date(TimeUnit.SECONDS.toMillis(ticket.getArrivalDate()));

        TextView txtWagonNumber = (TextView) view.findViewById(R.id.txtWagonNumber);
        TextView txtPlaceNumber = (TextView) view.findViewById(R.id.txtPlaceNumber);
        TextView txtPriceTicket = (TextView) view.findViewById(R.id.txtPriceTicket);
        TextView txtDispatchDate = (TextView) view.findViewById(R.id.txtDispatchDate);
        TextView txtDispatchTime = (TextView) view.findViewById(R.id.txtDispatchTime);
        TextView txtArrivalDate = (TextView) view.findViewById(R.id.txtArrivalDate);
        TextView txtArrivalTime = (TextView) view.findViewById(R.id.txtArrivalTime);
        TextView trainNameTextView = (TextView) view.findViewById(R.id.trainName);
        TextView stationFromTextView = (TextView) view.findViewById(R.id.stationFrom);
        TextView stationToTextView = (TextView) view.findViewById(R.id.stationTo);
        TextView txtDeleteTicket = (TextView) view.findViewById(R.id.txtDeleteTicket);
        final Button btnBuyTicket = (Button) view.findViewById(R.id.btnBuyTicket);
        final Button btnReserveTicket = (Button) view.findViewById(R.id.btnReserveTicket);
        final Button btnFull = (Button) view.findViewById(R.id.btnFull);
        final Button btnChild = (Button) view.findViewById(R.id.btnChild);
        final Button btnStudent = (Button) view.findViewById(R.id.btnStudent);

        btnFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackgroundFullChildAndStudentBtn(btnFull, btnChild, btnStudent, true, false, false);
            }
        });

        btnChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackgroundFullChildAndStudentBtn(btnFull, btnChild, btnStudent, false, true, false);
            }
        });

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackgroundFullChildAndStudentBtn(btnFull, btnChild, btnStudent, false, false, true);
            }
        });

        btnBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackgroundBuyAndReserveBtn(btnBuyTicket, btnReserveTicket, true, false);
            }
        });

        btnReserveTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackgroundBuyAndReserveBtn(btnBuyTicket, btnReserveTicket, false, true);
            }
        });

        txtDeleteTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketList.remove(position);
                FragmentManager manager = ((MainActivity) context).getSupportFragmentManager();
                PreparePurchaseFragment fragment = (PreparePurchaseFragment) manager.findFragmentById(R.id.fragmentContainer);
                fragment.initComponents();
                notifyDataSetChanged();
            }
        });

        txtWagonNumber.setText(ticket.getWagonNumber());
        txtPlaceNumber.setText(ticket.getPlaceNumber());
        txtPriceTicket.setText(price);
        txtDispatchDate.setText(dateFormat.format(departureDate));
        txtDispatchTime.setText(timeFormat.format(departureDate));
        txtArrivalDate.setText(dateFormat.format(arrivalDate));
        txtArrivalTime.setText(timeFormat.format(arrivalDate));

        trainNameTextView.setText(trainName);
        stationFromTextView.setText(stationFrom);
        stationToTextView.setText(stationTo);

        btnBuyTicket.setText("Купить, " + price);
        btnReserveTicket.setText("Резерв, 17 грн");

        return view;
    }

    /**
     * Change background Full, Child and Student button on click
     */
    private void setBackgroundFullChildAndStudentBtn(Button btnFull, Button btnChild, Button btnStudent,
                                                     boolean isFull, boolean isChild, boolean isStudent) {
        btnFull.setBackground(isFull ? ContextCompat.getDrawable(context, R.drawable.button_pressed_left) :
                ContextCompat.getDrawable(context, R.drawable.button_enabled_left));
        btnFull.setTextColor(isFull ? ContextCompat.getColor(context, android.R.color.white) :
                ContextCompat.getColor(context, R.color.accentColor));

        btnChild.setBackground(isChild ? ContextCompat.getDrawable(context, R.drawable.button_pressed_center) :
                ContextCompat.getDrawable(context, R.drawable.button_enabled_center));
        btnChild.setTextColor(isChild ? ContextCompat.getColor(context, android.R.color.white) :
                ContextCompat.getColor(context, R.color.accentColor));

        btnStudent.setBackground(isStudent ? ContextCompat.getDrawable(context, R.drawable.button_pressed_right) :
                ContextCompat.getDrawable(context, R.drawable.button_enabled_right));
        btnStudent.setTextColor(isStudent ? ContextCompat.getColor(context, android.R.color.white) :
                ContextCompat.getColor(context, R.color.accentColor));
    }

    /**
     * Change background Buy and Reserve button on click
     */
    private void setBackgroundBuyAndReserveBtn(Button btnBuyTicket, Button btnReserveTicket,
                                               boolean isBuy, boolean isReserve) {
        btnBuyTicket.setBackground(isBuy ? ContextCompat.getDrawable(context, R.drawable.button_pressed_left) :
                ContextCompat.getDrawable(context, R.drawable.button_enabled_left));
        btnBuyTicket.setTextColor(isBuy ? ContextCompat.getColor(context, android.R.color.white) :
                ContextCompat.getColor(context, R.color.accentColor));

        btnReserveTicket.setBackground(isReserve ? ContextCompat.getDrawable(context, R.drawable.button_pressed_right) :
                ContextCompat.getDrawable(context, R.drawable.button_enabled_right));
        btnReserveTicket.setTextColor(isReserve ? ContextCompat.getColor(context, android.R.color.white) :
                ContextCompat.getColor(context, R.color.accentColor));
    }
}
