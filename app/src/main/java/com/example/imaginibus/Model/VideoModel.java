package com.example.imaginibus.Model;

import java.io.Serializable;

public class VideoModel implements Serializable {
    private String path;

    public VideoModel(String path) {
        this.path = path;
    }

    public String getPath() { return path; }
}
