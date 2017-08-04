package com.example.daksh.mytwitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetui.TweetUtils;

import retrofit2.Call;
import retrofit2.Response;

public class TweetView extends AppCompatActivity implements View.OnClickListener {
    FrameLayout frameLayout;
    LinearLayout layout;
    long id;
    Button likeButton, commentButton, retweetButton;
    boolean flag ,retweet;
    ApiInterface apiInterface;
    int likedCount,retweetCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_view);
        apiInterface=ApiClient.getApiInterface();
        frameLayout = (FrameLayout) findViewById(R.id.tweetViewLinearLayout);
        layout = (LinearLayout) findViewById(R.id.linear_layout);
        likeButton = (Button) findViewById(R.id.likeButton);
        commentButton = (Button) findViewById(R.id.commentButton);
        retweetButton = (Button) findViewById(R.id.retweetButton);
        likeButton.setOnClickListener(this);
        commentButton.setOnClickListener(this);
        retweetButton.setOnClickListener(this);
        id = getIntent().getLongExtra("id", -1);
        flag=getIntent().getBooleanExtra("liked",true);
        retweet=getIntent().getBooleanExtra("retweeted",true);
        likedCount=getIntent().getIntExtra("likeCount",0);
        retweetCount=getIntent().getIntExtra("retweetCount",0);
        checkTweet();
        TweetUtils.loadTweet(id, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                com.twitter.sdk.android.tweetui.TweetView tweetView = new com.twitter.sdk.android.tweetui.TweetView(TweetView.this,
                        result.data);

                frameLayout.addView(tweetView);
                layout.setVisibility(View.VISIBLE);

                tweetView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.likeButton) {
            if (flag) {
                Call<Void> call=apiInterface.unlikeTweet(id);
                call.enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful())
                        {
                            // Toast.makeText(TweetView.this, "Unlike", Toast.LENGTH_SHORT).show();
                            likeButton.setTextColor(TweetView.this.getResources().getColor(R.color.tw__composer_black));
                            flag = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

            } else {

                Call<Void> call = apiInterface.likeTweet(id);
                call.enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful())
                        {
                            //Toast.makeText(TweetView.this, "Liked", Toast.LENGTH_SHORT).show();
                            likeButton.setTextColor(TweetView.this.getResources().getColor(R.color.tw__composer_blue_text));
                            flag = true;
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

            }
        }
        else if(view.getId()==R.id.retweetButton)
        {
           if(retweet)
           {
               Call<Void> call=apiInterface.unretweetTweet(id);
               call.enqueue(new retrofit2.Callback<Void>() {
                   @Override
                   public void onResponse(Call<Void> call, Response<Void> response) {
                       if(response.isSuccessful())
                       {
                           // Toast.makeText(TweetView.this, "UnRetweet", Toast.LENGTH_SHORT).show();
                           retweetButton.setTextColor(TweetView.this.getResources().getColor(R.color.tw__composer_black));
                           retweet = false;
                       }
                   }

                   @Override
                   public void onFailure(Call<Void> call, Throwable t) {

                   }
               });

           }else{

               Call<Void> call=apiInterface.retweetTweet(id);
               call.enqueue(new retrofit2.Callback<Void>() {
                   @Override
                   public void onResponse(Call<Void> call, Response<Void> response) {
                       if(response.isSuccessful())
                       {
                           // Toast.makeText(TweetView.this, "retweet", Toast.LENGTH_SHORT).show();
                           retweetButton.setTextColor(TweetView.this.getResources().getColor(R.color.tw__composer_blue_text));
                           retweet = true;
                       }
                   }

                   @Override
                   public void onFailure(Call<Void> call, Throwable t) {

                   }
               });
           }
        }
        else if(view.getId()==R.id.commentButton)
        {

        }
    }

    void checkTweet()
    {
        if(flag)
        {
            likeButton.setTextColor(TweetView.this.getResources().getColor(R.color.tw__composer_blue_text));
        }else{
            likeButton.setTextColor(TweetView.this.getResources().getColor(R.color.tw__composer_black));
        }

        if(retweet)
        {
            retweetButton.setTextColor(TweetView.this.getResources().getColor(R.color.tw__composer_blue_text));
        }else{
            retweetButton.setTextColor(TweetView.this.getResources().getColor(R.color.tw__composer_black));
        }
    }
}
