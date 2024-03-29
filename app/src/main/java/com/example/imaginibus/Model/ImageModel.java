package com.example.imaginibus.Model;

import android.database.Cursor;
import android.graphics.Rect;
import android.provider.MediaStore;

import com.example.imaginibus.Utils.MyRect;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageModel implements Serializable {
    private String[] data;
    public MyRect rect;
    public boolean containFace;

    // URL, DATE_ADDED, SIZE, WIDTH, HEIGHT, TITLE, BUCKET_DISPLAY_NAME, DISPLAY_NAME;
    private double latitude, longitude;
    private String tag;

    public ImageModel(){ }

    public ImageModel(String[] d){
        data = new String[d.length];
        System.arraycopy(d, 0, data, 0, d.length);
    }

    public void setImageTag(String tag) {
        this.tag = tag;
    }

    public void setImageLocation(Double latitude, Double longtitude) {
        this.latitude = latitude;
        this.longitude = longtitude;
    }

    public void setRect(MyRect rect) { this.rect = rect; }
    public void setContainFace(boolean val) { this.containFace = val; }
    public String getImageUrl() { return data[0]; }
    public String getImageDate() {
        //Date format
        String format = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);

        return formatter.format(new Date(Long.parseLong(data[1]) * 1000L));
    }
    public String getImageDateTime() {
        //Date format
        String format = "MM-dd-yyyy hh-mm-ss";
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);

        return formatter.format(new Date(Long.parseLong(data[1]) * 1000L));
    }
    public String getSize() { return data[2]; }
    public String getWidth() { return data[3]; }
    public String getHeight() { return data[4]; }
    public String getTitle() { return data[5]; }
    public String getAlbum() { return data[6]; }
    public String getName() { return data[7]; }
    public Double getLat() { return latitude; }
    public Double getLong() { return longitude; }
    public String getTag() { return tag; }
}
