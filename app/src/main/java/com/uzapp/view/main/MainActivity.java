package com.uzapp.view.main;

import android.os.Bundle;

import com.uzapp.R;
import com.uzapp.view.BaseActivity;
import com.uzapp.view.main.search.SearchFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new SearchFragment(), false);
    }
}
