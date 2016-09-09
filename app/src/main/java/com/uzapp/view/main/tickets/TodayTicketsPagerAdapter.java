package com.uzapp.view.main.tickets;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.uzapp.R;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 02.09.16.
 */
public class TodayTicketsPagerAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {
    private List<ShortTicket> ticketList = new ArrayList<>();
    int iconResId = R.drawable.bullet_inactive;
    int activeIconResId = R.drawable.bullet_active;
    int currentItem = 0;

    public TodayTicketsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addTickets(List<ShortTicket> ticketList) {
        this.ticketList.addAll(ticketList);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return TodayTicketItemFragment.getInstance(ticketList.get(position));
    }


    public void setCurrentPosition(int i) {
        currentItem = i;
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

}
