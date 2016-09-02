package com.uzapp.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.uzapp.R;
import com.uzapp.util.Constants;
import com.uzapp.view.BaseActivity;
import com.uzapp.view.main.menu.MenuFragment;
import com.uzapp.view.main.profile.ProfileFragment;
import com.uzapp.view.main.search.SearchFragment;
import com.uzapp.view.main.tickets.MyTicketsFragment;

public class MainActivity extends BaseActivity implements AHBottomNavigation.OnTabSelectedListener {
    private AHBottomNavigation bottomNavigationBar;
    private int previousSelectedBottomItem = Constants.BOTTOM_NAVIGATION_SEARCH;

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

    @Override
    public void replaceFragment(Fragment f, boolean addToBackStack) {
        removeMenuFromStack();
        super.replaceFragment(f, addToBackStack);
    }

    private void removeMenuFromStack() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MenuFragment.class.getName());
        if (fragment instanceof MenuFragment && fragment.isVisible()) {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void createNavItems() {
        bottomNavigationBar = (AHBottomNavigation) findViewById(R.id.bottomNavigationBar);
        if (bottomNavigationBar != null) {
            bottomNavigationBar.setOnTabSelectedListener(this);
        }

        //CREATE ITEMS
        AHBottomNavigationItem searchTrainItem = new AHBottomNavigationItem(getString(R.string.menu_search_ticket), R.drawable.ic_search_train);
        AHBottomNavigationItem myTicketsItem = new AHBottomNavigationItem(getString(R.string.menu_my_ticket), R.drawable.ic_my_tickets);
        AHBottomNavigationItem bottomMenuItem = new AHBottomNavigationItem(getString(R.string.menu), R.drawable.menu_icon_selector);
        //ADD THEM to bar
        bottomNavigationBar.addItem(searchTrainItem);
        bottomNavigationBar.addItem(myTicketsItem);
        bottomNavigationBar.addItem(bottomMenuItem);
        //set properties
        bottomNavigationBar.setDefaultBackgroundColor(getResources().getColor(android.R.color.white));
        bottomNavigationBar.setAccentColor(getResources().getColor(R.color.bgButtonWagonPlacesSelected));
        //set current item
        bottomNavigationBar.setCurrentItem(Constants.BOTTOM_NAVIGATION_SEARCH);
    }


    public void hideNavigationBar() {
        bottomNavigationBar.hideBottomNavigation(true);
    }

    public void showNavigationBar() {
        bottomNavigationBar.restoreBottomNavigation(true);
        bottomNavigationBar.refresh();
    }

    public void selectNoneItemsInNavBar() {
        bottomNavigationBar.setCurrentItem(AHBottomNavigation.CURRENT_ITEM_NONE);
        bottomNavigationBar.refresh();
    }

    public AHBottomNavigation getBottomNavigationBar() {
        return bottomNavigationBar;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (bottomNavigationBar.getCurrentItem() == Constants.BOTTOM_NAVIGATION_MENU) {
            bottomNavigationBar.setCurrentItem(previousSelectedBottomItem);
        }
    }

    @Override
    public void onTabSelected(int position, boolean wasSelected) {
        switch (position) {
            case Constants.BOTTOM_NAVIGATION_SEARCH:
                previousSelectedBottomItem = Constants.BOTTOM_NAVIGATION_SEARCH;
                clearBackstack();
                replaceFragment(new SearchFragment(), false);
                break;
            case Constants.BOTTOM_NAVIGATION_TICKETS:
                previousSelectedBottomItem = Constants.BOTTOM_NAVIGATION_TICKETS;
                clearBackstack();
                replaceFragment(new MyTicketsFragment(), false);
                break;
            case Constants.BOTTOM_NAVIGATION_MENU:
                if (wasSelected) {
                    bottomNavigationBar.setCurrentItem(previousSelectedBottomItem);
                } else {
                    addFragment(new MenuFragment(), R.anim.slide_up, R.anim.slide_down);
                }
                break;
            case AHBottomNavigation.CURRENT_ITEM_NONE:
                previousSelectedBottomItem = AHBottomNavigation.CURRENT_ITEM_NONE;
                removeMenuFromStack();
                break;
            default:
                break;
        }
    }
}
