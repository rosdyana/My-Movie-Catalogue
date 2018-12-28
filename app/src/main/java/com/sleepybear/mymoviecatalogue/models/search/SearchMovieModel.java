package com.sleepybear.mymoviecatalogue.models.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sleepybear.mymoviecatalogue.models.Result;

import java.util.List;

public class SearchMovieModel {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<Result> searchResults = null;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<Result> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<Result> searchResults) {
        this.searchResults = searchResults;
    }

}