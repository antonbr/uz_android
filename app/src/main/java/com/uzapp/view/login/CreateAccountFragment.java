package com.uzapp.view.login;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.BaseActivity;
import com.uzapp.view.main.search.CheckableImageView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

/**
 * Created by vika on 09.08.16.
 */
public class CreateAccountFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.resetBtn) Button resetBtn;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.emailField) TextInputEditText emailField;
    @BindView(R.id.emailLayout) TextInputLayout emailLayout;
    @BindView(R.id.passwordField) TextInputEditText passwordField;
    @BindView(R.id.passwordLayout) TextInputLayout passwordLayout;
    @BindView(R.id.showPasswordBtn) CheckableImageView showPasswordBtn;
    @BindView(R.id.termsOfServiceChb) CheckBox termsOfServiceChb;
    @BindView(R.id.bonusProgramChb) CheckBox bonusProgramChb;
    @BindView(R.id.registerBtn) Button registerBtn;
    @BindDimen(R.dimen.hint_padding) int hintPadding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_account_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        resetBtn.setVisibility(View.GONE);
        toolbarTitle.setText(R.string.create_account_title);
        emailLayout.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        passwordLayout.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        setClickableTextInCheckbox(termsOfServiceChb, R.string.create_account_terms_of_service,
                R.string.create_account_terms_of_service_clickable, R.string.terms_of_service);
        setClickableTextInCheckbox(bonusProgramChb, R.string.create_account_bonus_program,
                R.string.create_account_bonus_program_clickable, R.string.bonus_program);
        return view;
    }

    private void setClickableTextInCheckbox(CheckBox checkBox, int textRes, int clickableTextRes, final int urlRes) {
        SpannableString checkboxText = new SpannableString(getString(textRes));
        String clickableText = getString(clickableTextRes);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Fragment fragment = WebFragment.getInstance(getString(urlRes));
                ((BaseActivity) getActivity()).replaceFragment(fragment, true);
            }
        };
        int start = checkboxText.toString().indexOf(clickableText);
        int end = start + clickableText.length();
        checkboxText.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkBox.setText(checkboxText);
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());
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

    @OnFocusChange(R.id.passwordField)
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

    @OnTextChanged({R.id.emailField, R.id.passwordField})
    void onFieldsChanged(Editable editable) {
        checkFieldState();
    }

    @OnCheckedChanged(R.id.termsOfServiceChb)
    void onCheckboxesStateChanged() {
        checkFieldState();
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

    @OnClick(R.id.showPasswordBtn)
    void onShowPasswordBtnClicked(CheckableImageView view) {
        view.toggle();
        passwordField.setInputType(view.isChecked() ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setLetterSpacing(view.isChecked());
        passwordField.setSelection(passwordField.length());
    }

    @OnClick(R.id.registerBtn)
    void onRegisterBtnClicked() {
        CreateAccountProfileFragment fragment = CreateAccountProfileFragment.getInstance(emailField.getText().toString(),
                passwordField.getText().toString(), bonusProgramChb.isChecked());
        ((BaseActivity) getActivity()).replaceFragment(fragment, true);
    }

    private void checkFieldState() {
        boolean allowRegistration = CommonUtils.isEmailValid(emailField.getText().toString())
                && CommonUtils.isPasswordValid(passwordField.getText().toString())
                && termsOfServiceChb.isChecked();
        registerBtn.setEnabled(allowRegistration);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
