package com.espfullstack.wedoo.adapters;

import android.content.Context;

import com.espfullstack.wedoo.R;
import com.espfullstack.wedoo.fragments.ToDooFormFragment;
import com.espfullstack.wedoo.fragments.ToDooItemFragment;
import com.espfullstack.wedoo.pojo.ToDoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ToDooPageAdapter extends FragmentPagerAdapter {
    private Context context;
    private ToDoo toDoo;

    public ToDooPageAdapter(@NonNull FragmentManager fm, Context context, ToDoo toDoo) {
        super(fm);
        this.context = context;
        this.toDoo = toDoo;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ToDooItemFragment.getInstance(toDoo);
            case 1:
            return ToDooFormFragment.getInstance(toDoo);
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
                return context.getString(R.string.todoo_items);
            case 1:
                return context.getString(R.string.todoo);
        }
        return null;
    }
}
