package com.example.daksh.mytwitter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.Voice;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Daksh Garg on 7/31/2017.
 */

public class UserDisplayInfoActivity extends AppCompatActivity implements View.OnClickListener {
    CircleImageView circleImageView;
    TextView nameTextView, displayInfoTextView, followingTextView, followersTextView,usernameTextView;
    ImageView backgroundImage;
    UserInfoPagerAdapter userInfoPagerAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;
    Users u;
    FancyButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_display_info);
        Intent i = getIntent();
        if(i!=null) {
            u = (Users) i.getSerializableExtra("info");

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
        button=(FancyButton) findViewById(R.id.follow_button);
        if(u.following)
        {
            button.setText("Following");

            button.setBackgroundColor(getResources().getColor(R.color.tw__composer_blue));
            button.setTextColor(getResources().getColor(R.color.tw__composer_white));
        }else{
            button.setText("Follow");
            button.setTextColor(getResources().getColor(R.color.tw__composer_blue_text));
            button.setBackgroundColor(getResources().getColor(R.color.tw__composer_white));
        }
        button.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {

        if(u.following)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Unfollow");
            builder.setMessage("Stop Following "+u.name+"?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ApiInterface apiInterface=ApiClient.getApiInterface();
                    Call<Void> call=apiInterface.destroyFriend(u.id_str);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful())
                            {
                                button.setText("Follow");
                                button.setTextColor(getResources().getColor(R.color.tw__composer_blue_text));
                                button.setBackgroundColor(getResources().getColor(R.color.tw__composer_white));
                                u.following=false;
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });

                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
                }
            });
            builder.create().show();

        }else{
            button.setText("Following");
            button.setBackgroundColor(getResources().getColor(R.color.tw__composer_blue));
            button.setTextColor(getResources().getColor(R.color.tw__composer_white));
            ApiInterface apiInterface=ApiClient.getApiInterface();
            Call<Void> call=apiInterface.createFriend(u.id_str);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful())
                    u.following=true;
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }


}
