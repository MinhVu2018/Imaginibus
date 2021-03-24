package com.example.imaginibus.Model;

public class ImageModel {
    private String url;
    private String date;

    public void setImage(String url, String date) { this.url = url; this.date = date; }
    public String getImageUrl() { return url; }
    public String getImageDate() { return date; }
}
