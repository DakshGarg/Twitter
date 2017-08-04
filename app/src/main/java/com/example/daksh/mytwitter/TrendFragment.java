package com.example.daksh.mytwitter;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TrendFragment extends Fragment {
    ListView trendListView;
    ArrayAdapter<String> adapter;
    ArrayList<String> trendArrayList;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.trendfragment, container, false);
        trendListView = (ListView) v.findViewById(R.id.trendListView);
        trendArrayList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, trendArrayList);
        trendListView.setAdapter(adapter);
        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout_for_trends);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.tw__composer_blue),
                getResources().getColor(R.color.tw__composer_blue),getResources().getColor(R.color.tw__composer_blue));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showLatestTrends();
            }
        });


        trendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ParticularTrendTweets.class);
                intent.putExtra("name", trendArrayList.get(i));
                startActivity(intent);
            }
        });

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<ArrayList<Trend>> call = apiInterface.getTrends(23424848);
        call.enqueue(new Callback<ArrayList<Trend>>() {
            @Override
            public void onResponse(Call<ArrayList<Trend>> call, Response<ArrayList<Trend>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().get(0).trends.size(); i++) {
                        trendArrayList.add(response.body().get(0).trends.get(i).name);
                    }
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Trend>> call, Throwable t) {

            }
        });
        setHasOptionsMenu(true);
        return v;
    }

    private void showLatestTrends() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<ArrayList<Trend>> call = apiInterface.getTrends(23424848);
        call.enqueue(new Callback<ArrayList<Trend>>() {
            @Override
            public void onResponse(Call<ArrayList<Trend>> call, Response<ArrayList<Trend>> response) {
                if (response.isSuccessful()) {
                    trendArrayList.clear();
                    for (int i = 0; i < response.body().get(0).trends.size(); i++) {
                        trendArrayList.add(response.body().get(0).trends.get(i).name);
                    }
                    adapter.notifyDataSetChanged();

                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<Trend>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i=new Intent(getActivity(),SearchUser.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }


}
