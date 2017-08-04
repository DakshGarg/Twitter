package com.example.daksh.mytwitter;

import java.io.Serializable;

/**
 * Created by Daksh Garg on 8/4/2017.
 */

public class Profile implements Serializable {
    public String id_str;
    public String name;
    public String screen_name;
    public String location;
    public String description;
    public long followers_count;
    public long friends_count;
    public String profile_image_url;
    public String profile_banner_url;


    public Profile(String id_str, String name, String screen_name, String location,
                   String description, long followers_count, long friends_count, String profile_image_url, String profile_banner_url) {
        this.id_str = id_str;
        this.name = name;
        this.screen_name = screen_name;
        this.location = location;
        this.description = description;
        this.followers_count = followers_count;
        this.friends_count = friends_count;
        this.profile_image_url = profile_image_url;
        this.profile_banner_url = profile_banner_url;
    }
}
