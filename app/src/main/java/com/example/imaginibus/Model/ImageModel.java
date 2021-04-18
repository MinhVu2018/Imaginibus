package com.example.imaginibus.Model;

import java.io.Serializable;

public class ImageModel implements Serializable {
    private String url;
    private String date;
    private String album;

    public ImageModel(){ }

    public ImageModel(String url, String date, String album){
        this.url = url;
        this.date = date;
        this.album = album;
    }

    public void setImage(String url, String date, String album) { this.url = url; this.date = date; this.album = album; }
    public String getImageUrl() { return url; }
    public String getImageDate() { return date; }
    public String getAlbum() { return  album; }
}
