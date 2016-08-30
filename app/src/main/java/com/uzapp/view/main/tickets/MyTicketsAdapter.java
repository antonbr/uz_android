package com.uzapp.view.main.tickets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vika on 29.08.16.
 */
public class MyTicketsAdapter extends RecyclerView.Adapter<MyTicketsAdapter.TicketHolder> {
    private SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_FORTMAT);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EE, d MMMM");
    private DecimalFormat df = new DecimalFormat("#.00");
    private List<ShortTicket> ticketList = new ArrayList<>();
    private Context context;

    public MyTicketsAdapter(Context context) {
        this.context = context;
    }

    public void setTickets(List<ShortTicket> ticketList) {
        this.ticketList.clear();
        this.ticketList.addAll(ticketList);
        notifyDataSetChanged();
    }

    @Override
    public TicketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item, parent, false);
        return new TicketHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TicketHolder holder, final int position) {
        ShortTicket ticket = ticketList.get(position);
        holder.ticketInformationLayout.setVisibility(holder.isShowingTicketInfo ? View.VISIBLE : View.GONE);
        holder.ticketNumberLayout.setVisibility(holder.isShowingTicketInfo ? View.GONE : View.VISIBLE);
        holder.itemView.setScaleX(holder.isShowingTicketInfo ? 1 : -1);
        if (holder.isShowingTicketInfo) {
            holder.electronicTicketBtn.setVisibility(ticket.electronic ? View.VISIBLE : View.GONE);
            holder.exchangeTicketBtn.setVisibility(ticket.electronic ? View.GONE : View.VISIBLE);
            holder.trainName.setText(ticket.train);
            holder.stationFrom.setText(ticket.stationFromName);
            holder.stationTo.setText(ticket.stationToName);
            Date departureDate = new Date(TimeUnit.SECONDS.toMillis(ticket.departureDate));
            Date arrivalDate = new Date(TimeUnit.SECONDS.toMillis(ticket.arrivalDate));
            holder.arrivalDate.setText(dateFormat.format(arrivalDate));
            holder.arrivalTime.setText(timeFormat.format(arrivalDate));
            holder.departureDate.setText(dateFormat.format(departureDate));
            holder.departureTime.setText(timeFormat.format(departureDate));
            holder.wagonNumber.setText(String.valueOf(ticket.wagon));
            holder.placeNumber.setText(String.valueOf(ticket.place));
            StringBuilder wagonType = new StringBuilder(context.getString(ticket.wagonType.getLongNameStringRes()));
            if (ticket.wagonType.ordinal() == 4) {
                wagonType.append(" ").append(context.getString(ticket.wagonClass.getLongNameStringRes()));
            }
            holder.wagonType.setText(wagonType);
            String placeType = "";
            if (ticket.wagonType.ordinal() == 0) {
                placeType = context.getString(R.string.filter_bottom);
            } else if (ticket.wagonType.ordinal() == 2 || ticket.wagonType.ordinal() == 3) {
                placeType = (CommonUtils.isOdd((int) ticket.place))
                        ? context.getString(R.string.filter_bottom) : context.getString(R.string.filter_top);
            }
            holder.placeType.setText(placeType);
            StringBuilder fullName = new StringBuilder(ticket.firstname);
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(ticket.lastname);
            holder.fullName.setText(fullName);
            holder.price.setText(context.getString(R.string.ticket_cost, df.format(ticket.cost)));
            holder.ticketType.setText(context.getString(ticket.kind.getStringRes()));
            holder.electronicTicketBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rotateTicket(holder, position);
                }
            });
            holder.exchangeTicketBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rotateTicket(holder, position);
                }
            });
        } else {
            holder.electronicTicketNumberLayout.setVisibility(ticket.electronic ? View.VISIBLE : View.GONE);
            holder.analogTicketNumberLayout.setVisibility(ticket.electronic ? View.GONE : View.VISIBLE);
            if (ticket.electronic) {
                byte[] decodedString = Base64.decode(ticket.qrImage, Base64.DEFAULT);
                final Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.qrCodeImage.setImageBitmap(qrCodeBitmap);
                //TODO recycle?
                holder.electronicTicketNumber.setText(ticket.uid);
            } else {
                byte[] decodedString = Base64.decode(ticket.barcodeImage, Base64.DEFAULT);
                final Bitmap barcodeBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.barCodeImage.setImageBitmap(barcodeBitmap);
                //TODO recycle?
                holder.analogTicketNumber.setText(ticket.uid);//TODO is correct data?

            }
            holder.ticketInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rotateTicket(holder, position);
                }
            });
        }
    }

    private void rotateTicket(TicketHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = holder.itemView.getWidth();
        layoutParams.height = holder.itemView.getHeight();
        holder.itemView.setLayoutParams(layoutParams);
        holder.isShowingTicketInfo = !holder.isShowingTicketInfo;
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class TicketHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.electronicTicketBtn) TextView electronicTicketBtn;
        @BindView(R.id.exchangeTicketBtn) TextView exchangeTicketBtn;
        @BindView(R.id.trainName) TextView trainName;
        @BindView(R.id.stationFrom) TextView stationFrom;
        @BindView(R.id.stationTo) TextView stationTo;
        @BindView(R.id.departureTime) TextView departureTime;
        @BindView(R.id.arrivalTime) TextView arrivalTime;
        @BindView(R.id.departureDate) TextView departureDate;
        @BindView(R.id.arrivalDate) TextView arrivalDate;
        @BindView(R.id.wagonNumber) TextView wagonNumber;
        @BindView(R.id.placeNumber) TextView placeNumber;
        @BindView(R.id.wagonType) TextView wagonType;
        @BindView(R.id.placeType) TextView placeType;
        @BindView(R.id.fullName) TextView fullName;
        @BindView(R.id.price) TextView price;
        @BindView(R.id.ticketType) TextView ticketType;
        @BindView(R.id.ticketReturnBtn) Button ticketReturnBtn;

        @BindView(R.id.ticketInformationLayout) ViewGroup ticketInformationLayout;
        @BindView(R.id.ticketNumberLayout) ViewGroup ticketNumberLayout;
        @BindView(R.id.electronicTicketNumberLayout) ViewGroup electronicTicketNumberLayout;
        @BindView(R.id.analogTicketNumberLayout) ViewGroup analogTicketNumberLayout;
        @BindView(R.id.ticketInfoBtn) TextView ticketInfoBtn;
        @BindView(R.id.qrCodeImage) ImageView qrCodeImage;
        @BindView(R.id.electronicTicketNumber) TextView electronicTicketNumber;
        @BindView(R.id.barCodeImage) ImageView barCodeImage;
        @BindView(R.id.analogTicketNumber) TextView analogTicketNumber;
        @BindView(R.id.printBtn) Button printBtn;

        boolean isShowingTicketInfo = true;

        public TicketHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
