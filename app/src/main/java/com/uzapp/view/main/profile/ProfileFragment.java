package com.uzapp.view.main.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.User;
import com.uzapp.util.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.PrefsUtil;
import com.uzapp.view.BaseActivity;
import com.uzapp.view.login.PhoneNumberTextInputEditText;
import com.uzapp.view.login.StudentIdTextInputEditText;
import com.uzapp.view.main.MainActivity;

import org.parceler.Parcels;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 17.08.16.
 */
public class ProfileFragment extends Fragment {
    private static final int REQUEST_EDIT_PROFILE = 1;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.bonusCardNumber) TextView bonusCardNumber;
    @BindView(R.id.firstName) TextView firstName;
    @BindView(R.id.middleName) TextView middleName;
    @BindView(R.id.lastName) TextView lastName;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.phoneNumber) TextView phoneNumber;
    @BindView(R.id.studentId) TextView studentId;
    @BindView(R.id.myTicketsCount) TextView myTicketsCount;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.mainScrollView) ScrollView mainScrollView;
    @BindInt(R.integer.student_id_full_length) int studentIdLength;
    private Unbinder unbinder;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        progressBar.setVisibility(View.VISIBLE);
        mainScrollView.setVisibility(View.GONE);
        Call call = ApiManager.getApi(getContext()).getUser();
        call.enqueue(userCallback);
        ((MainActivity) getActivity()).showNavigationBar();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).selectNoneItemsInNavBar();
    }

    @OnClick(R.id.backBtn)
    void onBackBtnClicked() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.filterBtn)
    void onFilterBtnClicked() {
        PrefsUtil.clearAllPrefs(getContext());
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.editInfoBtn)
    void onEditInfoBtnClicked() {
        if (user != null) {
            Fragment fragment = EditProfileFragment.getInstance(user);
            fragment.setTargetFragment(this, REQUEST_EDIT_PROFILE);
            ((BaseActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
            //TODO check navigation and animation
        }
    }

    @OnClick(R.id.addCardBtn)
    void onAddCardBtnClicked() {
    }

    @OnClick({R.id.myTicketsBtn, R.id.ticketReturnBtn, R.id.bookingHistoryBtn, R.id.bonusProgramBtn})
    void onButtonsClicked(View view) {
        switch (view.getId()) {
            case R.id.myTicketsBtn:
                break;
            case R.id.ticketReturnBtn:
                break;
            case R.id.bookingHistoryBtn:
                break;
            case R.id.bonusProgramBtn:
                break;
        }
    }

    private void showUserInfo() {
        if (user != null) {
            mainScrollView.setVisibility(View.VISIBLE);
            bonusCardNumber.setText("");
            firstName.setText(user.getFirstName());
            middleName.setText(user.getMiddleName());
            lastName.setText(user.getLastName());
            email.setText(user.getEmail());
            phoneNumber.setText(formatPhoneNumber(user.getPhoneNumber()));
            studentId.setText(formatStudentId(user.getStudentId()));
        }
    }

    private String formatPhoneNumber(String phone) {
        StringBuilder formattedPhone = new StringBuilder("");
        if (!TextUtils.isEmpty(phone)) {
            formattedPhone.append(PhoneNumberTextInputEditText.FIRST_SIGN).
                    append(phone.substring(0, 3)).
                    append(" (").append(phone.substring(3, 5)).append(") ").
                    append(phone.substring(5, 8)).append("−").
                    append(phone.substring(8, 10)).append("−").
                    append(phone.substring(10, 12));
        }
        return formattedPhone.toString();
    }

    private String formatStudentId(String studentId) {
        StringBuilder formattedStudentId = new StringBuilder("");
        if (!TextUtils.isEmpty(studentId) && studentId.length() == studentIdLength) {
            formattedStudentId.append(studentId.substring(0, 2)).append(StudentIdTextInputEditText.SEPARATOR)
                    .append(studentId.substring(2));
        }
        return formattedStudentId.toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        ((MainActivity) getActivity()).hideNavigationBar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_EDIT_PROFILE) {
                user = Parcels.unwrap(data.getParcelableExtra("user"));
                showUserInfo();
                Snackbar.make(getView(), R.string.profile_edit_successful_result, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private Callback<User> userCallback = new Callback<User>() {

        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if (getView() != null) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    user = response.body();
                    showUserInfo();
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
                Snackbar.make(getView(), t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        }
    };
}
