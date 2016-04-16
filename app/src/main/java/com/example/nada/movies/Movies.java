package com.example.nada.movies;

/**
 * Created by nada on 25/12/2015.
 */
public class Movies {

    private String Overview ;
    private String Posters ;
    private String release_date ;
    private String Original_title ;
    private String Popularity ;
    private String Vote_count ;
    private String Vote_avarg ;
    private String _ID;

    public void set_ID(String id){this._ID=id;}
    public String get_ID(){return this._ID;}
    public void setOverview(String overview){  this.Overview=overview;}
    public String getOverview(){return this.Overview;}

    public void setPosters(String posters){  this.Posters="http://image.tmdb.org/t/p/w185" + posters;}
    public String getPosters(){return this.Posters;}

    public void setRelease_date(String release_date){  this.release_date=release_date;}
    public String getRelease_date(){return this.release_date;}

    public void setOriginal_title(String original_title){  this.Original_title=original_title;}
    public String getOriginal_title(){return this.Original_title;}

    public void setPopularity(String popularity){  this.Popularity=popularity;}
    public String getPopularity(){return this.Popularity;}

    public void setVote_count(String vote_count){  this.Vote_count=vote_count;}
    public String getVote_count(){return this.Vote_count;}

    public void setVote_avarg(String vote_avarg){  this.Vote_avarg=vote_avarg;}
    public String getVote_avarg(){return this.Vote_avarg;}
}
