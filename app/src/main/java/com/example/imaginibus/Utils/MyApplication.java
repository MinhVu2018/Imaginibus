package com.example.imaginibus.Utils;

import android.app.Application;

import com.example.imaginibus.Model.AlbumModel;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Model.VideoModel;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private List<ImageModel> listImage;
    private List<AlbumModel> listAlbum;
    private List<VideoModel> listVideo;
    private List<ImageModel> listImageFavorite;
    private List<VideoModel> listVideoFavorite;
    private List<AlbumModel> listFace;
    private List<ImageModel> listSecure;

    public int currentLayout;

    static public String share_preference_path = "com.example.imaginibus.PREFERENCES";
    static public String image_favorite_path = "FAVORITE_IMAGE_LIST";
    static public String video_favorite_path = "FAVORITE_VIDEO_LIST";

    public boolean setListImage(List<ImageModel> listImagePath) {
        this.listImage = listImagePath;
        return true;
    }

    public boolean removeImage(ImageModel item) {
        for (ImageModel imageModel : listImage) {
            if (imageModel.getImageUrl().equals(item.getImageUrl())) {
                listImage.remove(imageModel);
                break;
            }
        }

        for (AlbumModel albumModel : listAlbum) {
            boolean removed = false;
            if (albumModel.getAlbumName().equals(item.getAlbum())) {
                List<ImageModel> listImageInAlbum = albumModel.getListImage();
                for (ImageModel imageModel : listImageInAlbum) {
                    if (imageModel.getImageUrl().equals(item.getImageUrl())) {
                        listImageInAlbum.remove(imageModel);
                        removed = true;
                        break;
                    }
                }
                if (listImageInAlbum.size() == 0) {
                    listAlbum.remove(albumModel);
                }

                if (removed)
                    break;
            }
        }

        for (AlbumModel albumModel : listFace) {
            boolean removed = false;

            List<ImageModel> listImageInAlbum = albumModel.getListImage();
            for (ImageModel imageModel : listImageInAlbum) {
                if (imageModel.getImageUrl().equals(item.getImageUrl())) {
                    listImageInAlbum.remove(imageModel);
                    removed = true;
                    break;
                }
            }

            if (listImageInAlbum.size() == 0) {
                listFace.remove(albumModel);
            }

            if (removed)
                break;

        }
        return true;
    }
    public boolean setListAlbum(List<AlbumModel> listAlbum) {
        this.listAlbum = listAlbum;
        return true;
    }

    public void setListVideo(List<VideoModel> listVideo) {
        this.listVideo = listVideo;
    }

    public boolean setListImageFavorite(List<ImageModel> listImageFavorite) {
        this.listImageFavorite = listImageFavorite;
        return true;
    }

    public boolean setListVideoFavorite(List<VideoModel> listVideoFavorite){
        this.listVideoFavorite = listVideoFavorite;
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

    public void addTag(ImageModel item, String tag) {
        for (ImageModel image : listImage) {
            if (image.getImageUrl().equals(item.getImageUrl())) {
                image.setImageTag(tag);
            }
        }
    }

    public void addImageToFavorite(ImageModel item) {
        if (listImageFavorite == null) {
            listImageFavorite = new ArrayList<>();
        }

        listImageFavorite.add(item);
    }

    public void addVideoToFavorite(VideoModel item){
        if (listVideoFavorite == null)
            listVideoFavorite = new ArrayList<>();

        listVideoFavorite.add(item);
    }

    public void addImageToSecure(ImageModel item) {
        if (listSecure == null) {
            listSecure = new ArrayList<>();
        }

        listSecure.add(item);
    }

    public boolean removeImageFromFavorite(ImageModel image) {
        for (ImageModel item : listImageFavorite) {
            if (item.getImageUrl().equals(image.getImageUrl())) {
                listImageFavorite.remove(item);
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

    public boolean removeVideoFromFavorite(VideoModel video){
        for (VideoModel item : listVideoFavorite)
            if (item.getPath().equals(video.getPath())){
                listVideoFavorite.remove(item);
                return true;
            }
        return false;
    }

    public boolean isImageInFavorite(ImageModel image) {
        for (ImageModel item : listImageFavorite) {
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

    public boolean isVideoInFavorite(VideoModel video){
        for(VideoModel item : listVideoFavorite)
            if (item.getPath().equals(video.getPath()))
                return true;
        return false;
    }

    public boolean isImageInList(ImageModel item) {
        for (ImageModel image : listImage) {
            if (image.getImageUrl().equals(item.getImageUrl()))
                return true;
        }

        return false;
    }
    public int getListImageSize() { return listImage.size(); }
    public int getListVideoSize() { return listVideo.size(); }
    public List<ImageModel> getListImage() { return listImage; }
    public List<AlbumModel> getListAlbum() { return listAlbum; }
    public List<VideoModel> getListVideo() { return listVideo; }
    public List<ImageModel> getListImageFavorite() { return listImageFavorite; }
    public List<VideoModel> getListVideoFavorite() { return listVideoFavorite; }
    public List<ImageModel> getListSecure() { return listSecure; }
    public List<AlbumModel> getListFace() {return listFace; }
}
