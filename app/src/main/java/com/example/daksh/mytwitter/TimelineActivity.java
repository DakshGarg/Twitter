package com.example.daksh.mytwitter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TimelineActivity extends Fragment {
    ListView timelineListView;
    SwipeRefreshLayout swipeRefreshLayout;
    TwitterSession session;
    UserTimeline userTimeline;
    CustomAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.timeline, container, false);
        timelineListView = (ListView) v.findViewById(R.id.timelineListView);
        session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout_for_timeline);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.tw__composer_blue),
                getResources().getColor(R.color.tw__composer_blue),getResources().getColor(R.color.tw__composer_blue));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showNewTimeline();
            }
        });

     userTimeline = new UserTimeline.Builder()
                .screenName(session.getUserName())
                .build();


        adapter=new CustomAdapter(getContext(), userTimeline, new CustomAdapter.OnTweetClickListener() {
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

        timelineListView.setAdapter(adapter);

        return v;
    }

    private void showNewTimeline() {
        Log.d("check","showNewTimeline");


        adapter=new CustomAdapter(getContext(), userTimeline, new CustomAdapter.OnTweetClickListener() {
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
        adapter.notifyDataSetChanged();
        timelineListView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }


}


