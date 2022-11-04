package com.example.mytracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);

        AuthenticationPagerAdapter pagerAdapter =  new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);

        class AuthenticationPagerAdapter extends FragmentPagerAdapter{
            private ArrayList<Fragment> fragmentsLists = new ArrayList<>();

            public AuthenticationPagerAdapter(@NonNull FragmentManager fm) {
                super(fm);
            }

            public AuthenticationPagerAdapter(@NonNull FragmentManager fm, int behavior) {
                super(fm, behavior);
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentsLists.get(position);
            }

            @Override
            public int getCount() {
                return fragmentsLists.size();
            }

            void addFragment(Fragment fragment){
                fragmentsLists.add(fragment);
            }
        }
    }
}