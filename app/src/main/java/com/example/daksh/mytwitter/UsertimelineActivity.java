package com.example.daksh.mytwitter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsertimelineActivity extends Fragment {
    ListView usertimelineListView;
    List<Tweet> usertimelineTweets;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar mProgressBar;

    boolean flag=true;
    String max_id;
    String since_id="-1";
    CustomAdapter adapter;
    private FixedTweetTimeline timeline;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        View v = inflater.inflate(R.layout.usertimeline, container, false);
        mProgressBar =v.findViewById(R.id.progress_bar);
        showProgress(true);
        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.tw__composer_blue),
                getResources().getColor(R.color.tw__composer_blue),getResources().getColor(R.color.tw__composer_blue));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showLatestTweets();
            }
        });
        usertimelineTweets = new ArrayList<>();
        usertimelineListView = (ListView) v.findViewById(R.id.usertimelineListView);
        showTweets();
        usertimelineListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (i + i1 == i2 && usertimelineTweets.size()!=0 && flag == true) {
                    if (!max_id.equals("-1")){
                        flag=false;
                        loadMoreTweets();
                    }
                }
            }
        });

        return v;
    }

    private void showLatestTweets() {
        ApiInterface apiInterface=ApiClient.getApiInterface();
        Call<List<Tweet>> call=apiInterface.getLatestTweets(since_id);
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if(response.isSuccessful())
                {
                for(int i=response.body().size()-1;i>=0;i--)
                {
                    usertimelineTweets.add(0,response.body().get(i));
                }
                if(response.body().size()!=0) {
                    since_id = String.valueOf(response.body().get(0).id);

                }
                showHomeTimeline();
                }
                else{
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    private void loadMoreTweets() {

        ApiInterface apiInterface=ApiClient.getApiInterface();
        Call<List<Tweet>> call=apiInterface.getMoreTweetsOfUserTimeline(max_id);
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()) {
                    usertimelineTweets.addAll(response.body());
                    Log.d("check", usertimelineTweets.size() + "");
                    max_id = String.valueOf(response.body().get(response.body().size() - 1).id);
                    Log.d("check", max_id);
                    Log.d("check",response.body().get(response.body().size()-1).text);
                    showHomeTimeline();

                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {

            }
        });


    }

    private  void showTweets(){
        ApiInterface apiInterface=ApiClient.getApiInterface();
        Call<List<Tweet>> call = apiInterface.getUserTimelineTweets();

        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()) {
                    usertimelineTweets.clear();
                    usertimelineTweets.addAll(response.body());
                    since_id=String.valueOf(response.body().get(0).id);
                    max_id=String.valueOf(response.body().get(response.body().size()-1).id);
                    Log.d("check",max_id);
                    Log.d("check",response.body().get(response.body().size()-1).text);
                    showProgress(false);
                   showHomeTimeline();

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {


            }
        });
    }

    private void showHomeTimeline() {

        timeline = new FixedTweetTimeline.Builder()
                .setTweets(usertimelineTweets)
                .build();




         adapter=new CustomAdapter(getContext(), timeline, new CustomAdapter.OnTweetClickListener() {
            @Override
            public void onTweetClicked(int position, Tweet tweet) {
                Intent i=new Intent(getActivity(),TweetView.class);
                i.putExtra("id",tweet.id);
                i.putExtra("liked",tweet.favorited);
                i.putExtra("retweeted",tweet.retweeted);
                i.putExtra("likeCount",tweet.favoriteCount);
                i.putExtra("retweetCount",tweet.retweetCount);
                startActivity(i);
            }
        });
        usertimelineListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            flag = true;



        swipeRefreshLayout.setRefreshing(false);
    }


    private void showProgress(boolean show) {

        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }


}
