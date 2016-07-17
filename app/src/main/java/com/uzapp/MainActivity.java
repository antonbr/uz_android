package com.uzapp;

import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.uzapp.view.search.SearchFragment;

public class MainActivity extends AppCompatActivity {
   private FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new SearchFragment(), false);
    }


    public void replaceFragment(Fragment f, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.fragmentContainer, f, null);
        transaction.commit();
    }

    public void addFragment(Fragment f,@AnimRes int enter, @AnimRes int exit){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(enter,exit, enter,exit);
        transaction.add(R.id.fragmentContainer, f);
        transaction.commit();

    }
}
