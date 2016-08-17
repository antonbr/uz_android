package com.uzapp.view.main.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.User;
import com.uzapp.util.PrefsUtil;
import com.uzapp.view.login.PhoneNumberTextInputEditText;
import com.uzapp.view.login.StudentIdTextInputEditText;

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
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.bonusCardNumber) TextView bonusCardNumber;
    @BindView(R.id.firstName) TextView firstName;
    @BindView(R.id.lastName) TextView lastName;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.phoneNumber) TextView phoneNumber;
    @BindView(R.id.studentId) TextView studentId;
    @BindView(R.id.myTicketsCount) TextView myTicketsCount;
    private Unbinder unbinder;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText(R.string.profile_title);
        String token = PrefsUtil.getStringPreference(getContext(), PrefsUtil.USER_ACCESS_TOKEN);
        Call call = ApiManager.getApi(getContext()).getUser(token);
        call.enqueue(userCallback);
        return view;
    }

    @OnClick(R.id.backBtn)
    void onBackBtnClicked() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.filterBtn)
    void onFilterBtnClicked() {

    }

    @OnClick(R.id.editInfoBtn)
    void onEditInfoBtnClicked() {

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
            bonusCardNumber.setText("");
            firstName.setText(user.getFirstName());
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
        if (!TextUtils.isEmpty(studentId)) {
            formattedStudentId.append(studentId.substring(0, 2)).append(StudentIdTextInputEditText.SEPARATOR)
                    .append(studentId.substring(2));
        }
        return formattedStudentId.toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Callback<User> userCallback = new Callback<User>() {

        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if (response.isSuccessful()) {
                user = response.body();
                showUserInfo();
            } else {
                Snackbar.make(getView(), response.message(), Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            Snackbar.make(getView(), t.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    };
}
