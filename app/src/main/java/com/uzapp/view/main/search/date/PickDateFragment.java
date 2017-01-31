package com.uzapp.view.main.search.date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.view.main.MainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vika on 19.07.16.
 */
public class PickDateFragment extends Fragment implements CalendarDaysAdapter.OnDateSelectedListener, PickDatePresenter.PickDateView {
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.dateTextField) EditText dateTextField;
    @BindView(R.id.tomorrowDate) TextView tomorrowDateView;
    @BindView(R.id.tomorrowBtn) ViewGroup tomorrowBtn;
    @BindView(R.id.dayAfterTomorrowBtn) ViewGroup dayAfterTomorrowBtn;
    @BindView(R.id.dayAfterTomorrowDate) TextView dayAfterTomorrowDateView;
    @BindView(R.id.todayBtn) ToggleButton todayBtn;
    @BindView(R.id.monthPager) VerticalViewPager monthPager;
    @BindView(R.id.bottomBar) LinearLayout bottomBar;
    @BindView(R.id.okBtn) Button okBtn;
    private Unbinder unbinder;
    private MonthPagerAdapter monthPagerAdapter;
    private PickDatePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pick_date_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText(R.string.calendar_when);
        presenter = new PickDatePresenter(this, getArguments());
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).hideNavigationBar();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) getActivity()).showNavigationBar();
    }

    public static PickDateFragment getInstance(Date minDate) {
        PickDateFragment fragment = new PickDateFragment();
        Bundle args = new Bundle();
        args.putSerializable("minDate", minDate);
        fragment.setArguments(args);
        return fragment;
    }

    public static PickDateFragment getInstance(ArrayList<String> availableDates) {
        PickDateFragment fragment = new PickDateFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("availableDates", availableDates);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.ticketCloseBtn)
    void onCloseBtnClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.okBtn)
    void onOkBtnClick() {
        presenter.onOkBtnClick();
    }

    @OnCheckedChanged(R.id.todayBtn)
    void onTodayBtnClicked(boolean checked) {
        presenter.onTodayBtnClicked(checked);
    }

    @OnClick(R.id.tomorrowBtn)
    void onTomorrowBtnClicked() {
        presenter.onTomorrowBtnClicked();
    }

    @OnClick(R.id.dayAfterTomorrowBtn)
    void onDayAfterTomorrowBtnClicked() {
        presenter.onDayAfterTomorrowBtnClicked();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onDateSelected(int pagePosition, int dayPosition) {
        presenter.onDateSelected(pagePosition, dayPosition);
    }

    @Override
    public void showSelectedDateText(String text) {
        dateTextField.setText(text);
    }

    @Override
    public boolean isTodayBtnChecked() {
        return todayBtn.isChecked();
    }

    @Override
    public void setTodayBtnCheckedState(boolean isChecked) {
        todayBtn.setChecked(isChecked);
    }


    @Override
    public void setOkBtnEnabled(boolean isEnabled) {
        okBtn.setEnabled(isEnabled);
    }


    @Override
    public void showProgress(boolean isLoading) {

    }

    @Override
    public void showError(String error) {
        CommonUtils.showSnackbar(getView(), error);
    }

    @Override
    public void showBottomBar(boolean isVisible) {
        bottomBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setTomorrowBtnEnabled(boolean isEnabled) {
        tomorrowBtn.setEnabled(isEnabled);
    }

    @Override
    public void setDayAfterTomorrowBtnEnabled(boolean isEnabled) {
        dayAfterTomorrowBtn.setEnabled(isEnabled);
    }

    @Override
    public void setTomorrowBtnText(String text) {
        tomorrowDateView.setText(text);
    }

    @Override
    public void setDayAfterTomorrowBtnText(String text) {
        dayAfterTomorrowDateView.setText(text);
    }

    @Override
    public void setTodayBtnEnabled(boolean isEnabled) {
        todayBtn.setEnabled(isEnabled);
    }

    @Override
    public void initView(List<List<Date>> allDaysByMonths, List<Date> availableDates) {
        monthPagerAdapter = new MonthPagerAdapter(getContext(), allDaysByMonths, availableDates, this);
        monthPager.setOffscreenPageLimit(2);
        monthPager.setAdapter(monthPagerAdapter);
    }

    @Override
    public void returnResult(Date selectedDate) {
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            Intent i = new Intent();
            i.putExtra("date", selectedDate);
            targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        }
        getActivity().onBackPressed();
    }

    @Override
    public void selectDateInCalendarView(int pagePosition, int monthPosition) {
        monthPagerAdapter.updateSelection(pagePosition, monthPosition);
        monthPager.setCurrentItem(pagePosition);
    }
}
