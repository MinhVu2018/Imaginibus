package com.example.imaginibus.Model;

import java.io.Serializable;

public class ImageModel implements Serializable {
    private String url;
    private String date;

    public void setImage(String url, String date) { this.url = url; this.date = date; }
    public String getImageUrl() { return url; }
    public String getImageDate() { return date; }
}
