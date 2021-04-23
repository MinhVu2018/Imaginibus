package com.example.imaginibus.Model;

import java.util.ArrayList;
import java.util.List;

public class VideoModel {
    private String path;
    private List<ImageModel> imageList;

    public VideoModel(String path) {
        this.path = path;
        imageList = new ArrayList<>();
    }

    public VideoModel(String path, List<ImageModel> imageList) {
        this.path = path;
        this.imageList = imageList;
    }

    public String getPath() { return path; }
    public List<ImageModel> getImage() { return imageList; }
}
