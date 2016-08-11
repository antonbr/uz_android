package com.uzapp.view.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uzapp.R;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

/**
 * Created by vika on 10.08.16.
 */
public class CreateAccountProfileFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.resetBtn) Button resetBtn;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.firstNameField) TextInputEditText firstNameField;
    @BindView(R.id.lastNameField) TextInputEditText lastNameField;
    @BindView(R.id.phoneField) TextInputEditText phoneField;
    @BindView(R.id.studentIdField) TextInputEditText studentIdField;
    @BindView(R.id.saveBtn) Button saveBtn;
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    @BindInt(R.integer.phone_number_length) int phoneNumberLength;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_account_profile_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        resetBtn.setVisibility(View.GONE);
        toolbarTitle.setText(R.string.create_account_profile_info_title);
        return view;
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

    @OnTextChanged(value = {R.id.firstNameField, R.id.lastNameField},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onFieldsChanged(Editable editable) {
        checkFieldsState();
    }

    private boolean phoneChangedFromCode = false;


    @OnClick(R.id.saveBtn)
    void onSaveBtnClicked() {

    }

    private void checkFieldsState() {
        boolean allowSaving = !TextUtils.isEmpty(firstNameField.getText())
                && !TextUtils.isEmpty(lastNameField.getText())
                && phoneField.getText().length() == phoneNumberLength;
        saveBtn.setEnabled(allowSaving);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnTextChanged(value = R.id.phoneField,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onPhoneTextChanged(Editable editable) {
        if (!phoneChangedFromCode) {
            String oldText = editable.toString();
            int cursorPosition = phoneField.getSelectionEnd();
            boolean isCursorAtTheEnd = cursorPosition == oldText.length();

            if (!isCursorAtTheEnd) {
                int index = oldText.indexOf(" ");
                while (index >= 0) {
                    if (index <= cursorPosition) {
                        cursorPosition--;
                    }
                    index = oldText.indexOf(" ", index + 1);
                }
            }
            StringBuilder allText = new StringBuilder(oldText.replaceAll(" ", ""));
            if (allText.length() == 0 && !phoneField.isFocused()) return;

            if (allText.toString().contains("+") && allText.charAt(0) != '+') {
                allText.delete(0, allText.indexOf("+"));
            }
            if (!allText.toString().contains("+")) {
                allText.insert(0, "+");
                cursorPosition++;
            }

            if (allText.length() > 4) {
                allText.insert(4, " ");
                if (cursorPosition > 4) {
                    cursorPosition++;
                }
            }
            if (allText.length() > 7) {
                allText.insert(7, " ");
                if (cursorPosition > 7) {
                    cursorPosition++;
                }
            }
            if (allText.length() > 11) {
                allText.insert(11, " ");
                if (cursorPosition > 11) {
                    cursorPosition++;
                }
            }
            if (allText.length() > 14) {
                allText.insert(14, " ");
                if (cursorPosition > 14) {
                    cursorPosition++;
                }
            }
            if (!oldText.equals(allText.toString())) {
                phoneChangedFromCode = true;
                phoneField.setText(allText);
                phoneField.setSelection(isCursorAtTheEnd ? allText.length() : cursorPosition);
            }
            checkFieldsState();
        } else {
            phoneChangedFromCode = false;
        }
    }

}

//    @OnTextChanged(value = R.id.studentIdField,
//            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
//    void onStudentIdTextChanged(Editable editable) {
//        if (!isUpdatingStudentIdFromCode) {
//            boolean needUpdateText = false;
//            int cursorPosition = studentIdField.getSelectionEnd();
//            String stringText = editable.toString().replaceAll(" ", "");
//            StringBuilder text = new StringBuilder(stringText);
//            for (int i = 0; i < 2; i++) {
//                if (text.length() > i && !Character.isLetter(text.charAt(i))) {
//                    text.deleteCharAt(i);
//                    needUpdateText = true;
//                    cursorPosition = i + 1;
//                }
//            }
//            if (text.length() > 2 && text.charAt(2) != Character.SPACE_SEPARATOR) {
//                text.insert(2, " ");
//                needUpdateText = true;
//            }
//            if (needUpdateText) {
//                isUpdatingStudentIdFromCode = true;
//                studentIdField.setText(text);
//                studentIdField.setSelection(cursorPosition);
//            }
//        }
//        isUpdatingStudentIdFromCode = false;
//    }