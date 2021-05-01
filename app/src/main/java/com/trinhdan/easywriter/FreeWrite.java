package com.trinhdan.easywriter;

import android.os.Debug;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Represents a free write made by the user.
 */
public class FreeWrite {

    public static final DateFormat DATE_FORMAT = DateFormat.getDateInstance();

    private int id;
    private Date date;
    private String title;
    private String link;


    private String storyPics;
    private double duration;
    private boolean favorite;

    public FreeWrite(){
        this.id = 0;
        this.date = new Date();
        this.title = "";
        this.link = "";
        this.storyPics = "";
        this.duration = 0;
        this.favorite = false;
    }

    public FreeWrite(int id, Date date, String title, String storyPics, String link, double duration, boolean favorite){
        this.id = id;
        this.date = date;
        this.title = title;
        this.storyPics = storyPics;
        this.link = link;
        this.duration = duration;
        this.favorite = favorite;
    }

    public String getStoryPics() {
        return storyPics;
    }

    public void setStoryPics(String storyPics) {
        this.storyPics = storyPics;
    }

    public Date getDate() {
        return date;
    }

    public String getDateAsString() {
        return date.toString();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(String date) {
        try{
            this.date = FreeWrite.DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            Log.e("Date Issue in FreeWrite", "Could not parse the given date");
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    // overload for setFavorite since SQLite stores booleans as a binary 0 or 1.
    public void setFavorite(int favoriteValue){
        this.favorite = favoriteValue >= 1 ? true : false;
    }
}
