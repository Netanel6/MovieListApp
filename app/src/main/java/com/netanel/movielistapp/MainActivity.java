package com.netanel.movielistapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //animation from right to left when the activity is called after the splash screen
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_main);
        setUpNavDrawer();
        setNavigationViewItemSelected();
    }

    //set up custom toolbar layout and the navigation drawer layout and the navigation view
    public void setUpNavDrawer() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        /*
        This class provides a handy way to tie together the functionality of DrawerLayout and the
         framework ActionBar to implement the recommended design for navigation drawers.
        */
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //Synchronize the state of the drawer indicator/affordance with the linked DrawerLayout
        actionBarDrawerToggle.syncState();
        //Set a listener that will be notified when a menu item is selected.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_movie_list:
                        selectedFragment = new MovieListFragment();
                        break;
                    case R.id.nav_qr_scanner:
                        selectedFragment = new QrScannerFragment();
                        break;
                    case R.id.nav_logout:
                        finishAndRemoveTask();
                        System.exit(0);
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }
                //Closing the drawer after any selection
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });
    }

    //Sets the nav_movie_list as the first fragment to see when we load the app
    private void setNavigationViewItemSelected() {
        navigationView.setCheckedItem(R.id.nav_movie_list);
        navigationView.getMenu().performIdentifierAction(R.id.nav_movie_list, 0);
    }

    /*
    Deleted the super method so the back button wont work. you can exit from the app by click on
    nav_logout
    */
    @Override
    public void onBackPressed() {
    }
}