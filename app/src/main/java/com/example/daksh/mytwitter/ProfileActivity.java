package com.example.daksh.mytwitter;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    Profile u;
    CircleImageView circleImageView;
    TextView nameTextView, displayInfoTextView, followingTextView, followersTextView,usernameTextView;
    ImageView backgroundImage;
    UserInfoPagerAdapter userInfoPagerAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        Intent i = getIntent();
        if(i!=null) {
            u = (Profile) i.getSerializableExtra("profile");

        }
        Bundle bundle=new Bundle();
        bundle.putString("screen_name",u.screen_name);
        userInfoPagerAdapter= new UserInfoPagerAdapter(getSupportFragmentManager(),bundle);
        mViewPager=(ViewPager)findViewById(R.id.info_container);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(userInfoPagerAdapter);
        tabLayout=(TabLayout)findViewById(R.id.info_tabs);
        tabLayout.setupWithViewPager(mViewPager);


        circleImageView = (CircleImageView) findViewById(R.id.userImage);
        backgroundImage=(ImageView)findViewById(R.id.backgroundImage);
        nameTextView = (TextView) findViewById(R.id.textView);
        displayInfoTextView = (TextView) findViewById(R.id.detailsTextView);
        followingTextView = (TextView) findViewById(R.id.followingTextview);
        followersTextView = (TextView) findViewById(R.id.followersTextView);
        usernameTextView=(TextView)findViewById(R.id.username);

        setInfo();

    }
    private void setInfo() {
        String url=u.profile_image_url.replaceAll("_normal","");
        Picasso.with(this).load(u.profile_banner_url).into(backgroundImage);
        Picasso.with(this).load(url).into(circleImageView);
        nameTextView.setText(u.name);
        usernameTextView.setText("@"+u.screen_name);
        if(!u.description.equals(""))
            displayInfoTextView.setText(u.description);
        if(!u.location.equals(""))
            displayInfoTextView.append("\n\n"+u.location);
        if(u.description.equals("") && u.location.equals(""))
        {
            displayInfoTextView.setVisibility(View.GONE);
        }
        followersTextView.setText(u.followers_count+"");
        followingTextView.setText(u.friends_count+"");

    }

}
