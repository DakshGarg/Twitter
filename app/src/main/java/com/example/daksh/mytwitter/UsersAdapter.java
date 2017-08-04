package com.example.daksh.mytwitter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Daksh Garg on 7/31/2017.
 */

public class UsersAdapter extends ArrayAdapter<Users> {

    ArrayList<Users> usersArrayList;
    Context context;
    public UsersAdapter(@NonNull Context context,ArrayList<Users> usersArrayList) {
        super(context, 0);
        this.usersArrayList=usersArrayList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return usersArrayList.size();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.usersadapter, null);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.usersnameTextView);
            TextView screenNameTextView = (TextView) convertView.findViewById(R.id.usersscreen_nameTextView);
            CircleImageView circleImageView=(CircleImageView)convertView.findViewById(R.id.userImageView);
            UsersViewHolder usersViewHolder = new UsersViewHolder(nameTextView ,screenNameTextView,circleImageView);
            convertView.setTag(usersViewHolder);

        }
        UsersViewHolder usersViewHolder = (UsersViewHolder) convertView.getTag();
        Users u =usersArrayList.get(position);
        usersViewHolder.nameTextView.setText(u.name);
        usersViewHolder.screenNameTextView.setText("@"+u.screen_name);
        String url=u.profile_image_url.replaceAll("normal","bigger");
        Picasso.with(context).load(url).into(usersViewHolder.circleImageView);

        return convertView;
    }

    static class UsersViewHolder {
        TextView nameTextView;
        TextView screenNameTextView;
        CircleImageView circleImageView;

        public UsersViewHolder(TextView nameTextView, TextView screenNameTextView, CircleImageView circleImageView) {
            this.nameTextView = nameTextView;
            this.screenNameTextView = screenNameTextView;
            this.circleImageView = circleImageView;

        }
    }
}
