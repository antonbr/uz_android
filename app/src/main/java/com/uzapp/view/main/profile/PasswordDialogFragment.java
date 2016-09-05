package com.uzapp.view.main.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.main.search.CheckableImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

/**
 * Created by vika on 18.08.16.
 */
public class PasswordDialogFragment extends Fragment {
    @BindView(R.id.passwordField) EditText passwordField;
    @BindView(R.id.saveBtn) Button saveBtn;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_edit_password_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.closeBtn)
    void onBackBtnClicked() {
        getActivity().onBackPressed();
    }

    @OnTextChanged(R.id.passwordField)
    void onTextChanged() {
        saveBtn.setEnabled(CommonUtils.isPasswordValid(passwordField.getText().toString()));
    }

    @OnClick(R.id.saveBtn)
    void onSaveBtnClicked() {
        Intent intent = new Intent();
        intent.putExtra("password", passwordField.getText().toString());
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    @OnClick(R.id.showPasswordBtn)
    void onShowPasswordBtnClicked(CheckableImageView view) {
        if(passwordField.getText().length()>0) {
            view.toggle();
            passwordField.setInputType(view.isChecked() ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            setLetterSpacing(view.isChecked());
            passwordField.setSelection(passwordField.length());
        }
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
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
        unbinder.unbind();
    }
}

