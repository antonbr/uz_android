package com.uzapp.view.main.profile;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.User;
import com.uzapp.network.ApiErrorUtil;
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
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 19.08.16.
 */
public class ChangePasswordFragment extends Fragment {
    private static final int REQUEST_RESET_PASSWORD = 1;
    @BindView(R.id.passwordField) EditText passwordField;
    @BindView(R.id.newPasswordField) EditText newPasswordField;
    @BindView(R.id.showPasswordBtn) CheckableImageView showPasswordBtn;
    @BindView(R.id.showNewPasswordBtn) CheckableImageView showNewPasswordBtn;
    @BindView(R.id.changePasswordBtn) Button changePasswordBtn;
    @BindView(R.id.closeBtn) ImageButton closeBtn;
    @BindView(R.id.changePasswordLayout) ViewGroup changePasswordLayout;
    @BindView(R.id.successResultLayout) ViewGroup successResultLayout;
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_change_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).hideNavigationBar();
        setLetterSpacing(passwordField, false);
        setLetterSpacing(newPasswordField, false);
        return view;
    }

    @OnClick(R.id.closeBtn)
    void onCancelBtnClicked() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.changePasswordBtn)
    void onSaveBtnClicked() {
        ((BaseActivity)getActivity()).hideKeyboard(this);
        Call<User> call = ApiManager.getApi(getContext()).changePassword(passwordField.getText().toString(),
                newPasswordField.getText().toString());
        call.enqueue(changePasswordCallback);
    }

    @OnClick(R.id.forgotPasswordBtn)
    void onForgotPasswordBtnClicked() {
        //((BaseActivity) getActivity()).replaceFragment(new ResetPasswordFragment(), true);
            Fragment fragment = new ResetPasswordFragment();
            fragment.setTargetFragment(this, REQUEST_RESET_PASSWORD);
            ((BaseActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
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


    @OnClick({R.id.showPasswordBtn, R.id.showNewPasswordBtn})
    void onShowPasswordBtnClicked(CheckableImageView view) {
        EditText editText = null;
        switch (view.getId()) {
            case R.id.showPasswordBtn:
                editText = passwordField;
                break;
            case R.id.showNewPasswordBtn:
                editText = newPasswordField;
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

    @OnTextChanged({R.id.passwordField, R.id.newPasswordField})
    void onFieldsChanged(Editable editable) {
        checkFieldState();
    }

    private void checkFieldState() {
        boolean allowSaving = CommonUtils.isPasswordValid(passwordField.getText().toString())
                && CommonUtils.isPasswordValid(newPasswordField.getText().toString());
        changePasswordBtn.setEnabled(allowSaving);
    }

    private void setLetterSpacing(EditText editText, boolean isBigSpacing) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                    changePasswordLayout.setVisibility(View.GONE);
                    successResultLayout.setVisibility(View.VISIBLE);
                    changePasswordBtn.setVisibility(View.INVISIBLE);
                    closeBtn.setVisibility(View.INVISIBLE);
                } else {
                    String error = ApiErrorUtil.getErrorMessage(response, getActivity());
                    CommonUtils.showSnackbar(getView(), error);
                }
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            if (getView() != null) {
                String error = ApiErrorUtil.getErrorMessage(t, getActivity());
                CommonUtils.showSnackbar(getView(), error);
            }
        }
    };
}
