package com.example.imaginibus;

import android.app.Application;

import com.example.imaginibus.Model.AlbumModel;
import com.example.imaginibus.Model.ImageModel;

import java.util.List;

public class MyApplication extends Application {
    private List<ImageModel> listImage;
    private List<AlbumModel> listAlbum;


    public boolean setListImage(List<ImageModel> listImagePath) {
        this.listImage = listImagePath;
        return true;
    }

    public boolean setListAlbum(List<AlbumModel> listAlbum) {
        this.listAlbum = listAlbum;
        return true;
    }

    public int getListImageSize() { return listImage.size(); }
    public List<ImageModel> getListImage() {
        return listImage;
    }
    public List<AlbumModel> getListAlbum() { return listAlbum; }
}
