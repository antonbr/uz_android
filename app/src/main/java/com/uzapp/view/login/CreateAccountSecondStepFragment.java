package com.uzapp.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.login.CreateAccountInfo;
import com.uzapp.pojo.profile.UserTokenResponse;
import com.uzapp.network.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.util.PrefsUtil;
import com.uzapp.view.main.MainActivity;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 10.08.16.
 */
public class CreateAccountSecondStepFragment extends Fragment {
    private static final String TAG = CreateAccountSecondStepFragment.class.getName();

    private Unbinder unbinder;
    @BindView(R.id.resetBtn) Button resetBtn;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.firstNameField) TextInputEditText firstNameField;
    @BindView(R.id.lastNameField) TextInputEditText lastNameField;
    @BindView(R.id.phoneField) PhoneNumberTextInputEditText phoneField;
    @BindView(R.id.studentIdField) StudentIdTextInputEditText studentIdField;
    @BindView(R.id.saveBtn) Button saveBtn;
    @BindView(R.id.skipBtn) Button skipBtn;
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    private String email, password;
    private boolean isBonusProgramChecked;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_account_profile_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        resetBtn.setVisibility(View.GONE);
        toolbarTitle.setText(R.string.create_account_profile_info_title);
        initExtras();
        skipBtn.setEnabled(!isBonusProgramChecked);
        return view;
    }

    public static CreateAccountSecondStepFragment getInstance(String email, String password, boolean isBonusProgramChecked) {
        CreateAccountSecondStepFragment fragment = new CreateAccountSecondStepFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("password", password);
        args.putBoolean("bonus_program", isBonusProgramChecked);
        fragment.setArguments(args);
        return fragment;
    }

    private void initExtras() {
        Bundle args = getArguments();
        if (args != null) {
            email = args.getString("email");
            password = args.getString("password");
            isBonusProgramChecked = args.getBoolean("bonus_program");
        }
    }

    @OnFocusChange({R.id.firstNameField, R.id.lastNameField, R.id.phoneField, R.id.studentIdField})
    void onFieldFocusChanged(boolean focus, TextInputEditText view) {
        if (focus || view.getText().length() > 0) {
            view.setTranslationY(hintPadding);
        } else {
            view.setTranslationY(0);
        }
        if (view.getId() == R.id.phoneField) {
            String phoneText = phoneField.getText().toString();
            if (phoneText.length() == 0 || (phoneText.length() == 1 && phoneText.startsWith("+"))) {
                phoneField.setText(focus ? "+" : "");
            }
        }
    }

    @OnTextChanged(value = {R.id.firstNameField, R.id.lastNameField, R.id.studentIdField, R.id.phoneField},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onFieldsChanged(Editable editable) {
        checkFieldsState();
    }


    @OnClick({R.id.saveBtn, R.id.skipBtn})
    void onSaveBtnClicked(View view) {
        String deviceId = CommonUtils.getDeviceId(getContext());
        CreateAccountInfo.CreateAccountInfoBuilder builder = new CreateAccountInfo.CreateAccountInfoBuilder(deviceId, email);
        builder.setPassword(password);
        if (view.getId() == R.id.saveBtn) {
            if (isFirstNameValid() && firstNameField.getText().length() > 0) {
                builder.setFirstName(firstNameField.getText().toString());
            }
            if (isLastNameValid() && lastNameField.getText().length() > 0) {
                builder.setLastName(lastNameField.getText().toString());
            }
            if (phoneField.isValid() && phoneField.getText().length() > 0) {
                builder.setPhoneNumber(phoneField.getPhoneNumber());
            }
            if (studentIdField.isValid() && studentIdField.getText().length() > 0) {
                builder.setStudentId(studentIdField.getStudentId());
            }
        }
        Call call = ApiManager.getApi(getContext()).createAccount(builder.build());
        call.enqueue(callback);
    }

    private void checkFieldsState() {
        boolean allowSaving = isFirstNameValid()
                && isLastNameValid()
                && phoneField.isValid()
                && studentIdField.isValid();
        saveBtn.setEnabled(allowSaving);
    }

    private boolean isFirstNameValid() {
        if (isBonusProgramChecked) {
            return firstNameField.getText().length() >= Constants.FIRST_NAME_MIN_LENGTH;
        } else {
            return CommonUtils.isFirstNameValid(firstNameField.getText().toString());
        }
    }

    private boolean isLastNameValid() {
        if (isBonusProgramChecked) {
            return lastNameField.getText().length() >= Constants.LAST_NAME_MIN_LENGTH;
        } else {
            return CommonUtils.isLastNameValid(lastNameField.getText().toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Callback<UserTokenResponse> callback = new Callback<UserTokenResponse>() {

        @Override
        public void onResponse(Call<UserTokenResponse> call, Response<UserTokenResponse> response) {
            if (response.isSuccessful()) {
                UserTokenResponse user = response.body();
                PrefsUtil.saveUserInfo(getContext(), user.getUserId(), user.getAccessToken(), user.getRefreshToken());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                //bring back main activity from stack and start profile fragment
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("profile", true);
                startActivity(intent);
            } else {
                String error = ApiErrorUtil.getErrorMessage(response, getActivity());
                CommonUtils.showSnackbar(getView(), error);
            }
        }

        @Override
        public void onFailure(Call<UserTokenResponse> call, Throwable t) {
            if (getView() != null && t != null) {
                String error = ApiErrorUtil.getErrorMessage(t, getActivity());
                CommonUtils.showSnackbar(getView(), error);
            }
        }
    };

    private void showError(String message) {
        Log.d(TAG, message);
        CommonUtils.showSnackbar(getView(), message);
    }


}