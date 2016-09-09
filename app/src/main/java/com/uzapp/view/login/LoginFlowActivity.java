package com.uzapp.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = (Fragment) fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
