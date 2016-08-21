package com.uzapp.view.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.util.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.search.CheckableImageView;

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
 * Created by vika on 14.08.16.
 */
public class ResetPasswordFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.emailField) TextInputEditText emailField;
    @BindView(R.id.emailLayout) TextInputLayout emailLayout;
    @BindView(R.id.newPasswordField) TextInputEditText passwordField;
    @BindView(R.id.newPasswordLayout) TextInputLayout passwordLayout;
    @BindView(R.id.showPasswordBtn) CheckableImageView showPasswordBtn;
    @BindView(R.id.resetPasswordBtn) Button resetPasswordBtn;
    @BindView(R.id.resetPasswordLayout) ViewGroup resetPasswordLayout;
    @BindView(R.id.successResultLayout) ViewGroup successResultLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindDimen(R.dimen.hint_padding) int hintPadding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reset_password_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText(R.string.reset_passwword_title);
        emailLayout.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        passwordLayout.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        return view;
    }

    @OnFocusChange(R.id.emailField)
    void onEmailFieldFocusChanged(boolean focus) {
        if (focus || emailField.getText().length() > 0) {
            emailLayout.setHint(getString(R.string.create_account_email_hint));
            emailField.setTranslationY(hintPadding);
        } else {
            emailLayout.setHint(getString(R.string.create_account_email_hint_2));
            emailField.setTranslationY(0);
        }
    }

    @OnFocusChange(R.id.newPasswordField)
    void onPasswordFieldFocusChanged(boolean focus) {
        if (focus || passwordField.getText().length() > 0) {
            passwordLayout.setHint(getString(R.string.create_account_password_hint));
            passwordField.setTranslationY(hintPadding);
            showPasswordBtn.setVisibility(View.VISIBLE);
            setLetterSpacing(showPasswordBtn.isChecked());
        } else {
            passwordLayout.setHint(getString(R.string.create_account_password_hint_2));
            passwordField.setTranslationY(0);
            showPasswordBtn.setVisibility(View.GONE);
            setLetterSpacing(false);
        }
    }

    @OnClick(R.id.showPasswordBtn)
    void onShowPasswordBtnClicked(CheckableImageView view) {
        view.toggle();
        passwordField.setInputType(view.isChecked() ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setLetterSpacing(view.isChecked());
        passwordField.setSelection(passwordField.length());
    }

    @OnTextChanged({R.id.emailField, R.id.newPasswordField})
    void onFieldsChanged(Editable editable) {
        checkFieldState();
    }

    @OnClick(R.id.backBtn)
    void onBackBtnPressed() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.resetPasswordBtn)
    void onResetPasswordBtnClicked() {
        Call call = ApiManager.getApi(getActivity()).restorePassword(emailField.getText().toString(), passwordField.getText().toString());
        call.enqueue(callback);
    }

    @OnClick(R.id.searchScreenBtn)
    void onSearchScreenBtnClicked() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void checkFieldState() {
        boolean allowRegistration = CommonUtils.isEmailValid(emailField.getText().toString())
                && CommonUtils.isPasswordValid(passwordField.getText().toString());
        resetPasswordBtn.setEnabled(allowRegistration);

    }

    private void setLetterSpacing(boolean isBigSpacing) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //TODO fix left padding
            passwordField.setLetterSpacing(isBigSpacing ? Constants.PASSWORD_VISIBLE_LETTER_SPACING :
                    Constants.PASSWORD_INVISIBLE_LETTER_SPACING);
            //typeface is lost after setting letter spacing
            passwordField.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Callback<Object> callback = new Callback<Object>() {

        @Override
        public void onResponse(Call<Object> call, Response<Object> response) {
            if (getView() != null) {
                if (response.isSuccessful()) {
                    resetPasswordLayout.setVisibility(View.GONE);
                    successResultLayout.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.INVISIBLE);
                } else {
                    String error = ApiErrorUtil.parseError(response);
                    CommonUtils.showMessage(getView(), error);
                }
            }
        }

        @Override
        public void onFailure(Call<Object> call, Throwable t) {
            if (getView() != null && t != null) {
                CommonUtils.showMessage(getView(), t.getMessage());
            }
        }
    };
}
