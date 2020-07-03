package com.mcdev.whap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mcdev.whap.NonSwipeableViewpager.NonSwipeableViewpager;
import com.mcdev.whap.NonSwipeableViewpager.ViewpagerAdapter;
import com.mcdev.whap.Utils.MyMethods;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

        //make activity fullscreen
        MyMethods.makeFullScreen(MainActivity.this);
        
        //checking permissions handled with dexter
        checkPermissions(this);

        //animated bottom bar
        animatedBottomBarListeners();

        //init view pager
        //initViewpager();          // commented this because the viewpager initialises after the permissions are granted
    }


    public void initViewpager() {
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

    public void checkPermissions(Context context) {
        Dexter.withContext(context)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        initViewpager();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();


    }
}