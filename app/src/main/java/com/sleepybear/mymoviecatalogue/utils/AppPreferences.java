package com.sleepybear.mymoviecatalogue.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sleepybear.mymoviecatalogue.R;

public class AppPreferences {
    SharedPreferences sharedPreferences;
    Context context;

    public AppPreferences(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setFavorite(boolean isFavorite) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String key = context.getResources().getString(R.string.is_favorite);
        editor.putBoolean(key, isFavorite);
        editor.apply();
    }

    public Boolean isFavorite() {
        String key = context.getResources().getString(R.string.is_favorite);
        return sharedPreferences.getBoolean(key, true);
    }
}
