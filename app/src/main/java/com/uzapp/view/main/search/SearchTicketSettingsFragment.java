package com.uzapp.view.main.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.view.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vika on 08.09.16.
 */
public class SearchTicketSettingsFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.passengersCount) TextView passengersCount;
    @BindView(R.id.placementRadioGroup) RadioGroup placementRadioGroup;
    @BindView(R.id.singleCoupeBtn) SwitchCompat switchCompat;
    @BindView(R.id.wcSwitchBtn) SwitchCompat wcSwitchBtn;
    @BindView(R.id.conductorSwitchBtn) SwitchCompat conductorSwitchBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_ticket_settings_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).hideNavigationBar();
        toolbarTitle.setText(R.string.profile_search_settings);
        return view;
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

    }

    @OnClick(R.id.plusBtn)
    void onPlusBtnClicked() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        ((MainActivity) getActivity()).showNavigationBar();
    }
}
