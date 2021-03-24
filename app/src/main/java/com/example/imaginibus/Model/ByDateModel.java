package com.example.imaginibus.Model;

import java.util.List;

public class ByDateModel {
    private String date;
    private List<ImageModel> imageList;

    public void setByDateModel(String date, List<ImageModel> imageList) { this.date = date; this.imageList = imageList; }
    public List<ImageModel> getImageList() { return imageList; }
    public String getImageDate() { return date; }
}
