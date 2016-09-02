package com.uzapp.view.main.tickets;

import com.uzapp.pojo.TicketKind;
import com.uzapp.pojo.WagonClass;
import com.uzapp.pojo.WagonType;

/**
 * Created by vika on 23.08.16.
 */
public class ShortTicket {
    public boolean electronic;
    public String qrImage;
    public String barcodeImage;
    public String uid;
    public TicketKind kind;
    public long departureDate;
    public long arrivalDate;
    public String stationFromName;
    public String stationToName;
    public String train;
    public int wagon;
    public int place;
    public WagonType wagonType;
    public WagonClass wagonClass;
    public double cost;
    public String firstname;
    public String lastname;
}
