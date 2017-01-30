package com.uzapp.view.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.uzapp.R;
import com.uzapp.network.ApiErrorUtil;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.CreateAccountInfo;
import com.uzapp.pojo.LoginInfo;
import com.uzapp.pojo.SocialLoginErrorResponse;
import com.uzapp.pojo.SocialLoginInfo;
import com.uzapp.pojo.UserTokenResponse;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.PrefsUtil;
import com.uzapp.view.BaseActivity;
import com.uzapp.view.main.MainActivity;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

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
import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkTokenRequestListener;
import ru.ok.android.sdk.util.OkScope;

/**
 * Created by vika on 14.08.16.
 */
public class LoginFragment extends Fragment implements OkTokenRequestListener {
    public static final int REQUEST_USER_EMAIL = 1;
    List<String> permissionNeeds = Arrays.asList("email");
    private Unbinder unbinder;
    @BindView(R.id.emailField) TextInputEditText emailField;
    @BindView(R.id.emailLayout) TextInputLayout emailLayout;
    @BindView(R.id.passwordField) TextInputEditText passwordField;
    @BindView(R.id.passwordLayout) TextInputLayout passwordLayout;
    @BindView(R.id.loginBtn) Button loginBtn;
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    private CallbackManager callbackManager;//used for facebook login
    private Odnoklassniki odnoklassniki;
    private SocialLoginErrorResponse socialLoginErrorResponse; //when user doesn't exist api returns 404 and this object,
    // than app requests email in dialog if social network doesn't provide and executes register api call with fields from this object.
    // Password not needed when social id is set

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        emailLayout.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        passwordLayout.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        return view;
    }

    @OnFocusChange({R.id.emailField, R.id.passwordField})
    void onFieldFocusChanged(boolean focus, TextInputEditText view) {
        if (focus || view.getText().length() > 0) {
            view.setTranslationY(hintPadding);
        } else {
            view.setTranslationY(0);
        }
    }

    @OnClick(R.id.forgotPasswordBtn)
    void onForgotPasswordClicked() {
        ((BaseActivity) getActivity()).replaceFragment(new ResetPasswordFragment(), true);
    }

    @OnTextChanged({R.id.emailField, R.id.passwordField})
    void onFieldsChanged(Editable editable) {
        checkFieldState();
    }

    @OnClick(R.id.loginBtn)
    void onLoginBtnClicked() {
        ((BaseActivity) getActivity()).hideKeyboard(this);
        String deviceId = CommonUtils.getDeviceId(getContext());
        LoginInfo loginInfo = new LoginInfo(deviceId, emailField.getText().toString(),
                passwordField.getText().toString());
        Call<UserTokenResponse> call = ApiManager.getApi(getContext()).login(loginInfo);
        call.enqueue(loginCallback);
    }

    @OnClick(R.id.registrationBtn)
    void onRegistrationBtnClicked() {
        ((BaseActivity) getActivity()).replaceFragment(new CreateAccountFirstStepFragment(), true);
    }

    @OnClick(R.id.fbBtn)
    void onFbBtnClicked() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback);
        LoginManager.getInstance().logInWithReadPermissions(this, permissionNeeds);
    }

    @OnClick(R.id.vkBtn)
    void onVkBtnClicked() {
        VKSdk.login(getActivity(), VKScope.EMAIL);
    }

    @OnClick(R.id.okBtn)
    void onOkBtnClicked() {
        String appId = getString(R.string.odnoklassniki_app_id);
        String secretKey = getString(R.string.odnoklassniki_app_secret_key);
        String publicKey = getString(R.string.odnoklassniki_app_public_key);
        odnoklassniki = Odnoklassniki.createInstance(getActivity().getApplicationContext(), appId, secretKey, publicKey);
        odnoklassniki.setTokenRequestListener(this);
        odnoklassniki.requestAuthorization(getContext(), false, OkScope.VALUABLE_ACCESS);
    }

    private void checkFieldState() {
        boolean allowLogin = CommonUtils.isEmailValid(emailField.getText().toString()) &&
                CommonUtils.isPasswordValid(passwordField.getText().toString());
        loginBtn.setEnabled(allowLogin);
    }

    private void socialLogin(SocialLoginInfo socialLoginInfo) {
        Call<UserTokenResponse> userTokenResponseCall = ApiManager.getApi(getContext()).socialLogin(socialLoginInfo);
        userTokenResponseCall.enqueue(loginCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (odnoklassniki != null) {
            odnoklassniki.removeTokenRequestListener();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_USER_EMAIL) {
            String email = data.getStringExtra("email");
            createSocialAccount(email);
        } else if (!handleVkResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void createSocialAccount(String email) {
        String deviceId = CommonUtils.getDeviceId(getContext());
        CreateAccountInfo.CreateAccountInfoBuilder builder = new CreateAccountInfo.CreateAccountInfoBuilder(deviceId, email);
        if (socialLoginErrorResponse.isFacebook()) {
            builder.setFbId(socialLoginErrorResponse.getId());
        } else if (socialLoginErrorResponse.isVk()) {
            builder.setVkId(socialLoginErrorResponse.getId());
        } else if (socialLoginErrorResponse.isOk()) {
            builder.setOkId(socialLoginErrorResponse.getId());
        }
        builder.setFirstName(socialLoginErrorResponse.getFirstName());
        builder.setLastName(socialLoginErrorResponse.getLastName());
        Call<UserTokenResponse> call = ApiManager.getApi(getContext()).createAccount(builder.build());
        call.enqueue(loginCallback);
    }

    private boolean handleVkResult(int requestCode, int resultCode, Intent data) {
        return VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                String deviceId = CommonUtils.getDeviceId(getContext());
                SocialLoginInfo socialLoginInfo = new SocialLoginInfo(deviceId);
                socialLoginInfo.setVkToken(res.accessToken);
                socialLogin(socialLoginInfo);
            }

            @Override
            public void onError(VKError error) {
                Log.e(LoginFragment.class.getName(), "vk exception: " + error.toString());
            }
        });
    }

    private void showEmailDialog() {
        DialogFragment fragment = new EmailDialogFragment();
        fragment.setTargetFragment(this, REQUEST_USER_EMAIL);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
    }

    private Callback<UserTokenResponse> loginCallback = new Callback<UserTokenResponse>() {

        @Override
        public void onResponse(Call<UserTokenResponse> call, Response<UserTokenResponse> response) {
            if (getView() != null) {
                if (response.isSuccessful()) {
                    UserTokenResponse user = response.body();
                    PrefsUtil.saveUserInfo(getContext(), user.getUserId(), user.getAccessToken(), user.getRefreshToken());
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    //bring back main activity from stack and start profile fragment
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("profile", true);
                    startActivity(intent);
                } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                    socialLoginErrorResponse = ApiErrorUtil.getSocialLoginErrorResponse(response);
                    if (socialLoginErrorResponse != null) {
                        if (TextUtils.isEmpty(socialLoginErrorResponse.getEmail())) {
                            showEmailDialog();
                        } else {
                            createSocialAccount(socialLoginErrorResponse.getEmail());
                        }
                    }
                } else {
                    String error = ApiErrorUtil.getErrorMessage(response, getActivity());
                    CommonUtils.showSnackbar(emailField, error);
                }
            }
        }

        @Override
        public void onFailure(Call<UserTokenResponse> call, Throwable t) {
            if (getView() != null && t != null) {
                String error = ApiErrorUtil.getErrorMessage(t, getActivity());
                CommonUtils.showSnackbar(getView(), error);
            }
        }
    };
    private FacebookCallback facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            String deviceId = CommonUtils.getDeviceId(getContext());
            SocialLoginInfo socialLoginInfo = new SocialLoginInfo(deviceId);
            socialLoginInfo.setFbToken(accessToken.getToken());
            socialLogin(socialLoginInfo);
        }

        @Override
        public void onCancel() {
            LoginManager.getInstance().logOut();
        }

        @Override
        public void onError(FacebookException exception) {
            LoginManager.getInstance().logOut();
            Log.e(LoginFragment.class.getName(), "facebook exception: " + exception.toString());
        }
    };

    /*
    handling result of odnoklassniki login
     */
    @Override
    public void onSuccess(String token) {
        String deviceId = CommonUtils.getDeviceId(getContext());
        SocialLoginInfo socialLoginInfo = new SocialLoginInfo(deviceId);
        socialLoginInfo.setOkToken(token);
        socialLogin(socialLoginInfo);
    }

    @Override
    public void onError() {
        Log.e(LoginFragment.class.getName(), "APIOK Error");
    }

    @Override
    public void onCancel() {
        //if back button was pressed
        Log.e(LoginFragment.class.getName(), "APIOK Cancel");
    }
}
