package com.uzapp.view.main.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.User;
import com.uzapp.util.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.main.MainActivity;

import org.parceler.Parcels;

import butterknife.BindInt;
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
    @BindView(R.id.firstNameField) EditText firstNameField;
    @BindView(R.id.lastNameField) EditText lastNameField;
    @BindView(R.id.middleNameField) EditText middleNameField;
    @BindView(R.id.phoneField) PhoneNumberEditText phoneField;
    @BindView(R.id.emailField) EditText emailField;
    @BindView(R.id.studentIdSeriesField) EditText studentIdSeriesField;
    @BindView(R.id.studentIdNumberField) EditText studentIdNumberField;
    @BindView(R.id.tickBtn) ImageButton tickBtn;
    @BindInt(R.integer.profile_student_id_series_length) int studentIdSeriesLenght;
    private Unbinder unbinder;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_edit_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        user = Parcels.unwrap(getArguments().getParcelable("user"));
        initViews();
        ((MainActivity) getActivity()).hideNavigationBar();
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
            phoneField.setPhone(user.getPhoneNumber());
            String studentId = user.getStudentId();
            if (CommonUtils.isStudentIdValid(studentId)) {
                String studentIdSeries = studentId.substring(0, 2);
                studentIdSeriesField.setText(studentIdSeries);
                String studentIdNumber = studentId.substring(2, studentId.length());
                studentIdNumberField.setText(studentIdNumber);
            }
        }
    }


    @OnClick(R.id.closeBtn)
    void onBackBtnClicked() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.tickBtn)
    void onSaveBtnClicked() {
        EnterPasswordFragment fragment = new EnterPasswordFragment();
        fragment.setTargetFragment(this, REQUEST_PASSWORD);
        ((MainActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
    }

    @OnTextChanged(value = {R.id.firstNameField, R.id.middleNameField, R.id.lastNameField, R.id.phoneField,
            R.id.emailField},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onFieldsChanged(Editable editable) {
        checkFieldsState();
    }

    @OnTextChanged(value = {R.id.studentIdSeriesField},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onStudentIdSeriesChanged(Editable editable) {
        if (editable.length() == studentIdSeriesLenght) {
            studentIdNumberField.requestFocus();
        }
        checkFieldsState();
    }

    @OnTextChanged(value = {R.id.studentIdNumberField},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onStudentIdNumberChanged(Editable editable) {
        if (editable.length() == 0) {
            studentIdSeriesField.requestFocus();
        }
        checkFieldsState();
    }

    private void checkFieldsState() {
        boolean allowSaving = isFirstNameValid()
                && isMiddleNameValid()
                && isLastNameValid()
                && phoneField.isValid()
                && CommonUtils.isEmailValid(emailField.getText().toString())
                && CommonUtils.isStudentIdValid(studentIdSeriesField.getText().toString() + studentIdNumberField.getText().toString());
        tickBtn.setEnabled(allowSaving);
    }

    private boolean isFirstNameValid() {
        return firstNameField.getText().length() == 0 || firstNameField.getText().length() > Constants.FIRST_NAME_MIN_LENGTH;
    }

    private boolean isLastNameValid() {
        return lastNameField.getText().length() == 0 || lastNameField.getText().length() > Constants.LAST_NAME_MIN_LENGTH;
    }

    private boolean isMiddleNameValid() {
        return middleNameField.getText().length() == 0 || middleNameField.getText().length() > Constants.MIDDLE_NAME_MIN_LENGTH;
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
                        Snackbar.make(getView(), R.string.profile_edit_change_password_success_result, Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        }
    }

    private void updateUserData(String password) {
        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String middleName = middleNameField.getText().toString();
        String phone = phoneField.getPhoneNumber();
        String email = emailField.getText().toString();
        String studentId = studentIdSeriesField.getText().toString() + studentIdNumberField.getText().toString();
        email = user.getEmail().equals(email) ? null : email;
        Call call = ApiManager.getApi(getContext()).updateUser(password, firstName,
                middleName, lastName, phone, email, studentId);
        call.enqueue(updateUserCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        ((MainActivity) getActivity()).showNavigationBar();
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
