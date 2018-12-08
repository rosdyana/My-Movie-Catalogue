package com.sleepybear.mymoviecatalogue.utils;

public class utils {

    public static String getDeviceLang(String country) {
        if (country == "Indonesia") {
            return "id";
        }
        return "en-US";
    }
}