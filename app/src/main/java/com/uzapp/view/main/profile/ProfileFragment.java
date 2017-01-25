package com.uzapp.view.main.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.RouteHistoryItem;
import com.uzapp.pojo.User;
import com.uzapp.util.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.PrefsUtil;
import com.uzapp.view.BaseActivity;
import com.uzapp.view.login.PhoneNumberTextInputEditText;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.utils.VerticalDividerItemDecoration;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 17.08.16.
 */
public class ProfileFragment extends Fragment implements ProfileRouteHistoryAdapter.OnStationClickListener {
    private static final int REQUEST_EDIT_PROFILE = 1;
    private static final int REQUEST_CHANGE_PASSWORD = 2;
    @BindView(R.id.fullName) TextView fullName;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.phoneNumber) TextView phoneNumber;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.mainScrollView) NestedScrollView mainScrollView;
    @BindView(R.id.routeHistoryList) RecyclerView routeHistoryList;
    @BindView(R.id.routeHistoryCard) CardView routeHistoryCard;
    @BindView(R.id.routeHistoryLabel) TextView routeHistoryLabel;
    @BindInt(R.integer.student_id_full_length) int studentIdLength;
    @BindDimen(R.dimen.profile_button_padding) int padding;
    private Unbinder unbinder;
    private User user;
    private ProfileRouteHistoryAdapter adapter;
    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        progressBar.setVisibility(View.VISIBLE);
        mainScrollView.setVisibility(View.GONE);
        loadUserInfo();
        ((MainActivity) getActivity()).showNavigationBar();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).selectNoneItemsInNavBar();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!realm.isClosed()) {
            realm.close();
        }
    }

    private void loadUserInfo() {
        Call<User> call = ApiManager.getApi(getContext()).getUser();
        call.enqueue(userCallback);
    }

    private void loadRoutesHistory() {
        Call<List<RouteHistoryItem>> call = ApiManager.getApi(getContext()).getRouteHistory();
        call.enqueue(routeHistoryCallback);
    }

    @OnClick({R.id.ticketReturnBtn, R.id.bookingHistoryBtn, R.id.bonusProgramBtn, R.id.paymentMethodsBtn, R.id.profileSettingsBtn,
            R.id.searchSettingsBtn, R.id.changePasswordBtn, R.id.logoutBtn})
    void onButtonsClicked(View view) {
        switch (view.getId()) {
            case R.id.ticketReturnBtn:
                break;
            case R.id.bookingHistoryBtn:
                break;
            case R.id.bonusProgramBtn:
                break;
            case R.id.paymentMethodsBtn:
                break;
            case R.id.profileSettingsBtn:
                showEditProfileFragment();
                break;
            case R.id.searchSettingsBtn:
                break;
            case R.id.changePasswordBtn:
                showChangePasswordFragment();
                break;
            case R.id.logoutBtn:
                logout();
                break;
        }
    }

    private void showEditProfileFragment() {
        if (user != null) {
            Fragment fragment = EditProfileFragment.getInstance(user);
            fragment.setTargetFragment(this, REQUEST_EDIT_PROFILE);
            ((BaseActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
        }
    }

    private void logout() {
        PrefsUtil.clearAllPrefs(getContext());
        realm.close();
        Realm.deleteRealm(realm.getConfiguration());
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showChangePasswordFragment() {
        Fragment fragment = new ChangePasswordFragment();
        fragment.setTargetFragment(this, REQUEST_CHANGE_PASSWORD);
        ((BaseActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
    }

    private void showUserInfo() {
        if (user != null) {
            mainScrollView.setVisibility(View.VISIBLE);
            fullName.setText(user.getFirstName() + " " + user.getLastName());
            email.setText(user.getEmail());
            if (!TextUtils.isEmpty(user.getPhoneNumber())) {
                phoneNumber.setText(formatPhoneNumber(user.getPhoneNumber()));
            } else {
                phoneNumber.setVisibility(View.GONE);
            }
            loadRoutesHistory();
        }
    }

    private String formatPhoneNumber(String phone) {
        StringBuilder formattedPhone = new StringBuilder("");
        if (!TextUtils.isEmpty(phone)) {
            formattedPhone.append(PhoneNumberTextInputEditText.FIRST_SIGN).
                    append(phone.substring(0, 3)).
                    append(" ").append(phone.substring(3, 5)).append(" ").
                    append(phone.substring(5, 8)).append("−").
                    append(phone.substring(8, 10)).append("−").
                    append(phone.substring(10, 12));
        }
        return formattedPhone.toString();
    }


    private void showPopularStations(List<RouteHistoryItem> items) {
        if (items.size() > 0) {
            adapter = new ProfileRouteHistoryAdapter(this);
            routeHistoryList.setNestedScrollingEnabled(false);
            routeHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
            routeHistoryList.setAdapter(adapter);
            routeHistoryList.addItemDecoration(new VerticalDividerItemDecoration(getContext(), R.drawable.profile_divider_hint_color_horizontal, padding, 0));
            adapter.setStations(items);
        } else {
            routeHistoryCard.setVisibility(View.GONE);
            routeHistoryLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (getView() != null) {
                switch (requestCode) {
                    case REQUEST_EDIT_PROFILE:
                        user = Parcels.unwrap(data.getParcelableExtra("user"));
                        showUserInfo();
                        saveUserToRealm();
                        CommonUtils.showMessage(getView(), R.string.profile_edit_successful_result);
                        break;
                    case REQUEST_CHANGE_PASSWORD:
                        Snackbar.make(getView(), R.string.profile_edit_change_password_success_result, Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        }
    }

    private void saveUserToRealm() {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }

    private Callback<User> userCallback = new Callback<User>() {

        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if (getView() != null) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    user = response.body();
                    showUserInfo();
                    saveUserToRealm();
                } else {
                    String error = ApiErrorUtil.parseError(response);
                    CommonUtils.showMessage(getView(), error);
                }
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            if (getView() != null) {
                progressBar.setVisibility(View.GONE);
                CommonUtils.showMessage(getView(), t.getMessage());
            }
        }
    };

    private Callback<List<RouteHistoryItem>> routeHistoryCallback = new Callback<List<RouteHistoryItem>>() {

        @Override
        public void onResponse(Call<List<RouteHistoryItem>> call, Response<List<RouteHistoryItem>> response) {
            if (getView() != null) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    showPopularStations(response.body());
                } else {
                    String error = ApiErrorUtil.parseError(response);
                    CommonUtils.showMessage(getView(), error);
                }
            }
        }

        @Override
        public void onFailure(Call<List<RouteHistoryItem>> call, Throwable t) {
            if (getView() != null) {
                progressBar.setVisibility(View.GONE);
                CommonUtils.showMessage(getView(), t.getMessage());
            }
        }
    };

    @Override
    public void onStationItemClick(RouteHistoryItem routeHistoryItem) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("route", Parcels.wrap(routeHistoryItem));
        startActivity(intent);
    }
}
