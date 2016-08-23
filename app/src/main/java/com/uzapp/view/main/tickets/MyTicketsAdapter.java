package com.uzapp.view.main.tickets;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 23.08.16.
 */
public class MyTicketsAdapter extends PagerAdapter {
    private List<TicketForAdapter> ticketList = new ArrayList<>();
    private Context context;

    public MyTicketsAdapter(Context context) {
        this.context = context;
    }

    public void addTickets(List<TicketForAdapter> ticketList) {
        this.ticketList.addAll(ticketList);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        TicketForAdapter ticket = ticketList.get(position);
        TicketView ticketView = new TicketView(context);
        collection.addView(ticketView);
        ticketView.initView(ticket);
        return ticketView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    public TicketForAdapter getTicketAtPosition(int position) {
        return  ticketList.get(position);
    }

    @Override
    public int getCount() {
        return ticketList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
