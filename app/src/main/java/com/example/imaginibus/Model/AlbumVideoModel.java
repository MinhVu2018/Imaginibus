package com.example.imaginibus.Model;

import com.example.imaginibus.Activity.Video;

import java.util.ArrayList;
import java.util.List;

public class AlbumVideoModel {
    List<VideoModel> listVideo;
    String albumName;

    public AlbumVideoModel(List<VideoModel> listVideo, String albumName) {
        this.listVideo = listVideo;
        this.albumName = albumName;
    }

    public AlbumVideoModel(VideoModel video, String albumName) {
        this.listVideo = new ArrayList<>();
        this.listVideo.add(video);
        this.albumName = albumName;
    }

    public String getAlbumName() { return this.albumName; }
    public List<VideoModel> getListVideo() { return this.listVideo; }
    public void addVideo(VideoModel item) {
        this.listVideo.add(0, item);
    }
    public boolean containVideo(VideoModel item) {
        for (VideoModel videoModel : listVideo) {
            if (videoModel.getPath().equals(item.getPath()))
                return true;
        }

        return false;
    }
}
