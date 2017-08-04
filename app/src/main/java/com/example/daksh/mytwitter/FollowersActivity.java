package com.example.daksh.mytwitter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FollowersActivity extends AppCompatActivity {
    ListView followerListView;
    FollowersAdapter adapter;
    ArrayList<Followers.FollowersObject> followerArrayList;
    TwitterSession session;
    String next_cursor;
    boolean flag = true,follow;
    FancyButton button;
    private boolean check=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers);
        setTitle("Followers");
        session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        followerListView = (ListView) findViewById(R.id.followersListView);
        followerArrayList = new ArrayList<>();
        adapter = new FollowersAdapter(this, followerArrayList);
        adapter.setButtonClickListener(new FollowersAdapter.ButtonClickListener() {
            @Override
            public void onButtonClicked(View v, final int position) {
                button=(FancyButton) v;
                follow(position);
                if(follow)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(FollowersActivity.this);
                    builder.setTitle("Unfollow");
                    builder.setMessage("Stop Following "+followerArrayList.get(position).name+"?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ApiInterface apiInterface=ApiClient.getApiInterface();
                            Call<Void> call=apiInterface.destroyFriend(followerArrayList.get(position).id_str);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(response.isSuccessful())
                                    {
                                        button.setText("Follow");
                                        button.setTextColor(getResources().getColor(R.color.tw__composer_blue_text));
                                        button.setBackgroundColor(getResources().getColor(R.color.tw__composer_white));
                                        follow=false;
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
                    Call<Void> call=apiInterface.createFriend(followerArrayList.get(position).id_str);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful())
                                follow=true;
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                }


            }
        });
        followerListView.setAdapter(adapter);
        followerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Users u=new Users(followerArrayList.get(i).id_str,followerArrayList.get(i).name,followerArrayList.get(i).screen_name,
                        followerArrayList.get(i).location,followerArrayList.get(i).description,followerArrayList.get(i).profile_image_url,
                        followerArrayList.get(i).profile_banner_url,followerArrayList.get(i).following
                        ,followerArrayList.get(i).followers_count,
                        followerArrayList.get(i).friends_count);
                Intent intent=new Intent(FollowersActivity.this,UserDisplayInfoActivity.class);
                intent.putExtra("info",u);
                startActivity(intent);

            }
        });

        followerListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (i + i1 == i2 && followerArrayList.size() != 0 && flag == true) {
                    if (!next_cursor.equals("0")) {
                        flag = false;
                        loadMoreFollowers();

                    }

                }

            }
        });

       ApiInterface apiInterface=ApiClient.getApiInterface();
        Call<Followers> call = apiInterface.getFollowers("-1",session.getUserName(),true,false);
        call.enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                if(response.isSuccessful()) {

                    next_cursor = response.body().next_cursor_str;
                    Log.d("check11",next_cursor);
                    ArrayList<Followers.FollowersObject> followerList = response.body().users;
                    followerArrayList.addAll(followerList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Followers> call, Throwable t) {

            }
        });
    }


    private void follow(int position) {
        if(check) {
            follow = followerArrayList.get(position).following;
            check=false;
        }
    }
    private void loadMoreFollowers() {
        ApiInterface apiInterface=ApiClient.getApiInterface();
        Call<Followers> call = apiInterface.getFollowers(next_cursor,session.getUserName(),true,false);
        call.enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                if(response.isSuccessful()) {

                    next_cursor = response.body().next_cursor_str;
                    ArrayList<Followers.FollowersObject> followerList = response.body().users;
                    followerArrayList.addAll(followerList);
                    adapter.notifyDataSetChanged();
                    flag=true;
                }
            }

            @Override
            public void onFailure(Call<Followers> call, Throwable t) {

            }
        });
    }



}
