package com.uzapp.view.main.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.view.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by vika on 08.09.16.
 */
public class SearchTicketSettingsFragment extends Fragment {
    private static final int MIN_PASSENGERS_COUNT = 1;
    private static final int MAX_PASSENGERS_COUNT = 4;
    private Unbinder unbinder;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.plusBtn) ImageButton plusBtn;
    @BindView(R.id.minusBtn) ImageButton minusBtn;
    @BindView(R.id.passengersCount) TextView passengersCount;
    @BindView(R.id.placementRadioGroup) RadioGroup placementRadioGroup;
    @BindView(R.id.singleCoupeBtn) SwitchCompat singleCoupeSwitchBtn;
    @BindView(R.id.wcSwitchBtn) SwitchCompat wcSwitchBtn;
    @BindView(R.id.conductorSwitchBtn) SwitchCompat conductorSwitchBtn;
    @BindView(R.id.sortTitle) TextView sortTitle;
    @BindView(R.id.sortRadioGroup) SegmentedGroup sortRadioGroup;
    private int passengersCountValue = MIN_PASSENGERS_COUNT;
    private Type pageType;

    public enum Type {
        SEARCH_FILTER, PROFILE_SETTINGS
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_ticket_settings_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).hideNavigationBar();
        pageType = (Type) getArguments().getSerializable("type");
        toolbarTitle.setText(pageType == Type.SEARCH_FILTER ? R.string.search_filter : R.string.profile_search_settings);
        sortTitle.setVisibility(pageType == Type.SEARCH_FILTER ? View.VISIBLE : View.GONE);
        sortRadioGroup.setVisibility(pageType == Type.SEARCH_FILTER ? View.VISIBLE : View.GONE);
        setStatePlusMinusBtns();
        return view;
    }

    public static SearchTicketSettingsFragment getInstance(Type type) {
        SearchTicketSettingsFragment fragment = new SearchTicketSettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.closeBtn)
    void onBackBtnClicked() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.tickBtn)
    void onSaveBtnClicked() {
        //TODO
    }

    @OnClick(R.id.minusBtn)
    void onMinusBtnClicked() {
        if (passengersCountValue > MIN_PASSENGERS_COUNT) {
            passengersCountValue--;
            setPassengersText();
        }
        toggleVisibilityOfSingleCoupeSwitch();
        setStatePlusMinusBtns();
    }

    @OnClick(R.id.plusBtn)
    void onPlusBtnClicked() {
        if (passengersCountValue < MAX_PASSENGERS_COUNT) {
            passengersCountValue++;
            setPassengersText();
        }
        toggleVisibilityOfSingleCoupeSwitch();
        setStatePlusMinusBtns();
    }

    private void setStatePlusMinusBtns() {
        minusBtn.setEnabled(passengersCountValue > MIN_PASSENGERS_COUNT);
        plusBtn.setEnabled(passengersCountValue < MAX_PASSENGERS_COUNT);
    }

    private void setPassengersText() {
        passengersCount.setText(getResources().getQuantityString(R.plurals.passengers_count, passengersCountValue, passengersCountValue));
    }

    private void toggleVisibilityOfSingleCoupeSwitch() {
        singleCoupeSwitchBtn.setVisibility(passengersCountValue == MIN_PASSENGERS_COUNT ? View.GONE : View.VISIBLE);
        if (singleCoupeSwitchBtn.getVisibility() == View.GONE) {
            singleCoupeSwitchBtn.setChecked(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
