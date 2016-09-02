package com.uzapp.view.main.profile;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.User;
import com.uzapp.util.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.BaseActivity;
import com.uzapp.view.login.ResetPasswordFragment;
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
 * Created by vika on 19.08.16.
 */
public class ChangePasswordFragment extends Fragment {
    @BindView(R.id.passwordField) TextInputEditText passwordField;
    @BindView(R.id.newPasswordField) TextInputEditText newPasswordField;
    @BindView(R.id.confirmNewPasswordField) TextInputEditText confirmNewPasswordField;
    @BindView(R.id.passwordLayout) TextInputLayout passwordLayout;
    @BindView(R.id.newPasswordLayout) TextInputLayout newPasswordLayout;
    @BindView(R.id.confirmNewPasswordLayout) TextInputLayout confirmNewPasswordLayout;
    @BindView(R.id.showPasswordBtn) CheckableImageView showPasswordBtn;
    @BindView(R.id.showNewPasswordBtn) CheckableImageView showNewPasswordBtn;
    @BindView(R.id.showConfirmNewPasswordBtn) CheckableImageView showConfirmNewPasswordBtn;
    @BindView(R.id.saveBtn) Button saveBtn;
    @BindView(R.id.cancelBtn) Button cancelBtn;
    @BindView(R.id.changePasswordLayout) ViewGroup changePasswordLayout;
    @BindView(R.id.successResultLayout) ViewGroup successResultLayout;
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_change_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        passwordLayout.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        newPasswordLayout.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        confirmNewPasswordLayout.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        ((MainActivity) getActivity()).hideNavigationBar();
        return view;
    }

    @OnClick(R.id.cancelBtn)
    void onCancelBtnClicked() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.saveBtn)
    void onSaveBtnClicked() {
        Call call = ApiManager.getApi(getContext()).changePassword(passwordField.getText().toString(),
                newPasswordField.getText().toString());
        call.enqueue(changePasswordCallback);
    }

    @OnClick(R.id.forgotPasswordBtn)
    void onForgotPasswordBtnClicked() {
        ((BaseActivity) getActivity()).replaceFragment(new ResetPasswordFragment(), true);
    }

    @OnClick(R.id.profileScreenBtn)
    void onProfileScreenBtnClicked() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ProfileFragment profileFragment = (ProfileFragment) fragmentManager.findFragmentByTag(ProfileFragment.class.getName());
        if (profileFragment != null) {
            boolean fragmentPopped = fragmentManager.popBackStackImmediate(ProfileFragment.class.getName(), 0);
            if (!fragmentPopped) {
                ((BaseActivity) getActivity()).replaceFragment(new ProfileFragment(), true);
            }
        }
    }

    @OnFocusChange({R.id.passwordField, R.id.newPasswordField, R.id.confirmNewPasswordField})
    void onPasswordFieldFocusChanged(EditText view, boolean focus) {
        CheckableImageView button = null;
        switch (view.getId()) {
            case R.id.passwordField:
                button = showPasswordBtn;
                break;
            case R.id.newPasswordField:
                button = showNewPasswordBtn;
                break;
            case R.id.confirmNewPasswordField:
                button = showConfirmNewPasswordBtn;
                break;
        }
        if (button != null) {
            if (focus || passwordField.getText().length() > 0) {
                view.setTranslationY(hintPadding);
                button.setVisibility(View.VISIBLE);
                setLetterSpacing(view, showPasswordBtn.isChecked());
            } else {
                view.setTranslationY(0);
                button.setVisibility(View.GONE);
                setLetterSpacing(view, false);
            }
        }
    }

    @OnClick({R.id.showPasswordBtn, R.id.showNewPasswordBtn, R.id.showConfirmNewPasswordBtn})
    void onShowPasswordBtnClicked(CheckableImageView view) {
        EditText editText = null;
        switch (view.getId()) {
            case R.id.showPasswordBtn:
                editText = passwordField;
                break;
            case R.id.showNewPasswordBtn:
                editText = newPasswordField;
                break;
            case R.id.showConfirmNewPasswordBtn:
                editText = confirmNewPasswordField;
                break;
        }
        view.toggle();
        if (editText != null) {
            editText.setInputType(view.isChecked() ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            setLetterSpacing(editText, view.isChecked());
            editText.setSelection(editText.length());
        }
    }

    @OnTextChanged({R.id.passwordField, R.id.newPasswordField, R.id.confirmNewPasswordField})
    void onFieldsChanged(Editable editable) {
        checkFieldState();
    }

    private void checkFieldState() {
        boolean allowSaving = CommonUtils.isPasswordValid(passwordField.getText().toString())
                && CommonUtils.isPasswordValid(newPasswordField.getText().toString())
                && newPasswordField.getText().toString().equals(confirmNewPasswordField.getText().toString());
        saveBtn.setEnabled(allowSaving);
    }

    private void setLetterSpacing(EditText editText, boolean isBigSpacing) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //TODO fix left padding
            editText.setLetterSpacing(isBigSpacing ? Constants.PASSWORD_VISIBLE_LETTER_SPACING :
                    Constants.PASSWORD_INVISIBLE_LETTER_SPACING);
            //typeface is lost after setting letter spacing
            editText.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        ((MainActivity) getActivity()).showNavigationBar();
    }

    private Callback<User> changePasswordCallback = new Callback<User>() {

        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if (getView() != null) {
                if (response.isSuccessful()) {
//                    Fragment targetFragment = getTargetFragment();
//                    if (targetFragment != null) {
//                        Intent i = new Intent();
//                        targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
//                    }
                    // getActivity().onBackPressed();

                    changePasswordLayout.setVisibility(View.GONE);
                    successResultLayout.setVisibility(View.VISIBLE);
                    saveBtn.setVisibility(View.INVISIBLE);
                    cancelBtn.setVisibility(View.INVISIBLE);
                } else {
                    String error = ApiErrorUtil.parseError(response);
                    CommonUtils.showMessage(getView(), error);
                }
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            if (getView() != null) {
                Snackbar.make(getView(), t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        }
    };
}
