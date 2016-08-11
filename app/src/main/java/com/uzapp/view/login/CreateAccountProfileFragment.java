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
import butterknife.BindInt;
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
    @BindView(R.id.phoneField) TextInputEditText phoneField;
    @BindView(R.id.studentIdField) TextInputEditText studentIdField;
    @BindView(R.id.saveBtn) Button saveBtn;
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    @BindInt(R.integer.phone_number_length) int phoneNumberLength;
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
        email = args.getString("email");
        password = args.getString("password");
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
        String deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        CreateAccountInfo createAccountInfo = new CreateAccountInfo(
                deviceId,
                firstNameField.getText().toString(),
                lastNameField.getText().toString(),
                email,
                password);
        if (phoneField.getText().length() == phoneNumberLength) {
            createAccountInfo.setPhoneNumber(phoneField.getText().toString().replaceAll(" ", "").replaceAll("\\+", ""));
        }
        //TODO student id
        Call call = ApiManager.getApi(getContext()).createAccount(createAccountInfo);
        call.enqueue(callback);
    }

    private void checkFieldsState() {
        boolean allowSaving = !TextUtils.isEmpty(firstNameField.getText())
                && !TextUtils.isEmpty(lastNameField.getText())
                && (phoneField.getText().length() == 0 ||
                phoneField.getText().length() == 1 ||
                phoneField.getText().length() == phoneNumberLength);
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