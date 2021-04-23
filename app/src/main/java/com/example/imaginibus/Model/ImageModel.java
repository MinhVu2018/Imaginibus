package com.example.imaginibus.Model;

import java.io.Serializable;

public class ImageModel implements Serializable {
    private String url;
    private String date;
    private String album;
    private double latitude, longitude;
    
    public ImageModel(){ }

    public ImageModel(String url, String date, String album, double latitude, double longitude){
        this.url = url;
        this.date = date;
        this.album = album;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setImage(String url, String date, String album, double latitude, double longitude) {
        this.url = url; this.date = date; this.album = album;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getImageUrl() { return url; }
    public String getImageDate() { return date; }
    public String getAlbum() { return  album; }
    public Double getLat() { return latitude; }
    public Double getLong() { return longitude; }
}
