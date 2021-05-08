package com.example.imaginibus.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumModel {
    List<ImageModel> listImage;
    String albumName;

    public AlbumModel(List<ImageModel> listImage, String albumName) {
        this.listImage = listImage;
        this.albumName = albumName;
    }

    public AlbumModel(ImageModel image, String albumName) {
        this.listImage = new ArrayList<>();
        this.listImage.add(image);
        this.albumName = albumName;
    }

    public String getAlbumName() { return this.albumName; }
    public List<ImageModel> getListImage() { return this.listImage; }
    public void addImage(ImageModel item) {
        this.listImage.add(item);
    }
}
