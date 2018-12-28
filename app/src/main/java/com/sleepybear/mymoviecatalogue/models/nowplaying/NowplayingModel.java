package com.sleepybear.mymoviecatalogue.models.nowplaying;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sleepybear.mymoviecatalogue.models.Result;

import java.util.List;

public class NowplayingModel {

    @Nullable
    @SerializedName("results")
    @Expose
    private final List<Result> results = null;

    @Nullable
    public List<Result> getResults() {
        return results;
    }

}