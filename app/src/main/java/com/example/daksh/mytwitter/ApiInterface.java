package com.example.daksh.mytwitter;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Daksh Garg on 7/22/2017.
 */

public interface ApiInterface {

    @GET("account/verify_credentials.json")
    Call<Profile> getProfile();

    @GET("statuses/home_timeline.json")
    Call<List<Tweet>> getUserTimelineTweets();

    @GET("statuses/home_timeline.json")
    Call<List<Tweet>> getMoreTweetsOfUserTimeline(@Query("max_id") String max_id);

    @GET("statuses/home_timeline.json")
    Call<List<Tweet>> getLatestTweets(@Query("since_id") String since_id);

    @GET("trends/place.json")
    Call<ArrayList<Trend>> getTrends(@Query("id") int id);

    @GET("friends/list.json")
    Call<Friends> getFriends(@Query("cursor") String cursor, @Query("screen_name") String screen_name,
                             @Query("skip_status") boolean skip_status, @Query("include_user_entities") boolean include_user_entities);

    @GET("followers/list.json")
    Call<Followers> getFollowers(@Query("cursor") String cursor, @Query("screen_name") String screen_name,
                                 @Query("skip_status") boolean skip_status, @Query("include_user_entities") boolean include_user_entities);

    @GET("search/tweets.json")
    Call<ParticularTrend> getParticularTrendTweets(@Query(value = "q", encoded = true) String q);


    @GET("users/search.json")
    Call<List<Users>> getUsers(@Query("q") String q);

    @POST("friendships/create.json")
    Call<Void> createFriend(@Query("user_id") String user_id);

    @POST("friendships/destroy.json")
    Call<Void> destroyFriend(@Query("user_id") String user_id);

    @POST("favorites/create.json")
    Call<Void> likeTweet(@Query("id") long id);

    @POST("favorites/destroy.json")
    Call<Void> unlikeTweet(@Query("id") long id);

    @POST("statuses/retweet/{id}.json")
    Call<Void> retweetTweet(@Path("id") long id);

    @POST("statuses/unretweet/{id}.json")
    Call<Void> unretweetTweet(@Path("id") long id);

    @GET("favorites/list.json")
    Call<List<Tweet>> getFavouriteTweets(@Query("screen_name") String screen_name);

    @GET("statuses/user_timeline.json")
    Call<List<Tweet>> getUserTweets(@Query("screen_name") String screen_name);


}
