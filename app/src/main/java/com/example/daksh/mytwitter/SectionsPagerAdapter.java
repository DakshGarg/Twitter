package com.example.daksh.mytwitter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

/**
 * Created by Daksh Garg on 7/24/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);

    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            UsertimelineActivity fragment = new UsertimelineActivity();
            return fragment;
        }
        if (position == 1) {
            TrendFragment fragment = new TrendFragment();
            return fragment;
        }
        if (position == 2) {
            TimelineActivity fragment = new TimelineActivity();
            return fragment;
        }


        return null;

    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Trends";
            case 2:
                return "Tweets";
        }
        return null;

        }
    }
