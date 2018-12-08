package com.sleepybear.mymoviecatalogue.utils;

import android.util.Log;

public class utils {

    public static String getDeviceLang(String country) {
        Log.d("ROS",country);
        if (country.equals("Indonesia")) {
            return "id";
        }
        return "en-US";
    }
}