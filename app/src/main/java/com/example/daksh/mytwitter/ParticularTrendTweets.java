package com.example.daksh.mytwitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParticularTrendTweets extends AppCompatActivity {
    String trendName;
    ListView trendTweetsListView;
    List<Tweet> trendTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.particular_trend_tweets);
        Intent i=getIntent();
        trendName=i.getStringExtra("name");
        trendTweetsListView= (ListView) findViewById(R.id.particularTrendTweetsListView);
        showTrendTweets();


     /* try {
            trendName  = URLEncoder.encode(i.getStringExtra("name"),"UTF-8");
        }
 catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
       /* trendTweetsListView= (ListView) findViewById(R.id.particularTrendTweetsListView);
        trendTweets=new ArrayList<>();
        ApiInterface apiInterface=ApiClient.getApiInterface();
        Call<ParticularTrend> call=apiInterface.getParticularTrendTweets(trendName);
        call.enqueue(new Callback<ParticularTrend>() {
            @Override
            public void onResponse(Call<ParticularTrend> call, Response<ParticularTrend> response) {
                if(response.isSuccessful())
                {
                  //  trendTweets.addAll(response.body().statuses);
                    showTrendTweets();
                }

            }

            @Override
            public void onFailure(Call<ParticularTrend> call, Throwable t) {

            }
        });*/

    }

    private void showTrendTweets() {

       /* FixedTweetTimeline timeline = new FixedTweetTimeline.Builder()
                .setTweets(trendTweets)
                .build();

        TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(timeline)
                .build();*/
        SearchTimeline searchTimeline=new SearchTimeline.Builder()
                .query(trendName)
                .build();
        CustomAdapter adapter=new CustomAdapter(this, searchTimeline, new CustomAdapter.OnTweetClickListener() {
            @Override
            public void onTweetClicked(int position, Tweet tweet) {
                Intent i=new Intent(ParticularTrendTweets.this,TweetView.class);
                i.putExtra("id",tweet.id);
                i.putExtra("liked",tweet.favorited);
                i.putExtra("retweeted",tweet.retweeted);
                i.putExtra("likeCount",tweet.favoriteCount);
                i.putExtra("retweetCount",tweet.retweetCount);
                startActivity(i);
            }
        });


        trendTweetsListView.setAdapter(adapter);



    }
}
