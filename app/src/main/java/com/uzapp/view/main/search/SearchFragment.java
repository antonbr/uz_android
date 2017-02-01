package com.uzapp.view.main.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.uzapp.R;
import com.uzapp.pojo.route.RouteHistoryItem;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.BaseFragment;
import com.uzapp.view.common.CheckableImageView;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.search.date.PickDateFragment;
import com.uzapp.view.main.search.station.StationSearchFragment;
import com.uzapp.view.main.tickets.TodayTicketsFragment;
import com.uzapp.view.main.trains.TabbedTrainFragment;

import java.util.Date;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static com.uzapp.R.id.firstDate;
import static com.uzapp.R.id.secondDate;

/**
 * Created by vika on 13.07.16.
 */
public class SearchFragment extends BaseFragment implements DatePickerAdapter.OnDateClickListener, SearchPresenter.SearchView {
    private static final String TAG = SearchFragment.class.getName();
    protected static final int SELECT_STATION_FROM_REQUEST_CODE = 11;
    protected static final int SELECT_STATION_TO_REQUEST_CODE = 12;
    protected static final int SELECT_FIRST_DATE = 13;
    protected static final int SELECT_SECOND_DATE = 14;
    @BindView(R.id.pathFrom) EditText pathFrom;
    @BindView(R.id.pathTo) EditText pathTo;
    @BindView(R.id.useLocationBtn) CheckableImageView useLocationBtn;
    @BindView(R.id.backRouteToggle) ToggleButton backRouteToggle;
    @BindView(R.id.datePickerList) RecyclerView datePickerList;
    @BindView(R.id.monthName) TextView monthName;
    @BindView(R.id.findTicketsBtn) Button findTicketsBtn;
    @BindView(R.id.selectedDateLayout) RelativeLayout selectedDateLayout;
    @BindView(R.id.firstMonthName) TextView firstMonthName;
    @BindView(R.id.secondMonthName) TextView secondMonthName;
    @BindView(firstDate) DateItemView firstDateLayout;
    @BindView(secondDate) DateItemView secondDateLayout;
    @BindView(R.id.inDays) TextView inDays;
    @BindView(R.id.seeFullCalendarBtn) Button seeFullCalendarBtn;
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    @BindDimen(R.dimen.small_padding) int datePickerItemPadding;
    private Unbinder unbinder;
    private LinearLayoutManager datePickerLayoutManager;
    private DatePickerAdapter datePickerAdapter;
    private SearchPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (presenter == null) {
            presenter = new SearchPresenter(getArguments());
        }
        presenter.attachView(this);
        ((MainActivity) getActivity()).showNavigationBar();
        ((MainActivity) getActivity()).getBottomNavigationBar().setCurrentItem(Constants.BOTTOM_NAVIGATION_SEARCH, false);
        return view;
    }

    public static SearchFragment getInstance(RouteHistoryItem routeHistoryItem) {
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(SearchPresenter.getArgs(routeHistoryItem));
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.useLocationBtn)
    void onLocationBtnClicked() {
        useLocationBtn.toggle();
        //pathFrom.setTranslationY(useLocationBtn.isChecked() ? hintPadding : 0);
        presenter.onLocationBtnClicked(useLocationBtn.isChecked());
    }

    @OnClick({R.id.pathTo, R.id.pathFrom})
    void onSelectPathFromClick(View view) {
        StationSearchFragment fragment = StationSearchFragment.getInstance
                (getString(view.getId() == R.id.pathFrom ? R.string.search_path_from : R.string.search_path_to));
        fragment.setTargetFragment(this, view.getId() == R.id.pathFrom ? SELECT_STATION_FROM_REQUEST_CODE :
                SELECT_STATION_TO_REQUEST_CODE);
        ((MainActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
    }

    @OnTextChanged({R.id.pathTo, R.id.pathFrom})
    void onPathTextChanged() {
        presenter.checkAllFieldsFilled();
    }

    @OnCheckedChanged(R.id.backRouteToggle)
    void onBackRouteToggleChanged() {
        presenter.checkAllFieldsFilled();
        if (datePickerAdapter != null) {
            datePickerAdapter.setSelectingSecondDate(backRouteToggle.isChecked());
        }
    }

    @OnClick(R.id.seeFullCalendarBtn)
    void seeFullCalendarBtn() {
        presenter.seeFullCalendarBtnClicked();
    }

    @OnClick(R.id.findTicketsBtn)
    void onFindTicketsBtnClicked() {
        presenter.onFindTicketsBtnClicked();
    }

    @OnClick(R.id.todayTicketsBtn)
    void onTodayTicketsBtnClicked() {
        ((MainActivity) getActivity()).addFragment(new TodayTicketsFragment(), R.anim.slide_up, R.anim.slide_down);
    }

    @OnClick(R.id.rightBtn)
    void onResetBtnClicked() {
        presenter.onResetBtnClicked();
    }

    @OnClick(R.id.resetDateBtn)
    void onResetDateBtnClicked() {
        presenter.onResetDateBtnClicked();
    }

    @Override
    public void initDatePickerList(final List<Date> dates) {
        datePickerLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        datePickerList.setLayoutManager(datePickerLayoutManager);
        datePickerList.post(new Runnable() {
            @Override
            public void run() {
                if (datePickerList != null) {
                    int itemWidth = datePickerList.getWidth() / Constants.DAYS_TO_SHOW_SMALL_CALENDAR;
                    if (datePickerAdapter == null) {
                        datePickerAdapter = new DatePickerAdapter(dates, itemWidth, SearchFragment.this);
                    } else {
                        datePickerAdapter.setItemWidth(itemWidth);
                    }
                    datePickerList.setAdapter(datePickerAdapter);
                    firstDateLayout.setViewWidth(itemWidth);
                    secondDateLayout.setViewWidth(itemWidth);

                    //this check is useful when user press back from trains page
                    presenter.onDateViewReady();
                }
            }
        });


        datePickerList.addOnScrollListener(dateScrollListener);

    }

    @Override
    public void requestPermission(String[] permissions, int requestCode) {
        this.requestPermissions(permissions, requestCode);
    }

    @Override
    public void setLocationEnabled(boolean isEnabled) {
        useLocationBtn.setChecked(isEnabled);
    }

    @Override
    public void setFindBtnEnabled(boolean isEnabled) {
        findTicketsBtn.setEnabled(isEnabled);
    }

    @Override
    public boolean isBackRouteToggleChecked() {
        return backRouteToggle.isChecked();
    }

    @Override
    public void showFullCalendarFragment(Date date) {
        PickDateFragment fragment = PickDateFragment.getInstance(date);
        fragment.setTargetFragment(this, backRouteToggle.isChecked() ? SELECT_SECOND_DATE : SELECT_FIRST_DATE);
        ((MainActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    public void showSelectTrainFragment(long stationFromCode, long stationToCode, long firstDate, long secondDate) {
        Fragment fragment = TabbedTrainFragment.getInstance(stationFromCode, stationToCode, firstDate, secondDate);
        ((MainActivity) getActivity()).replaceFragment(fragment, true);
    }

    @Override
    public void setBackRouteToggleChecked(boolean isChecked) {
        backRouteToggle.setChecked(isChecked);
    }

    @Override
    public void setBackRouteToggleEnabled(boolean isEnabled) {
        backRouteToggle.setEnabled(isEnabled);
    }

    @Override
    public void setMonthName(String title) {
        monthName.setText(title);
    }

    @Override
    public void setSelectedFirstPosition(int selectedFirstPosition) {
        datePickerAdapter.setSelectedFirstPosition(selectedFirstPosition);
    }

    @Override
    public void setSelectedSecondPosition(int selectedSecondPosition) {
        datePickerAdapter.setSelectedSecondPosition(selectedSecondPosition);
    }

    @Override
    public void setSelectingSecondDate(boolean isSelectingSecondDate) {
        datePickerAdapter.setSelectingSecondDate(isSelectingSecondDate);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment != null && fragment instanceof PickDateFragment) {
            fragment.setTargetFragment(this, fragment.getTargetRequestCode());
        } else if (fragment != null && fragment instanceof StationSearchFragment) {
            fragment.setTargetFragment(this, fragment.getTargetRequestCode());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_STATION_FROM_REQUEST_CODE) {
                presenter.onSelectStationFromResult(data);
            } else if (requestCode == SELECT_STATION_TO_REQUEST_CODE) {
                presenter.onSelectStationToResult(data);
            } else if (requestCode == SELECT_FIRST_DATE) {
                presenter.onSelectFirstDateResult(data);
            } else if (requestCode == SELECT_SECOND_DATE) {
                presenter.onSelectSecondDateResult(data);
            }
        }
    }

    @Override
    public void scrollToSelectedPosition(final int selectedPosition) {
        if (selectedPosition < datePickerLayoutManager.findFirstCompletelyVisibleItemPosition() ||
                selectedPosition > datePickerLayoutManager.findLastCompletelyVisibleItemPosition()) {
            datePickerList.post(new Runnable() {
                @Override
                public void run() {
                    datePickerLayoutManager.scrollToPositionWithOffset(selectedPosition, 0);
                    presenter.onMonthScrollStateChanged(selectedPosition);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }


    private RecyclerView.OnScrollListener dateScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                presenter.onMonthScrollStateChanged(datePickerLayoutManager.findFirstCompletelyVisibleItemPosition());
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    @Override
    public void onDateItemClick(int position, Date date) {
        presenter.onDateItemClick(position, date);
    }

    @Override
    public void setSelectedDateLayoutVisible(Date firstDate, Date secondDate, String firstMonth, String secondMonth) {
        setSelectedDateLayoutVisibility(true);
        firstDateLayout.bindDate(firstDate);
        secondDateLayout.bindDate(secondDate);
        firstDateLayout.setSelectedDayBackground(true);
        secondDateLayout.setSelectedDayBackground(false);
        firstDateLayout.setSelectedTextColor();
        secondDateLayout.setSelectedTextColor();
        firstMonthName.setText(firstMonth);
        secondMonthName.setText(secondMonth);
        int daysBetween = (int) CommonUtils.getDaysDifference(secondDate, firstDate);
        inDays.setText(getResources().getQuantityString(R.plurals.search_in_n_days, daysBetween, daysBetween));
    }

    @Override
    public void setSelectedDateLayoutInvisible() {
        setSelectedDateLayoutVisibility(false);
        datePickerAdapter.setSelectedSecondPosition(-1);
    }

    private void setSelectedDateLayoutVisibility(boolean visible) {
        selectedDateLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
        datePickerList.setVisibility(visible ? View.GONE : View.VISIBLE);
        monthName.setVisibility(visible ? View.GONE : View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) seeFullCalendarBtn.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, visible ? R.id.selectedDateLayout : R.id.datePickerList);
        seeFullCalendarBtn.setLayoutParams(layoutParams);
        seeFullCalendarBtn.setEnabled(!visible);
        backRouteToggle.setEnabled(!visible);
        presenter.checkAllFieldsFilled();
    }

    @Override
    public void showProgress(boolean isLoading) {

    }

    @Override
    public void setPathFromText(String pathFrom) {
        this.pathFrom.setText(pathFrom);
    }

    @Override
    public void setPathToText(String pathTo) {
        this.pathTo.setText(pathTo);
    }

}
