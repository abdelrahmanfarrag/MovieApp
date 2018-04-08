package com.example.mana.movieapp;

/**
 * Created by Abdelrahman on 8/18/2016.
 */
public class Encaps_poster {
    String img_path,overview,date,title;
    int id;
    double rate;
    public Encaps_poster(String img_path,String overview,String date,String title,int id,double rate)
    {
        this.setImg_path(img_path);
        this.setOverview(overview);
        this.setDate(date);
        this.setTitle(title);
        this.setId(id);
        this.setRate(rate);
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date= data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
