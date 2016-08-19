package com.uzapp.view.main.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.User;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.util.PrefsUtil;
import com.uzapp.view.BaseActivity;
import com.uzapp.view.login.PhoneNumberTextInputEditText;
import com.uzapp.view.login.StudentIdTextInputEditText;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 18.08.16.
 */
public class EditProfileFragment extends Fragment {
    private static final int REQUEST_PASSWORD = 1;
    private static final int REQUEST_CHANGE_PASSWORD = 2;
    @BindView(R.id.firstNameField) TextInputEditText firstNameField;
    @BindView(R.id.lastNameField) TextInputEditText lastNameField;
    @BindView(R.id.middleNameField) TextInputEditText middleNameField;
    @BindView(R.id.phoneField) PhoneNumberTextInputEditText phoneField;
    @BindView(R.id.emailField) TextInputEditText emailField;
    @BindView(R.id.studentIdField) StudentIdTextInputEditText studentIdField;
    @BindView(R.id.saveBtn) Button saveBtn;
    private Unbinder unbinder;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_edit_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        user = Parcels.unwrap(getArguments().getParcelable("user"));
        initViews();
        return view;
    }

    public static EditProfileFragment getInstance(User user) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", Parcels.wrap(user));
        fragment.setArguments(args);
        return fragment;
    }

    private void initViews() {
        if (user != null) {
            firstNameField.setText(user.getFirstName());
            middleNameField.setText(user.getMiddleName());
            lastNameField.setText(user.getLastName());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhoneNumber());
            studentIdField.setText(user.getStudentId());
        }
    }

    @OnClick(R.id.backBtn)
    void onBackBtnClicked() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.saveBtn)
    void onSaveBtnClicked() {
        DialogFragment fragment = new PasswordDialogFragment();
        fragment.setTargetFragment(this, REQUEST_PASSWORD);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
    }

    @OnTextChanged(value = {R.id.firstNameField, R.id.middleNameField, R.id.lastNameField, R.id.phoneField,
            R.id.emailField, R.id.studentIdField},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onFieldsChanged(Editable editable) {
        checkFieldsState();
    }

    @OnClick(R.id.passwordBtn)
    void onPasswordBtnClicked() {
        Fragment fragment = new ChangePasswordFragment();
        fragment.setTargetFragment(this, REQUEST_CHANGE_PASSWORD);
        ((BaseActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
    }

    private void checkFieldsState() {
        boolean allowSaving = isFirstNameValid()
                && isMiddleNameValid()
                && isLastNameValid()
                && phoneField.isValid()
                && CommonUtils.isEmailValid(emailField.getText().toString())
                && studentIdField.isValid();
        saveBtn.setEnabled(allowSaving);
    }

    private boolean isFirstNameValid() {
        return firstNameField.getText().length() == 0 || firstNameField.getText().length() > Constants.FIRST_NAME_MIN_LENGTH;
    }

    private boolean isLastNameValid() {
        return lastNameField.getText().length() == 0 || lastNameField.getText().length() > Constants.LAST_NAME_MIN_LENGTH;
    }

    private boolean isMiddleNameValid() {
        return middleNameField.getText().length() == 0 || middleNameField.getText().length() > Constants.LAST_NAME_MIN_LENGTH;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (getView() != null) {
                switch (requestCode) {
                    case REQUEST_PASSWORD:
                        String password = data.getStringExtra("password");
                        updateUserData(password);
                        break;
                    case REQUEST_CHANGE_PASSWORD:
                        Snackbar.make(getView(), R.string.profile_edit_change_password_success, Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        }
    }

    private void updateUserData(String password) {
        String accessToken = PrefsUtil.getStringPreference(getContext(), PrefsUtil.USER_ACCESS_TOKEN);
        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String middleName = middleNameField.getText().toString();
        String phone = phoneField.getPhoneNumber();
        String email = emailField.getText().toString();
        String studentId = studentIdField.getStudentId();
        //TODO fix email
        Call call = ApiManager.getApi(getContext()).updateUser(accessToken, password, firstName,
                middleName, lastName, phone, null, studentId);
        call.enqueue(updateUserCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Callback<User> updateUserCallback = new Callback<User>() {

        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if (getView() != null) {
                if (response.isSuccessful()) {
                    Fragment targetFragment = getTargetFragment();
                    if (targetFragment != null) {
                        Intent i = new Intent();
                        i.putExtra("user", Parcels.wrap(response.body()));
                        targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                    }
                    getActivity().onBackPressed();
                } else {
                    Snackbar.make(getView(), response.message(), Snackbar.LENGTH_SHORT).show();
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
