package com.uzapp.view;

import android.support.annotation.AnimRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.uzapp.R;

/**
 * Created by vika on 09.08.16.
 */
public class BaseActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();

    public void replaceFragment(Fragment f, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(f.getClass().getName());
        }
        transaction.replace(R.id.fragmentContainer, f, f.getClass().getName());
        transaction.commit();
    }

    public void addFragment(Fragment f, @AnimRes int enter, @AnimRes int exit) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(enter, exit, enter, exit);
        transaction.add(R.id.fragmentContainer, f);
        transaction.commit();
    }

    public void clearBackstack() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onBackPressed() {
        fragmentManager.popBackStack();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
    }
}
