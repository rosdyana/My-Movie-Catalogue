package com.sleepybear.mymoviecatalogue.models.search;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sleepybear.mymoviecatalogue.models.Result;

import java.util.List;

public class SearchMovieModel {

    @Nullable
    @SerializedName("results")
    @Expose
    private final List<Result> searchResults = null;

    @Nullable
    public List<Result> getSearchResults() {
        return searchResults;
    }

}