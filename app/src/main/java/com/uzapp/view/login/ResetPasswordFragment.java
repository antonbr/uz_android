package com.uzapp.view.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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
    @BindView(R.id.emailField) EditText emailField;
    @BindView(R.id.newPasswordField) EditText passwordField;
    @BindView(R.id.showPasswordBtn) CheckableImageView showPasswordBtn;
    @BindView(R.id.resetPasswordBtn) Button resetPasswordBtn;
    @BindView(R.id.resetPasswordLayout) ViewGroup resetPasswordLayout;
    @BindView(R.id.successResultLayout) ViewGroup successResultLayout;
    @BindView(R.id.toolbar) AppBarLayout toolbar;
    @BindDimen(R.dimen.hint_padding) int hintPadding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reset_password_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        setPasswordLetterSpacing(false);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideNavigationBar();
        }
        return view;
    }

    @OnClick(R.id.showPasswordBtn)
    void onShowPasswordBtnClicked(CheckableImageView view) {
        view.toggle();
        passwordField.setInputType(view.isChecked() ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setPasswordLetterSpacing(view.isChecked());
        passwordField.setSelection(passwordField.length());
    }

    @OnTextChanged({R.id.emailField, R.id.newPasswordField})
    void onFieldsChanged(Editable editable) {
        checkFieldState();
    }

    @OnClick(R.id.closeBtn)
    void onBackBtnPressed() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.resetPasswordBtn)
    void onResetPasswordBtnClicked() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(resetPasswordBtn.getWindowToken(), 0);
        Call<Object> call = ApiManager.getApi(getActivity()).restorePassword(emailField.getText().toString(), passwordField.getText().toString());
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

    private void setPasswordLetterSpacing(boolean isBigSpacing) {
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
