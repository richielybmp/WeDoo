package com.espfullstack.wedoo.adapters;

import android.content.Context;

import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.fragments.ToDooFormFragment;
import com.espfullstack.wedoo.fragments.ToDooItemFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FPageAdapter extends FragmentPagerAdapter {
    private Context context;

    public FPageAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ToDooFormFragment();
            case 1:
                return new ToDooItemFragment();
            default:
                return new Fragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.app_name);
            case 1:
                return context.getString(R.string.todoo_items);
        }
        return null;
    }
}
