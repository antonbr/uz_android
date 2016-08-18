package com.uzapp.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.uzapp.R;
import com.uzapp.view.BaseActivity;
import com.uzapp.view.main.menu.MenuFragment;
import com.uzapp.view.main.profile.ProfileFragment;
import com.uzapp.view.main.search.SearchFragment;

public class MainActivity extends BaseActivity implements AHBottomNavigation.OnTabSelectedListener {
    private AHBottomNavigation bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNavItems();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("profile")) {
            replaceFragment(new ProfileFragment(), true);
        } else {
            clearBackstack();
            bottomNavigationBar.setCurrentItem(0);
        }
    }

    private void createNavItems() {
        bottomNavigationBar = (AHBottomNavigation) findViewById(R.id.bottomNavigationBar);
        if (bottomNavigationBar != null) {
            bottomNavigationBar.setOnTabSelectedListener(this);
        }

        //CREATE ITEMS
        AHBottomNavigationItem searchTrainItem = new AHBottomNavigationItem(null, R.drawable.ic_search_train);
        AHBottomNavigationItem myTicketsItem = new AHBottomNavigationItem(null, R.drawable.ic_my_tickets);
        AHBottomNavigationItem bottomMenuItem = new AHBottomNavigationItem(null, R.drawable.ic_bottom_menu);
        //ADD THEM to bar
        bottomNavigationBar.addItem(searchTrainItem);
        bottomNavigationBar.addItem(myTicketsItem);
        bottomNavigationBar.addItem(bottomMenuItem);
        //set properties
        bottomNavigationBar.setDefaultBackgroundColor(getResources().getColor(android.R.color.white));
        bottomNavigationBar.setAccentColor(getResources().getColor(R.color.bgButtonWagonPlacesSelected));
        //set current item
        bottomNavigationBar.setCurrentItem(0);
        replaceFragment(new SearchFragment(), false);
    }

    @Override
    public void onTabSelected(int position, boolean wasSelected) {
        switch (position) {
            case 0:
                if (!wasSelected)
                    replaceFragment(new SearchFragment(), false);
                break;
            case 1:
                Toast.makeText(MainActivity.this, "My Tickets", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                if (!wasSelected)
                    addFragment(new MenuFragment(), R.anim.slide_up, R.anim.slide_down);
                break;
            default:
                break;
        }
    }
}
