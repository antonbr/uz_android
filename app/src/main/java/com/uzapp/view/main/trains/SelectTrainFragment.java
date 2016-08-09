package com.uzapp.view.main.trains;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vika on 26.07.16.
 */
public class SelectTrainFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = SelectTrainFragment.class.getName();
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DAY_MONTH_WEEK_FORMAT);
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;
    private Unbinder unbinder;
    private long stationFromCode, stationToCode, firstDate, secondDate;
    private boolean isGoingBack = false;
    private TrainWaysViewPagerAdapter viewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_train_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initArguments();
        setupViewPager();
        if (isGoingBack) {
            tabLayout.setupWithViewPager(viewPager);
        } else {
            tabLayout.setVisibility(View.GONE);
        }
        return view;
    }

    private void setupViewPager() {
        viewPagerAdapter = new TrainWaysViewPagerAdapter(getChildFragmentManager());
        String firstDateName = dateFormat.format(new Date(firstDate));
        String tabFirstName = getString(R.string.trains_first_tab_name, firstDateName);
        viewPagerAdapter.addFragment(TrainsResultListFragment.getInstance(stationFromCode, stationToCode, firstDate), tabFirstName);
        if (isGoingBack) {
            String secondDateName = dateFormat.format(new Date(secondDate));
            String tabSecondName = getString(R.string.trains_second_tab_name, secondDateName);
            viewPagerAdapter.addFragment(TrainsResultListFragment.getInstance(stationToCode, stationFromCode, secondDate), tabSecondName);
        }
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    /*
    second date here is date for return back option, in api there is date2 with other meaning
     */
    public static SelectTrainFragment getInstance(long stationFromCode, long stationToCode, long firstDate, long secondDate) {
        SelectTrainFragment fragment = new SelectTrainFragment();
        Bundle args = new Bundle();
        args.putLong("stationFromCode", stationFromCode);
        args.putLong("stationToCode", stationToCode);
        args.putLong("firstDate", firstDate);
        if (secondDate != 0) {
            args.putLong("secondDate", secondDate);
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void initArguments() {
        Bundle args = getArguments();
        stationFromCode = args.getLong("stationFromCode");
        stationToCode = args.getLong("stationToCode");
        firstDate = args.getLong("firstDate");
        if (args.containsKey("secondDate")) {
            secondDate = args.getLong("secondDate");
            isGoingBack = true;
        }
    }

    private void setToolbarTitle(int trainsCount) {
        toolbarTitle.setText(getResources().getQuantityString(R.plurals.trains_n_found, trainsCount, trainsCount));
    }

    public void onTrainsLoaded(long date, int trainsCount) {
        if (date == firstDate && viewPager.getCurrentItem() == 0) {
            setToolbarTitle(trainsCount);
        } else if (date == secondDate && viewPager.getCurrentItem() == viewPagerAdapter.getCount() - 1) {
            setToolbarTitle(trainsCount);
        }
    }

    @OnClick(R.id.backBtn)
    void onBackBtnPressed() {
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setToolbarTitle(viewPagerAdapter.getItem(position).getTrainsCount());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
