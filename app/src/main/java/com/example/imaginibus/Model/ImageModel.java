package com.example.imaginibus.Model;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageModel implements Serializable {
    private String url;
    private String date;
    private String album;
    private String[] data;
    // URL, DATE_ADDED, SIZE, WIDTH, HEIGHT, TITLE, BUCKET_DISPLAY_NAME, DISPLAY_NAME;
    private double latitude, longitude;

    public ImageModel(){ }

    public ImageModel(String[] d){
        data = new String[d.length];
        for (int i=0; i<d.length; i++)
            data[i] = d[i];
    }

    public void setImageLocation(Double latitude, Double longtitude) {
        this.latitude = latitude;
        this.longitude = longtitude;
    }
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
}
