package com.example.daksh.mytwitter;

import android.content.Context;
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
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Daksh Garg on 8/4/2017.
 */

public class FollowersAdapter extends ArrayAdapter<Followers.FollowersObject> {
    ArrayList<Followers.FollowersObject> followersArrayList;
    Context context;
   ButtonClickListener listener;

    void setButtonClickListener(ButtonClickListener listener)
    {
        this.listener=listener;
    }

    public FollowersAdapter(Context context,ArrayList<Followers.FollowersObject> followersArrayList) {
        super(context, 0);
        this.followersArrayList = followersArrayList;
        this.context=context;
    }

   @Override
    public int getCount() {
       return followersArrayList.size();
   }

       @NonNull
       @Override
       public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
           if(convertView==null)
           {
               convertView = LayoutInflater.from(context).inflate(R.layout.friendsadapter, null);
               TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
               TextView screenNameTextView = (TextView) convertView.findViewById(R.id.screen_nameTextView);
               CircleImageView circleImageView=(CircleImageView)convertView.findViewById(R.id.friendImageView);
               FancyButton button=(FancyButton) convertView.findViewById(R.id.following_button);
               button.setFocusable(false);
               FollowerViewHolder followerViewHolder = new FollowerViewHolder(nameTextView ,screenNameTextView,circleImageView,button);
               convertView.setTag(followerViewHolder);

           }
           FollowerViewHolder followerViewHolder = (FollowerViewHolder) convertView.getTag();
           Followers.FollowersObject f =followersArrayList.get(position);
           followerViewHolder.nameTextView.setText(f.name);
           followerViewHolder.screenNameTextView.setText("@"+f.screen_name);
           String url=f.profile_image_url.replaceAll("_normal","");
           Picasso.with(context).load(url).into(followerViewHolder.circleImageView);
           if(f.following){
               followerViewHolder.button.setText("Following");
               followerViewHolder.button.setTextColor(context.getResources().getColor(R.color.tw__composer_white));
               followerViewHolder.button.setBackgroundColor(context.getResources().getColor(R.color.tw__composer_blue));

           }else{
               followerViewHolder.button.setText("Follow");
               followerViewHolder.button.setTextColor(context.getResources().getColor(R.color.tw__composer_blue_text));
               followerViewHolder.button.setBackgroundColor(context.getResources().getColor(R.color.tw__composer_white));
           }
           followerViewHolder.button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(listener!=null) {
                       listener.onButtonClicked(view,position);
                   }

               }
           });


           return convertView;
       }


    static class FollowerViewHolder {
        TextView nameTextView;
        TextView screenNameTextView;
        CircleImageView circleImageView;
        FancyButton button;

        public FollowerViewHolder(TextView nameTextView, TextView screenNameTextView, CircleImageView circleImageView,FancyButton button) {
            this.nameTextView = nameTextView;
            this.screenNameTextView = screenNameTextView;
            this.circleImageView = circleImageView;
            this.button=button;

        }
    }


    interface ButtonClickListener{
        void onButtonClicked(View view, int position);
    }
}
