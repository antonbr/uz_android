package com.uzapp.view.main.tickets;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.uzapp.R;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 23.08.16.
 */
public class OldMyTicketsAdapter extends PagerAdapter implements IconPagerAdapter {
    private List<ShortTicket> ticketList = new ArrayList<>();
    private Context context;
    int iconResId = R.drawable.bullet_inactive;
    int activeIconResId = R.drawable.bullet_active;
    int currentItem = 0;

    public OldMyTicketsAdapter(Context context) {
        this.context = context;
    }

    public void addTickets(List<ShortTicket> ticketList) {
        this.ticketList.addAll(ticketList);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ShortTicket ticket = ticketList.get(position);
        OldTicketView ticketView = new OldTicketView(context);
        collection.addView(ticketView);
        ticketView.initView(ticket);
        return ticketView;
    }

    public void setCurrentPosition(int i) {
        currentItem = i;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    public ShortTicket getTicketAtPosition(int position) {
        return ticketList.get(position);
    }

    @Override
    public int getIconResId(int i) {
        if (i == currentItem)
            return activeIconResId;

        return iconResId;
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
