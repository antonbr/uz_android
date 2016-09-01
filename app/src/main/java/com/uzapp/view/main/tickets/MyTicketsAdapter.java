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
import com.uzapp.pojo.WagonType;
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
    private final int ELECTRONIC_TICKET_TYPE = 0;
    private final int ANALOGUE_TICKET_TYPE = 1;
    private final int CAMERA_DISTANCE = 5;
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

    public List<ShortTicket> getTicketList() {
        return ticketList;
    }

    @Override
    public int getItemViewType(int position) {
        if (ticketList.get(position).electronic) return ELECTRONIC_TICKET_TYPE;
        else return ANALOGUE_TICKET_TYPE;
    }

    @Override
    public MyTicketsAdapter.TicketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyTicketsAdapter.TicketHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        if (viewType == ELECTRONIC_TICKET_TYPE) {
            itemView = inflater.inflate(R.layout.ticket_item_electronic, parent, false);
            viewHolder = new TicketElectronicHolder(itemView);
        } else {
            itemView = inflater.inflate(R.layout.ticket_item_analogue, parent, false);
            viewHolder = new TicketAnalogueHolder(itemView);
        }
        itemView.setCameraDistance(itemView.getCameraDistance() * CAMERA_DISTANCE);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyTicketsAdapter.TicketHolder holder, int position) {
        ShortTicket ticket = ticketList.get(position);
        holder.bindView(ticket, position);
    }

    @Override
    public void onBindViewHolder(MyTicketsAdapter.TicketHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.size() == 0) {
            ShortTicket ticket = ticketList.get(position);
            holder.bindView(ticket, position);
        }
    }

    @Override
    public void onViewRecycled(MyTicketsAdapter.TicketHolder holder) {
        super.onViewRecycled(holder);
        holder.isShowingTicketInfo = true;
        holder.itemView.setScaleX(1);
        holder.itemView.setRotationY(0);
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class TicketElectronicHolder extends TicketHolder {
        @BindView(R.id.electronicTicketBtn) TextView electronicTicketBtn;
        @BindView(R.id.electronicTicketNumberLayout) ViewGroup electronicTicketNumberLayout;
        @BindView(R.id.qrCodeImage) ImageView qrCodeImage;
        @BindView(R.id.electronicTicketNumber) TextView electronicTicketNumber;

        public TicketElectronicHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindFrontSide(final ShortTicket ticket, final int position) {
            super.bindFrontSide(ticket, position);
            electronicTicketBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rotateTicket(ticket, position);
                }
            });

        }

        public void bindBackSide(final ShortTicket ticket, final int position) {
            super.bindBackSide(ticket,position);
            byte[] decodedString = Base64.decode(ticket.qrImage, Base64.DEFAULT);
            final Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            qrCodeImage.setImageBitmap(qrCodeBitmap);
            electronicTicketNumber.setText(ticket.uid);
            ticketInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rotateTicket(ticket, position);
                }
            });
        }
    }

    public class TicketAnalogueHolder extends TicketHolder {
        @BindView(R.id.exchangeTicketBtn) TextView exchangeTicketBtn;
        @BindView(R.id.analogTicketNumberLayout) ViewGroup analogTicketNumberLayout;
        @BindView(R.id.barCodeImage) ImageView barCodeImage;
        @BindView(R.id.analogTicketNumber) TextView analogTicketNumber;
        @BindView(R.id.printBtn) Button printBtn;

        public TicketAnalogueHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindFrontSide(final ShortTicket ticket, final int position) {
            super.bindFrontSide(ticket, position);
            exchangeTicketBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rotateTicket(ticket, position);
                }
            });

        }

        @Override
        public void bindBackSide(final ShortTicket ticket, final int position) {
            super.bindBackSide(ticket,position);
            try {
                byte[] decodedString = Base64.decode(ticket.barcodeImage, Base64.DEFAULT);
                final Bitmap barcodeBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                barCodeImage.setImageBitmap(barcodeBitmap);
            } catch (IllegalArgumentException e) {
                Log.e(MyTicketsAdapter.class.getName(), e.getMessage());
                barCodeImage.setImageResource(0);
            }
            analogTicketNumber.setText(ticket.uid);//TODO is correct data? need formatting
            ticketInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rotateTicket(ticket, position);
                }
            });
        }
    }

    public abstract class TicketHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ticketInformationLayout) ViewGroup ticketInformationLayout;
        @BindView(R.id.ticketNumberLayout) ViewGroup ticketNumberLayout;
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
        @BindView(R.id.ticketInfoBtn) TextView ticketInfoBtn;

        boolean isShowingTicketInfo = true;

        public TicketHolder(View itemView) {
            super(itemView);
        }

        public void bindView(ShortTicket ticket, int position) {
            if (isShowingTicketInfo) {
                bindFrontSide(ticket, position);
            } else {
                bindBackSide(ticket, position);
            }
        }

        public void bindFrontSide(final ShortTicket ticket, final int position) {
            itemView.setScaleX(isShowingTicketInfo ? 1 : -1);
            ticketInformationLayout.setVisibility(isShowingTicketInfo ? View.VISIBLE : View.GONE);
            ticketNumberLayout.setVisibility(isShowingTicketInfo ? View.GONE : View.VISIBLE);

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
            wagonNumber.setText(String.valueOf(ticket.wagon));
            placeNumber.setText(String.valueOf(ticket.place));
            StringBuilder wagonTypeBuilder = new StringBuilder(context.getString(ticket.wagonType.getLongNameStringRes()));
            if (ticket.wagonType.ordinal() == WagonType.SEATING.ordinal()) {
                wagonTypeBuilder.append(" ").append(context.getString(ticket.wagonClass.getLongNameStringRes()));
            }
            wagonType.setText(wagonTypeBuilder);
            String placeTypeString = "";
            if (ticket.wagonType.ordinal() == WagonType.SUITE.ordinal()) {
                placeTypeString = context.getString(R.string.filter_bottom);
            } else if (ticket.wagonType.ordinal() == WagonType.COUPE.ordinal() || ticket.wagonType.ordinal() == WagonType.BERTH.ordinal()) {
                placeTypeString = (CommonUtils.isOdd((int) ticket.place))
                        ? context.getString(R.string.filter_bottom) : context.getString(R.string.filter_top);
            }
            placeType.setText(placeTypeString);
            StringBuilder fullNameString = new StringBuilder(ticket.firstname);
            if (fullNameString.length() > 0) {
                fullNameString.append(" ");
            }
            fullNameString.append(ticket.lastname);
            fullName.setText(fullNameString);
            price.setText(context.getString(R.string.ticket_cost, df.format(ticket.cost)));
            ticketType.setText(context.getString(ticket.kind.getStringRes()));
        }

        public void bindBackSide(final ShortTicket ticket, final int position){
            itemView.setScaleX(isShowingTicketInfo ? 1 : -1);
            ticketInformationLayout.setVisibility(isShowingTicketInfo ? View.VISIBLE : View.GONE);
            ticketNumberLayout.setVisibility(isShowingTicketInfo ? View.GONE : View.VISIBLE);
        }

        protected void rotateTicket(ShortTicket ticket, int position) {
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = itemView.getWidth();
            layoutParams.height = itemView.getHeight();
            itemView.setLayoutParams(layoutParams);
            notifyItemChanged(position, new MyTicketsItemAnimator.TicketItemHolderInfo(ticket, position));
        }
    }

}
