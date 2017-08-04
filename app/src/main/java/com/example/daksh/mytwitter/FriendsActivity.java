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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

public class FriendsActivity extends AppCompatActivity {
    ListView friendListView;
    FriendsAdapter adapter;
    ArrayList<Friends.FriendObject> friendArrayList;
    TwitterSession session;
    String next_cursor;
    boolean flag = true,follow;
    FancyButton button;
    private boolean check=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        setTitle("Following");
        next_cursor = "-1";
        session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        friendListView = (ListView) findViewById(R.id.friendsListView);
        friendArrayList = new ArrayList<>();
        adapter = new FriendsAdapter(this, friendArrayList);
        adapter.setButtonClickListener(new FriendsAdapter.ButtonClickListener() {
            @Override
            public void onButtonClicked(View v, final int position) {
                button=(FancyButton) v;
                follow(position);
                if(follow)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(FriendsActivity.this);
                    builder.setTitle("Unfollow");
                    builder.setMessage("Stop Following "+friendArrayList.get(position).name+"?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ApiInterface apiInterface=ApiClient.getApiInterface();
                            Call<Void> call=apiInterface.destroyFriend(friendArrayList.get(position).id_str);
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
                    Call<Void> call=apiInterface.createFriend(friendArrayList.get(position).id_str);
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
        friendListView.setAdapter(adapter);
        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Users u=new Users(friendArrayList.get(i).id_str,friendArrayList.get(i).name,friendArrayList.get(i).screen_name,
                        friendArrayList.get(i).location,friendArrayList.get(i).description,friendArrayList.get(i).profile_image_url,
                        friendArrayList.get(i).profile_banner_url,friendArrayList.get(i).following,friendArrayList.get(i).followers_count,
                        friendArrayList.get(i).friends_count);
                Intent intent=new Intent(FriendsActivity.this,UserDisplayInfoActivity.class);
                intent.putExtra("info",u);
                startActivity(intent);

            }
        });

        friendListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (i + i1 == i2 && friendArrayList.size() != 0 && flag == true) {
                    if (!next_cursor.equals("0")) {
                        flag = false;
                        loadMoreFriends();

                    }

                }

            }
        });
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<Friends> call = apiInterface.getFriends("-1", session.getUserName(), true, false);
        call.enqueue(new Callback<Friends>() {
            @Override
            public void onResponse(Call<Friends> call, Response<Friends> response) {

                if (response.isSuccessful()) {

                    next_cursor = response.body().next_cursor_str;
                    ArrayList<Friends.FriendObject> friendList = response.body().users;
                     friendArrayList.addAll(friendList);
                    /*for (int i = 0; i < friendList.size(); i++) {
                        Friends.FriendObject f = new Friends.FriendObject(friendList.get(i).name, friendList.get(i).screen_name,
                                friendList.get(i).profile_image_url.replaceAll("_normal",""));
                        friendArrayList.add(f);
                    }*/
                    adapter.notifyDataSetChanged();
                } else {
                }
            }

            @Override
            public void onFailure(Call<Friends> call, Throwable t) {

            }
        });

    }

    private void follow(int position) {
        if(check) {
            follow = friendArrayList.get(position).following;
        check=false;
        }
    }

    private void loadMoreFriends() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<Friends> call = apiInterface.getFriends(next_cursor, session.getUserName(), true, false);
        call.enqueue(new Callback<Friends>() {
            @Override
            public void onResponse(Call<Friends> call, Response<Friends> response) {

                if (response.isSuccessful()) {
                    next_cursor = response.body().next_cursor_str;
                    ArrayList<Friends.FriendObject> friendList = response.body().users;
                    friendArrayList.addAll(friendList);
                   /* for (int i = 0; i < friendList.size(); i++) {
                        Friends.FriendObject f = new Friends.FriendObject(friendList.get(i).name, friendList.get(i).screen_name
                                , friendList.get(i).profile_image_url.replaceAll("_normal",""));
                        friendArrayList.add(f);
                    }*/
                    adapter.notifyDataSetChanged();
                    flag = true;
                }
            }

            @Override
            public void onFailure(Call<Friends> call, Throwable t) {

            }
        });
    }


}
