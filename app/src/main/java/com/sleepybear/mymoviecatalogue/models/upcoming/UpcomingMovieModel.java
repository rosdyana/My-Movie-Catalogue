package com.sleepybear.mymoviecatalogue.models.upcoming;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sleepybear.mymoviecatalogue.models.Result;

import java.util.List;

public class UpcomingMovieModel {

    @Nullable
    @SerializedName("results")
    @Expose
    private final List<Result> results = null;

    @Nullable
    public List<Result> getResults() {
        return results;
    }

}