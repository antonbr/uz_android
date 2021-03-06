package com.uzapp.view.main.trains;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 27.07.16.
 */
public class TrainWaysViewPagerAdapter extends FragmentPagerAdapter {
    private final List<TrainsResultListFragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public TrainWaysViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public TrainsResultListFragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(TrainsResultListFragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

}
