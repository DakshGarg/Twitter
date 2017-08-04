package com.example.daksh.mytwitter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Daksh Garg on 8/4/2017.
 */

public class UserInfoPagerAdapter extends FragmentPagerAdapter {
    Bundle bundle;
    UserInfoPagerAdapter(FragmentManager fm, Bundle bundle){
        super(fm);
        this.bundle=bundle;
    }
    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {
            UserTweetsFragment fragment=new UserTweetsFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
        if(position==1)
        {

            UserFavouritesTweetsFragment fragment=new UserFavouritesTweetsFragment();
            fragment.setArguments(bundle);
            return fragment;
        }




        return null;

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Tweets";
            case 1:
                return "Likes";
        }
        return null;
    }
}
