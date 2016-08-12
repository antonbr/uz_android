package com.uzapp.view.login;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
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
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    private String email, password;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_account_profile_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        resetBtn.setVisibility(View.GONE);
        toolbarTitle.setText(R.string.create_account_profile_info_title);
        initExtras();
        return view;
    }

    public static CreateAccountProfileFragment getInstance(String email, String password) {
        CreateAccountProfileFragment fragment = new CreateAccountProfileFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("password", password);
        fragment.setArguments(args);
        return fragment;
    }

    private void initExtras() {
        Bundle args = getArguments();
        if (args != null) {
            email = args.getString("email");
            password = args.getString("password");
        }
    }


    @OnFocusChange({R.id.firstNameField, R.id.lastNameField, R.id.phoneField, R.id.studentIdField})
    void onEmailFieldFocusChanged(boolean focus, TextInputEditText view) {
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


    @OnClick(R.id.saveBtn)
    void onSaveBtnClicked() {
        String deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        CreateAccountInfo createAccountInfo = new CreateAccountInfo(
                deviceId,
                firstNameField.getText().toString(),
                lastNameField.getText().toString(),
                email,
                password);
        if (phoneField.isValid() && phoneField.getText().length() > 0) {
            createAccountInfo.setPhoneNumber(phoneField.getPhoneNumber());
        }
        if (studentIdField.isValid() && studentIdField.getText().length() > 0) {
            createAccountInfo.setStudentId(studentIdField.getStudentId());
        }
        Call call = ApiManager.getApi(getContext()).createAccount(createAccountInfo);
        call.enqueue(callback);
    }

    private void checkFieldsState() {
        boolean allowSaving = !TextUtils.isEmpty(firstNameField.getText())
                && !TextUtils.isEmpty(lastNameField.getText())
                && phoneField.isValid()
                && studentIdField.isValid();
        saveBtn.setEnabled(allowSaving);
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
                Snackbar.make(getView(), "Yohooo!!!", Snackbar.LENGTH_SHORT).show();
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
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }


}