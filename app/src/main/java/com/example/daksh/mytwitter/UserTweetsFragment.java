package com.example.daksh.mytwitter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserTweetsFragment extends Fragment {

    ListView listView;
    List<Tweet> tweetArrayList;
    FixedTweetTimeline timeline;
    CustomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user_tweets, container, false);
        listView = (ListView) v.findViewById(R.id.userTweetsListView);
        tweetArrayList = new ArrayList<>();
        Bundle bundle = getArguments();
        String screen_name = bundle.getString("screen_name");
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<List<Tweet>> call = apiInterface.getUserTweets(screen_name);
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()) {
                    tweetArrayList.addAll(response.body());
                    showUserTimeline();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {

            }
        });
        return v;
    }

    private void showUserTimeline() {
        timeline = new FixedTweetTimeline.Builder()
                .setTweets(tweetArrayList)
                .build();
        adapter = new CustomAdapter(getContext(), timeline, new CustomAdapter.OnTweetClickListener() {
            @Override
            public void onTweetClicked(int position, Tweet tweet) {
                Intent i = new Intent(getActivity(), TweetView.class);
                i.putExtra("id", tweet.id);
                i.putExtra("liked", tweet.favorited);
                i.putExtra("retweeted", tweet.retweeted);
                i.putExtra("likeCount", tweet.favoriteCount);
                i.putExtra("retweetCount", tweet.retweetCount);
                startActivity(i);
            }
        });
        listView.setAdapter(adapter);
    }

}
