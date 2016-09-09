package com.uzapp.view.main.tickets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.util.Constants;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by vika on 02.09.16.
 */
public class TodayTicketItemFragment extends Fragment {
    private SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_FORTMAT);
    @BindView(R.id.trainName) TextView trainName;
    @BindView(R.id.stationFrom) TextView stationFrom;
    @BindView(R.id.stationTo) TextView stationTo;
    @BindView(R.id.departureTime) TextView departureTime;
    @BindView(R.id.wagonNumber) TextView wagonNumber;
    @BindView(R.id.placeNumber) TextView placeNumber;
    @BindView(R.id.firstName) TextView firstName;
    @BindView(R.id.lastName) TextView lastName;
    //for electronic ticket
    @Nullable
    @BindView(R.id.qrCodeImage)
    ImageView qrCodeImage;
    @Nullable
    @BindView(R.id.electronicTicketNumber)
    TextView electronicTicketNumber;
    //for analogue ticket
    @Nullable
    @BindView(R.id.barCodeImage)
    ImageView barCodeImage;
    @Nullable
    @BindView(R.id.analogTicketNumber)
    TextView analogTicketNumber;
    private Unbinder unbinder;
    private ShortTicket ticket;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = ticket.electronic ? R.layout.today_ticket_electronic_item : R.layout.today_ticket_analogue_item;
        View view = inflater.inflate(layoutRes, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (ticket.electronic) {
            bindElectronicTicket();
        } else {
            bindAnalogueTicket();
        }
        return view;
    }

    private void bindHeader() {
        trainName.setText(ticket.train);
        stationFrom.setText(ticket.stationFromName);
        stationTo.setText(ticket.stationToName);
        Date departureDateObj = new Date(TimeUnit.SECONDS.toMillis(ticket.departureDate));
        departureTime.setText(timeFormat.format(departureDateObj));
        wagonNumber.setText(String.valueOf(ticket.wagon));
        placeNumber.setText(String.valueOf(ticket.place));
        firstName.setText(ticket.firstname);
        lastName.setText(ticket.lastname);
    }

    private void bindElectronicTicket() {
        bindHeader();
        if (qrCodeImage != null && electronicTicketNumber != null) {
            byte[] decodedString = Base64.decode(ticket.qrImage, Base64.DEFAULT);
            final Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            qrCodeImage.setImageBitmap(qrCodeBitmap);
            electronicTicketNumber.setText(ticket.uid);
        }
    }

    private void bindAnalogueTicket() {
        bindHeader();
        if (barCodeImage != null && analogTicketNumber != null) {
            try {
                byte[] decodedString = Base64.decode(ticket.barcodeImage, Base64.DEFAULT);
                final Bitmap barcodeBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                barCodeImage.setImageBitmap(barcodeBitmap);
            } catch (IllegalArgumentException e) {
                Log.e(MyTicketsListAdapter.class.getName(), e.getMessage());
                barCodeImage.setImageResource(0);
            }
            analogTicketNumber.setText(ticket.uid);//TODO is correct data? need formatting
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        ticket = Parcels.unwrap(args.getParcelable("ticket"));
    }

    public static TodayTicketItemFragment getInstance(ShortTicket ticket) {
        TodayTicketItemFragment fragment = new TodayTicketItemFragment();
        Bundle args = new Bundle();
        args.putParcelable("ticket", Parcels.wrap(ticket));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
