package com.example.daksh.mytwitter;

import java.util.ArrayList;

/**
 * Created by Daksh Garg on 7/26/2017.
 */

public class Friends {
   public String next_cursor_str;


    public ArrayList<FriendObject> users;

    public static class FriendObject{
        public String id_str;
        public String name;
        public String screen_name;
        public String profile_image_url;
        public String location;
        public String description;
        public long followers_count;
        public long friends_count;
        public String profile_banner_url;
        public boolean following;

        public FriendObject(String name, String screen_name, String profile_image_url) {
            this.name = name;
            this.screen_name = screen_name;
            this.profile_image_url = profile_image_url;
        }
    }

}
