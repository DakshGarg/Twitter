package com.example.daksh.mytwitter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;



public class MainActivity extends AppCompatActivity {

    TwitterLoginButton twitterLoginButton;
    public final static String TOKEN = "token";
    public final static String TOKEN_SECRET = "secret";
    public final static String USER_NAME = "user_name";
    public final static String USER_ID = "user_id";
    public final static String SP_NAME = "twitter";

    String token, tokenSecret, userName;
    long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Twitter.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.login_button);

        SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        token = sp.getString(TOKEN, null);

        if (token != null) {

            tokenSecret = sp.getString(TOKEN_SECRET, null);
            userName = sp.getString(USER_NAME, null);
            userId = sp.getLong(USER_ID, -1);

            setActiveSession();
            Intent i = new Intent(MainActivity.this, StartingtwitterActivity.class);
            startActivity(i);
            finish();


        }

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                TwitterAuthToken authToken = session.getAuthToken();

                SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong(USER_ID, session.getUserId());
                editor.putString(USER_NAME, session.getUserName());
                editor.putString(TOKEN, authToken.token);
                editor.putString(TOKEN_SECRET, authToken.secret);
                editor.commit();

                Intent i = new Intent(MainActivity.this, StartingtwitterActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });


    }

    private void setActiveSession() {
        TwitterAuthToken authToken = new TwitterAuthToken(token, tokenSecret);
        TwitterSession session = new TwitterSession(authToken, userId, userName);
        TwitterCore.getInstance().getSessionManager().setActiveSession(session);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
