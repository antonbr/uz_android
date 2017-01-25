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
    protected FragmentManager fragmentManager = getSupportFragmentManager();

    public void replaceFragment(Fragment f, boolean addToBackStack) {
        Fragment oldFragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (oldFragment != null && oldFragment.getClass().equals(f.getClass())) {
            return;
        }
        // prevent duplicates
        String backStateName = f.getClass().getName();
        /* check whether fragment with this class name is already in backstack and we must pop it
         rather that adding new one. This will keep only one instance of every fragment in backstack */
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        if (fragmentPopped) return;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(f.getClass().getName());
        }
        transaction.replace(R.id.fragmentContainer, f, f.getClass().getName());
        transaction.commit();
    }

    public void addFragment(Fragment f, @AnimRes int enter, @AnimRes int exit) {
        Fragment oldFragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (oldFragment != null && oldFragment.getClass().equals(f.getClass())) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(enter, exit, enter, exit);
        transaction.add(R.id.fragmentContainer, f, f.getClass().getName());
        transaction.commit();
    }

    public void clearBackstack() {
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onBackPressed() {
        fragmentManager.popBackStack();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
    }
}
