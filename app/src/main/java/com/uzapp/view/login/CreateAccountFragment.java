package com.uzapp.view.login;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.view.main.search.CheckableImageView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.Unbinder;

/**
 * Created by vika on 09.08.16.
 */
public class CreateAccountFragment extends Fragment {
    private static final float PASSWORD_INVISIBLE_LETTER_SPACING = 0.5f;
    private static final float PASSWORD_VISIBLE_LETTER_SPACING = 1f;
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

    private void setLetterSpacing(boolean isBigSpacing) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //TODO fix left padding
            passwordField.setLetterSpacing(isBigSpacing ? PASSWORD_VISIBLE_LETTER_SPACING : PASSWORD_INVISIBLE_LETTER_SPACING);
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
//TODO add validation
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
