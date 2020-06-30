package com.mcdev.whap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.mcdev.whap.NonSwipeableViewpager.NonSwipeableViewpager;
import com.mcdev.whap.NonSwipeableViewpager.ViewpagerAdapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    AnimatedBottomBar animatedBottomBar;
    NonSwipeableViewpager viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        init();

        //animated bottom bar
        animatedBottomBarListeners();

        initViewpager();
    }

    private void initViewpager() {
        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);       //initializing the view pager
        viewpager.setAdapter(adapter);      //setting view pager's adapter with contents in the viewpager adapter class
        viewpager.setOffscreenPageLimit(3);     //increasing the limit for screens to lose cache to three
//        viewPager.setCurrentItem(0);        //setting default page to position 0
    }


    private void animatedBottomBarListeners() {
        animatedBottomBar.setupWithViewPager(viewpager);    //set up with view pager
        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NotNull AnimatedBottomBar.Tab tab1) {

            }

            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {

            }
        });
    }

    private void init() {
        animatedBottomBar = findViewById(R.id.animated_bottom_bar);
        viewpager = findViewById(R.id.fragment_container_viewpager);
    }
}