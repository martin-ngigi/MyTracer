package com.example.mytracer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytracer.Constants;
import com.example.mytracer.R;
import com.example.mytracer.fragments.SettingsFragment;
import com.example.mytracer.fragments.AccountFragment;
import com.example.mytracer.fragments.HomeFragment;
import com.example.mytracer.fragments.InboxFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FrameLayout frameLayout;
    ActionBarDrawerToggle toggle;

    //get Text views in header file
    NavigationView myNavigationView;
    View myHeaderView ;
    ImageView imageview;
    TextView fullNameTv, emailTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        frameLayout = findViewById(R.id.main_frameLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation);
        //inflate the header view at runtime
        //View headerLayout = navigationView.inflateHeaderView(R.layout.header_file);
        //ImageView ivHeaderPhoto = headerLayout.findViewById(R.id.imageview);

        //get Textviews and imageviews in header file
        myNavigationView = (NavigationView) findViewById(R.id.navigation);
        myHeaderView = navigationView.getHeaderView(0);
        fullNameTv = (TextView) myHeaderView.findViewById(R.id.fullNameTv);
        emailTv = (TextView) myHeaderView.findViewById(R.id.emailTv);
        imageview = (ImageView) myHeaderView.findViewById(R.id.imageview);

        //set user details in headerview
        setUserDataInHeaderView();



        //setup drawer view
        setupDrawerContent(navigationView);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //Tie DrawerLayout events to the ActionBarToggle
        drawerLayout.addDrawerListener(toggle);
        // Setup toggle to display hamburger icon with nice animation
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        //navigate to activity logout
        MenuItem logOutItem = navigationView.getMenu().findItem(R.id.signout);
        logOutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
                return true;
            }
        });

    }

    private void setUserDataInHeaderView() {
        //get data from Constants.java
        String email = Constants.email;
        String fname = Constants.f_name;
        String lname = Constants.l_name;

        //set data to ui
        emailTv.setText(email);
        fullNameTv.setText(fname+" "+lname);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });

    }

    private void selectDrawerItem(MenuItem item) {
        //create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (item.getItemId()){
            case R.id.home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.settings:
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.inbox:
                fragmentClass = InboxFragment.class;
                break;
            case R.id.user:
                fragmentClass = AccountFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        //insert the fragment by replacing the existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        //highlight the selected item has been done by NavigationView
        item.setCheckable(true);
        //set action bar title
        setTitle(item.getTitle());
        //close the navigation drawer
        drawerLayout.closeDrawers();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

//        //////or
//        if (toggle.onOptionsItemSelected(item)){
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    //Next, we need to make sure we synchronize the state whenever the screen is restored or there is a configuration change (i.e screen rotation):

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();

        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        //pass any configuration to the drawer toggles
        toggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
}