package com.example.myapi.model;

import android.content.Intent;

import com.example.myapi.vista.VistaFavoritos;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DatosApi implements Serializable {



    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("poster_path")
    @Expose
    private String poster_path;

    @SerializedName("popularity")
    @Expose
    private double popularity;

    @SerializedName("vote_average")
    @Expose
    private double vote_average;

    @SerializedName("overview")
    @Expose
    private String overview;




    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {return title;
    }

    public void setTitle(String title) {this.title = title;}

    public String getPoster_path() {return poster_path;}

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Integer vote_average) {
        this.vote_average = vote_average;
    }




}
