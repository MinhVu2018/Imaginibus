package com.example.imaginibus;

import android.app.Application;

import com.example.imaginibus.Model.ImageModel;

import java.util.List;

public class MyApplication extends Application {
    private List<ImageModel> listImagePath;

    public List<ImageModel> getListImage() {
        return listImagePath;
    }

    public boolean setListImagePath(List<ImageModel> listImagePath) {
        this.listImagePath = listImagePath;
        return true;
    }

    public int getListImageSize() {
        return listImagePath.size();
    }
}
