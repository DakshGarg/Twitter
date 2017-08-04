package com.example.daksh.mytwitter;

import java.util.ArrayList;

/**
 * Created by Daksh Garg on 7/26/2017.
 */

public class Followers {
    String next_cursor_str;


    public ArrayList<FollowersObject> users;

    public static class FollowersObject{
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
    }
}
