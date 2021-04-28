package com.trinhdan.easywriter;

import java.util.Date;

/**
 * Represents a free write made by the user.
 */
public class Freewrite {

    Date date;
    String title;
    String link;
    double duration;
    boolean favorite;

    public Freewrite(Date date, String title, String link, double duration, boolean favorite){
        this.date = date;
        this.title = title;
        this.link = link;
        this.duration = duration;
        this.favorite = favorite;
    }
}
