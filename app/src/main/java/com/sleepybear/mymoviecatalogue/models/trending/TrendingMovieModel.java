package com.sleepybear.mymoviecatalogue.models.trending;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrendingMovieModel {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("trendingResults")
    @Expose
    private List<TrendingResult> trendingResults = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<TrendingResult> getTrendingResults() {
        return trendingResults;
    }

    public void setTrendingResults(List<TrendingResult> trendingResults) {
        this.trendingResults = trendingResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }



}