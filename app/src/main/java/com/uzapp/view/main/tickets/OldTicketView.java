package com.uzapp.view.main.tickets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.util.Constants;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vika on 23.08.16.
 */
public class OldTicketView extends LinearLayout {
    private SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_FORTMAT);
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DAY_MONTH_YEAR_FORMAT);
    private DecimalFormat df = new DecimalFormat("#.00");
    @BindView(R.id.trainName) TextView trainName;
    @BindView(R.id.stationFrom) TextView stationFrom;
    @BindView(R.id.stationTo) TextView stationTo;
    @BindView(R.id.departureFullDate) TextView departureFullDate;
    @BindView(R.id.departureTime) TextView departureTime;
    @BindView(R.id.arrivalFullDate) TextView arrivalFullDate;
    @BindView(R.id.arrivalTime) TextView arrivalTime;
    @BindView(R.id.wagonNumber) TextView wagonNumber;
    @BindView(R.id.placeNumber) TextView placeNumber;
    @BindView(R.id.price) TextView price;
    @BindView(R.id.ticketNumber) TextView ticketNumber;
    @BindView(R.id.firstName) TextView firstName;
    @BindView(R.id.lastName) TextView lastName;
    @BindView(R.id.ticketType) TextView ticketType;
    @BindView(R.id.qrCodeImage) ImageView qrCodeImage;
    @BindView(R.id.orderType) ImageView orderType;

    public OldTicketView(Context context) {
        this(context, null);
    }

    public OldTicketView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.old_ticket_item, this, true);
        ButterKnife.bind(this);
        setOrientation(VERTICAL);
//        setBackground(ContextCompat.getDrawable(context, R.drawable.shadow));
//
//        setShowDividers(SHOW_DIVIDER_MIDDLE);
//        setDividerDrawable(ContextCompat.getDrawable(context, R.drawable.divider_hint_color_horizontal));
    }

    public void initView(ShortTicket ticket) {
        trainName.setText(ticket.train);
        stationFrom.setText(ticket.stationFromName);
        stationTo.setText(ticket.stationToName);
        Date departureDate = new Date(TimeUnit.SECONDS.toMillis(ticket.departureDate));
        Date arrivalDate = new Date(TimeUnit.SECONDS.toMillis(ticket.arrivalDate));
        arrivalFullDate.setText(dateFormat.format(arrivalDate));
        arrivalTime.setText(timeFormat.format(arrivalDate));
        departureFullDate.setText(dateFormat.format(departureDate));
        departureTime.setText(timeFormat.format(departureDate));
        wagonNumber.setText(String.valueOf(ticket.wagon));
        placeNumber.setText(String.valueOf(ticket.place));
        price.setText(getContext().getString(R.string.ticket_cost, df.format(ticket.cost)));
        ticketNumber.setText(ticket.uid); //TODO change format
        firstName.setText(ticket.firstname);
        lastName.setText(ticket.lastname);
        ticketType.setText(getContext().getString(ticket.kind.getStringRes()));
        byte[] decodedString = Base64.decode(ticket.qrImage, Base64.DEFAULT);
        final Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        qrCodeImage.setImageBitmap(qrCodeBitmap);
        qrCodeImage.post(new Runnable() {
            @Override
            public void run() {
                if (qrCodeImage.getWidth() != qrCodeImage.getHeight()) {
                    RelativeLayout.LayoutParams imageLayoutParams = (RelativeLayout.LayoutParams) qrCodeImage.getLayoutParams();
                    imageLayoutParams.width = qrCodeImage.getHeight();
                    qrCodeImage.setLayoutParams(imageLayoutParams);

                }
            }
        });
    }
}