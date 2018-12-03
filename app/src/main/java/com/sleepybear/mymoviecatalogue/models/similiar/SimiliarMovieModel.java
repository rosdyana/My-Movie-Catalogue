package com.sleepybear.mymoviecatalogue.models.similiar;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimiliarMovieModel {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("similiarResults")
    @Expose
    private List<SimiliarResult> similiarResults = null;
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

    public List<SimiliarResult> getSimiliarResults() {
        return similiarResults;
    }

    public void setSimiliarResults(List<SimiliarResult> similiarResults) {
        this.similiarResults = similiarResults;
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