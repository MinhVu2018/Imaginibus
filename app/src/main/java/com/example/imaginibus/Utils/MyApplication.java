package com.example.imaginibus.Utils;

import android.app.Application;
import android.graphics.Rect;

import com.example.imaginibus.Model.AlbumModel;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Model.VideoModel;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MyApplication extends Application {
    private List<ImageModel> listImage;
    private List<AlbumModel> listAlbum;
    private List<VideoModel> listVideo;
    private List<ImageModel> listFavorite;
    private List<AlbumModel> listFace;
    private List<ImageModel> listSecure;

    public int currentLayout;

    public boolean setListImage(List<ImageModel> listImagePath) {
        this.listImage = listImagePath;
        return true;
    }

    public boolean setListAlbum(List<AlbumModel> listAlbum) {
        this.listAlbum = listAlbum;
        return true;
    }

    public boolean setListVideo(List<VideoModel> listVideo) {
        this.listVideo = listVideo;
        return true;
    }

    public boolean setListFavorite(List<ImageModel> listFavorite) {
        this.listFavorite = listFavorite;
        return true;
    }

    public boolean setListFace(List<AlbumModel> listFace) {
        this.listFace = listFace;
        return true;
    }

    public boolean setListSecure(List<ImageModel> listSecure) {
        this.listSecure = listSecure;
        return true;
    }
    public void addImageToFavorite(ImageModel item) {
        if (listFavorite == null) {
            listFavorite = new ArrayList<>();
        }

        listFavorite.add(item);
    }

    public void addImageToSecure(ImageModel item) {
        if (listSecure == null) {
            listSecure = new ArrayList<>();
        }

        listSecure.add(item);
    }

    public boolean removeImageFromFavorite(ImageModel image) {
        for (ImageModel item : listFavorite) {
            if (item.getImageUrl().equals(image.getImageUrl())) {
                listFavorite.remove(item);
                return true;
            }
        }

        return false;
    }

    public boolean removeImageFromSecure(ImageModel image) {
        for (ImageModel item : listSecure) {
            if (item.getImageUrl().equals(image.getImageUrl())) {
                listSecure.remove(item);
                return true;
            }
        }

        return false;
    }

    public boolean isImageInFavorite(ImageModel image) {
        for (ImageModel item : listFavorite) {
            if (item.getImageUrl().equals(image.getImageUrl())) {
                return true;
            }
        }

        return false;
    }

    public boolean isImageInSecure(ImageModel image) {
        for (ImageModel item : listSecure) {
            if (item.getImageUrl().equals(image.getImageUrl())) {
                return true;
            }
        }

        return false;
    }

    public int getListImageSize() { return listImage.size(); }
    public List<ImageModel> getListImage() {
        return listImage;
    }
    public List<AlbumModel> getListAlbum() { return listAlbum; }
    public List<VideoModel> getListVideo() { return listVideo; }
    public List<ImageModel> getListFavorite() { return listFavorite; }
    public List<AlbumModel> getListFace() {return listFace; }
    public List<ImageModel> getListSecure() { return listSecure; }
}
