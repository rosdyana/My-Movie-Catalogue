package com.sleepybear.mymoviecatalogue.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {
    public static String getDeviceLang(String country) {
//        Log.d("ROS", country);
        if (country.equals("Indonesia")) {
            return "id";
        }
        return "en-US";
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());
        return mdformat.format(calendar.getTime());
    }

}