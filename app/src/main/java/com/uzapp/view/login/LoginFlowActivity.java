package com.uzapp.view.login;

import android.os.Bundle;

import com.uzapp.R;
import com.uzapp.view.BaseActivity;

/**
 * Created by vika on 09.08.16.
 */
public class LoginFlowActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        replaceFragment(new LoginFragment(), false);
    }

}
