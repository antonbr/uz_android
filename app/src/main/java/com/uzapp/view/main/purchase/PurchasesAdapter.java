package com.uzapp.view.main.purchase;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vladimir on 19.08.2016.
 */
public class PurchasesAdapter extends RecyclerView.Adapter<PurchasesAdapter.PurchaseHolder> {

    private SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_FORTMAT);
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DAY_MONTH_YEAR_FORMAT);

    private List<Ticket> ticketList;
    private Context context;
    private String stationFrom, stationTo, trainName;

    public PurchasesAdapter(Context context, List<Ticket> ticketList, String stationFrom,
                            String stationTo, String trainName) {
        this.context = context;
        this.ticketList = ticketList;
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.trainName = trainName;
    }

    @Override
    public PurchaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fragment_purchase, parent, false);
        return new PurchaseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PurchaseHolder holder, int position) {
        final Ticket ticket = ticketList.get(position);
        String price = String.valueOf(ticket.getTicketPrice())
                + " " + context.getString(R.string.ticket_currency);
        Date departureDate = new Date(TimeUnit.SECONDS.toMillis(ticket.getDepartureDate()));
        Date arrivalDate = new Date(TimeUnit.SECONDS.toMillis(ticket.getArrivalDate()));

        holder.txtWagonNumber.setText(ticket.getWagonNumber());
        holder.txtPlaceNumber.setText(ticket.getPlaceNumber());
        holder.txtPriceTicket.setText(price);
        holder.txtDispatchDate.setText(dateFormat.format(departureDate));
        holder.txtDispatchTime.setText(timeFormat.format(departureDate));
        holder.txtArrivalDate.setText(dateFormat.format(arrivalDate));
        holder.txtArrivalTime.setText(timeFormat.format(arrivalDate));

        holder.trainName.setText(trainName);
        holder.stationFrom.setText(stationFrom);
        holder.stationTo.setText(stationTo);

        holder.btnBuyTicket.setText("Купить, " + price);
        holder.btnReserveTicket.setText("Резерв, 17 грн");
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class PurchaseHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtWagonNumber) TextView txtWagonNumber;
        @BindView(R.id.txtPlaceNumber) TextView txtPlaceNumber;
        @BindView(R.id.txtPriceTicket) TextView txtPriceTicket;
        @BindView(R.id.txtDispatchDate) TextView txtDispatchDate;
        @BindView(R.id.txtDispatchTime) TextView txtDispatchTime;
        @BindView(R.id.txtArrivalDate) TextView txtArrivalDate;
        @BindView(R.id.txtArrivalTime) TextView txtArrivalTime;
        @BindView(R.id.btnFull) Button btnFull;
        @BindView(R.id.btnChild) Button btnChild;
        @BindView(R.id.btnStudent) Button btnStudent;
        @BindView(R.id.trainName) TextView trainName;
        @BindView(R.id.stationFrom) TextView stationFrom;
        @BindView(R.id.stationTo) TextView stationTo;
        @BindView(R.id.btnBuyTicket) Button btnBuyTicket;
        @BindView(R.id.btnReserveTicket) Button btnReserveTicket;
        @BindView(R.id.txtDeleteTicket) TextView txtDeleteTicket;
        @BindView(R.id.firstNameField) EditText firstNameField;
        @BindView(R.id.lastNameField) EditText lastNameField;

        public PurchaseHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.btnFull)
        void onClickFull() {
            setBackgroundFullChildAndStudentBtn(this, true, false, false);
        }

        @OnClick(R.id.btnChild)
        void onClickChild() {
            setBackgroundFullChildAndStudentBtn(this, false, true, false);
        }

        @OnClick(R.id.btnStudent)
        void onClickStudent() {
            setBackgroundFullChildAndStudentBtn(this, false, false, true);
        }

        @OnClick(R.id.btnBuyTicket)
        void onClickBuyTicket() {
            setBackgroundBuyAndReserveBtn(this, true, false);
        }

        @OnClick(R.id.btnReserveTicket)
        void onClickReserveTicket() {
            setBackgroundBuyAndReserveBtn(this, false, true);
        }

        @OnClick(R.id.txtDeleteTicket)
        void onClickDeleteTicket() {
            ticketList.remove(getAdapterPosition());
            FragmentManager manager = ((MainActivity) context).getSupportFragmentManager();
            PreparePurchaseFragment fragment = (PreparePurchaseFragment) manager.findFragmentById(R.id.fragmentContainer);
            fragment.initComponents();
            notifyDataSetChanged();
        }
    }

    /**
     * Change background Full, Child and Student button on click
     */
    private void setBackgroundFullChildAndStudentBtn(PurchaseHolder holder, boolean isFull, boolean isChild, boolean isStudent) {
        holder.btnFull.setBackground(isFull ? ContextCompat.getDrawable(context, R.drawable.button_pressed_left) :
                ContextCompat.getDrawable(context, R.drawable.button_enabled_left));
        holder.btnFull.setTextColor(isFull ? ContextCompat.getColor(context, android.R.color.white) :
                ContextCompat.getColor(context, R.color.accentColor));

        holder.btnChild.setBackground(isChild ? ContextCompat.getDrawable(context, R.drawable.button_pressed_center) :
                ContextCompat.getDrawable(context, R.drawable.button_enabled_center));
        holder.btnChild.setTextColor(isChild ? ContextCompat.getColor(context, android.R.color.white) :
                ContextCompat.getColor(context, R.color.accentColor));

        holder.btnStudent.setBackground(isStudent ? ContextCompat.getDrawable(context, R.drawable.button_pressed_right) :
                ContextCompat.getDrawable(context, R.drawable.button_enabled_right));
        holder.btnStudent.setTextColor(isStudent ? ContextCompat.getColor(context, android.R.color.white) :
                ContextCompat.getColor(context, R.color.accentColor));
    }

    /**
     * Change background Buy and Reserve button on click
     */
    private void setBackgroundBuyAndReserveBtn(PurchaseHolder holder, boolean isBuy, boolean isReserve) {
        holder.btnBuyTicket.setBackground(isBuy ? ContextCompat.getDrawable(context, R.drawable.button_pressed_left) :
                ContextCompat.getDrawable(context, R.drawable.button_enabled_left));
        holder.btnBuyTicket.setTextColor(isBuy ? ContextCompat.getColor(context, android.R.color.white) :
                ContextCompat.getColor(context, R.color.accentColor));

        holder.btnReserveTicket.setBackground(isReserve ? ContextCompat.getDrawable(context, R.drawable.button_pressed_right) :
                ContextCompat.getDrawable(context, R.drawable.button_enabled_right));
        holder.btnReserveTicket.setTextColor(isReserve ? ContextCompat.getColor(context, android.R.color.white) :
                ContextCompat.getColor(context, R.color.accentColor));
    }
}
