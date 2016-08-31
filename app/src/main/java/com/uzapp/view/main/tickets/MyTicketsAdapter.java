package com.uzapp.view.main.tickets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
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
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EE, d MMM");
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

    public void addTickets(List<ShortTicket> ticketList) {
        this.ticketList.addAll(ticketList);
        notifyDataSetChanged();
    }

    public void clearTickets() {
        this.ticketList.clear();
        notifyDataSetChanged();
    }

    @Override
    public TicketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item, parent, false);
        itemView.setCameraDistance(itemView.getCameraDistance() * 5);
        return new TicketHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TicketHolder holder, int position) {
        ShortTicket ticket = ticketList.get(position);
        if (holder.isShowingTicketInfo) {
            holder.bindFrontSide(ticket, position);
        } else {
            holder.bindBackSide(ticket, position);
        }
    }

    @Override
    public void onBindViewHolder(TicketHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.size() == 0) {
            ShortTicket ticket = ticketList.get(position);
            if (holder.isShowingTicketInfo) {
                holder.bindFrontSide(ticket, position);
            } else {
                holder.bindBackSide(ticket, position);
            }
        }
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
        @BindView(R.id.filterDate) TextView departureDate;
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

        public void bindFrontSide(final ShortTicket ticket, final int position) {
            Log.d("TAG", "bind front side " + position);

            ticketInformationLayout.setVisibility(isShowingTicketInfo ? View.VISIBLE : View.GONE);
            ticketNumberLayout.setVisibility(isShowingTicketInfo ? View.GONE : View.VISIBLE);
            itemView.setScaleX(isShowingTicketInfo ? 1 : -1);
            //TODO refactor
            electronicTicketBtn.setVisibility(ticket.electronic ? View.VISIBLE : View.GONE);
            exchangeTicketBtn.setVisibility(ticket.electronic ? View.GONE : View.VISIBLE);
            trainName.setText(ticket.train);
            stationFrom.setText(ticket.stationFromName);
            stationTo.setText(ticket.stationToName);
            Date departureDateObj = new Date(TimeUnit.SECONDS.toMillis(ticket.departureDate));
            Date arrivalDateObj = new Date(TimeUnit.SECONDS.toMillis(ticket.arrivalDate));
            String formattedArrivalDate = dateFormat.format(arrivalDateObj);
            String formattedDepartureDate = dateFormat.format(departureDateObj);
            if (!arrivalDate.getText().equals(formattedArrivalDate)) {
                arrivalDate.setText(formattedArrivalDate);
            }
            if (!departureDate.getText().equals(formattedDepartureDate)) {
                departureDate.setText(formattedDepartureDate);
            }
            arrivalTime.setText(timeFormat.format(arrivalDateObj));
            departureTime.setText(timeFormat.format(departureDateObj));
            String wagon = String.valueOf(ticket.wagon);
            String place = String.valueOf(ticket.place);
            if (!wagonNumber.getText().equals(wagon)) {
                wagonNumber.setText(wagon);
            }
            if (!placeNumber.getText().equals(place)) {
                placeNumber.setText(place);
            }
            StringBuilder wagonTypeBuilder = new StringBuilder(context.getString(ticket.wagonType.getLongNameStringRes()));
            if (ticket.wagonType.ordinal() == 4) {
                wagonTypeBuilder.append(" ").append(context.getString(ticket.wagonClass.getLongNameStringRes()));
            }
            if (!wagonType.getText().equals(wagonTypeBuilder)) {
                wagonType.setText(wagonTypeBuilder);
            }
            String placeTypeString = "";
            if (ticket.wagonType.ordinal() == 0) {
                placeTypeString = context.getString(R.string.filter_bottom);
            } else if (ticket.wagonType.ordinal() == 2 || ticket.wagonType.ordinal() == 3) {
                placeTypeString = (CommonUtils.isOdd((int) ticket.place))
                        ? context.getString(R.string.filter_bottom) : context.getString(R.string.filter_top);
            }
            if (!placeType.getText().equals(placeTypeString)) {
                placeType.setText(placeTypeString);
            }
            StringBuilder fullNameString = new StringBuilder(ticket.firstname);
            if (fullNameString.length() > 0) {
                fullNameString.append(" ");
            }
            fullName.append(ticket.lastname);
            fullName.setText(fullNameString);
            price.setText(context.getString(R.string.ticket_cost, df.format(ticket.cost)));
            ticketType.setText(context.getString(ticket.kind.getStringRes()));
            electronicTicketBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rotateTicket(ticket, position);
                }
            });
            exchangeTicketBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rotateTicket(ticket, position);
                }
            });

        }

        public void bindBackSide(final ShortTicket ticket, final int position) {
            Log.d("TAG", "bind back side " + position);
            ticketInformationLayout.setVisibility(isShowingTicketInfo ? View.VISIBLE : View.GONE);
            ticketNumberLayout.setVisibility(isShowingTicketInfo ? View.GONE : View.VISIBLE);
            itemView.setScaleX(isShowingTicketInfo ? 1 : -1);
            //TODO refactor
            electronicTicketNumberLayout.setVisibility(ticket.electronic ? View.VISIBLE : View.GONE);
            analogTicketNumberLayout.setVisibility(ticket.electronic ? View.GONE : View.VISIBLE);
            if (ticket.electronic) {
                byte[] decodedString = Base64.decode(ticket.qrImage, Base64.DEFAULT);
                final Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                qrCodeImage.setImageBitmap(qrCodeBitmap);
                //TODO recycle?
                electronicTicketNumber.setText(ticket.uid);
            } else {
                byte[] decodedString = Base64.decode(ticket.barcodeImage, Base64.DEFAULT);
                final Bitmap barcodeBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                barCodeImage.setImageBitmap(barcodeBitmap);
                //TODO recycle?
                analogTicketNumber.setText(ticket.uid);//TODO is correct data?

            }
            ticketInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rotateTicket(ticket, position);
                }
            });
        }

        private void rotateTicket(ShortTicket ticket, int position) {
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = itemView.getWidth();
            layoutParams.height = itemView.getHeight();
            itemView.setLayoutParams(layoutParams);
            notifyItemChanged(position, new MyTicketsItemAnimator.TicketItemHolderInfo(ticket, position));
        }
    }

}
