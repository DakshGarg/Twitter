package com.example.daksh.mytwitter;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUser extends AppCompatActivity {
    SearchView searchView;
    SearchManager searchManager;
    ApiInterface apiInterface;
    ListView usersListView;
    ArrayList<Users> usersArrayList;
    UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user);
        usersListView = (ListView) findViewById(R.id.usersListView);
        usersArrayList = new ArrayList<>();
        adapter = new UsersAdapter(this, usersArrayList);
        usersListView.setAdapter(adapter);
        searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        apiInterface = ApiClient.getApiInterface();
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("check",usersArrayList.get(i).name);
                Intent intent=new Intent(SearchUser.this,UserDisplayInfoActivity.class);
                intent.putExtra("info",usersArrayList.get(i));
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_2, menu);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconified(false);
        try {
            Field searchField = SearchView.class.getDeclaredField("mCloseButton");
            searchField.setAccessible(true);
            ImageView imageView = (ImageView) searchField.get(searchView);
            imageView.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e("TAG", "Error finding close button", e);
        }
//        int searchImgId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
//        ImageView searchImage = (ImageView) searchView.findViewById(searchImgId);
//        searchImage.setVisibility(View.GONE);

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(this.getComponentName()));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                if (s.length()==2) {
                    usersArrayList.clear();
                    adapter.notifyDataSetChanged();
                    return true;
                }
//                Toast.makeText(SearchUser.this,s,Toast.LENGTH_SHORT).show();
//                Toast.makeText(SearchUser.this,s.length()+"",Toast.LENGTH_SHORT).show();

                Call<List<Users>> call = apiInterface.getUsers(s);
                call.enqueue(new Callback<List<Users>>() {
                    @Override
                    public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                        if (response.isSuccessful()) {
                            usersArrayList.clear();


                            usersArrayList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Users>> call, Throwable t) {

                    }
                });

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
