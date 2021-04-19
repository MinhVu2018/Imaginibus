package com.example.imaginibus;

import android.app.Application;

import com.example.imaginibus.Model.AlbumModel;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Model.VideoModel;

import java.util.List;

public class MyApplication extends Application {
    private List<ImageModel> listImage;
    private List<AlbumModel> listAlbum;
    private List<VideoModel> listVideo;
    private List<ImageModel> listFavorite;

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

    public void addImageToFavorite(ImageModel item) {
        listFavorite.add(item);
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

    public boolean isImageInFavorite(ImageModel image) {
        for (ImageModel item : listFavorite) {
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
}
