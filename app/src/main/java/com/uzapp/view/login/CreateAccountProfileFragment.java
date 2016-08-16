package com.uzapp.view.login;

import android.os.Bundle;
import android.provider.Settings;
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
import com.uzapp.pojo.CreateAccountInfo;
import com.uzapp.pojo.UserTokenResponse;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.util.PrefsUtil;

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
public class CreateAccountProfileFragment extends Fragment {
    private static final String TAG = CreateAccountProfileFragment.class.getName();

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

    public static CreateAccountProfileFragment getInstance(String email, String password, boolean isBonusProgramChecked) {
        CreateAccountProfileFragment fragment = new CreateAccountProfileFragment();
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
        String deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        CreateAccountInfo createAccountInfo = new CreateAccountInfo(
                deviceId,
                email,
                password);
        if (view.getId() == R.id.saveBtn) {
            if (isFirstNameValid() && firstNameField.getText().length() > 0) {
                createAccountInfo.setFirstName(firstNameField.getText().toString());
            }
            if (isLastNameValid() && lastNameField.getText().length() > 0) {
                createAccountInfo.setLastName(lastNameField.getText().toString());
            }
            if (phoneField.isValid() && phoneField.getText().length() > 0) {
                createAccountInfo.setPhoneNumber(phoneField.getPhoneNumber());
            }
            if (studentIdField.isValid() && studentIdField.getText().length() > 0) {
                createAccountInfo.setStudentId(studentIdField.getStudentId());
            }
        }
        Call call = ApiManager.getApi(getContext()).createAccount(createAccountInfo);
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
            return firstNameField.getText().length() > Constants.FIRST_NAME_MIN_LENGTH;
        } else {
            return firstNameField.getText().length() == 0 || firstNameField.getText().length() > Constants.FIRST_NAME_MIN_LENGTH;
        }
    }

    private boolean isLastNameValid() {
        if (isBonusProgramChecked) {
            return lastNameField.getText().length() > Constants.LAST_NAME_MIN_LENGTH;
        } else {
            return lastNameField.getText().length() == 0 || lastNameField.getText().length() > Constants.LAST_NAME_MIN_LENGTH;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //[text={"status":"user already exists"}]
    private Callback<UserTokenResponse> callback = new Callback<UserTokenResponse>() {

        @Override
        public void onResponse(Call<UserTokenResponse> call, Response<UserTokenResponse> response) {
            if (response.isSuccessful()) {
                CommonUtils.showMessage(getView(), "Account created successfully! Profile page is not yet implemented");
                UserTokenResponse user = response.body();
                PrefsUtil.saveUserInfo(getContext(), user.getUserId(), user.getAccessToken(), user.getRefreshToken());
            } else {
                showError(response.message());
            }
        }

        @Override
        public void onFailure(Call<UserTokenResponse> call, Throwable t) {
            if (getView() != null && t != null) {
                showError(t.getMessage());
            }
        }
    };

    private void showError(String message) {
        Log.d(TAG, message);
        CommonUtils.showMessage(getView(), message);
    }


}