package com.example.daksh.mytwitter;

import java.util.ArrayList;

/**
 * Created by Daksh Garg on 7/26/2017.
 */

public class Trend {

    public ArrayList<TrendObject> trends;

    public static class TrendObject{
        public String name;
        public String query;
    }
}
