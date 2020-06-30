package com.mcdev.whap.NonSwipeableViewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mcdev.whap.Fragments.ImagesFragment;
import com.mcdev.whap.Fragments.LibraryFragment;
import com.mcdev.whap.Fragments.VideosFragment;

public class ViewpagerAdapter extends FragmentPagerAdapter {
    public ViewpagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewpagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ImagesFragment();
            case 1:
                return new VideosFragment();
            case 2:
                return new LibraryFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;       //Three fragments
    }
}
