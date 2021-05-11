package com.example.imaginibus.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VideoModel implements Serializable {
    private String[] data;

    public VideoModel(String[] d){
        data = new String[d.length];
        System.arraycopy(d, 0, data, 0, d.length);
    }
    public String getPath() { return data[0]; }
    public String getVideoDate() {
        //Date format
        String format = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);

        return formatter.format(new Date(Long.parseLong(data[1]) * 1000L));
    }
    public String getVideoDateTime() {
        //Date format
        String format = "MM-dd-yyyy hh-mm-ss";
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);

        return formatter.format(new Date(Long.parseLong(data[1]) * 1000L));
    }
    public String getSize() { return data[2]; }
    public String getWidth() { return data[3]; }
    public String getHeight() { return data[4]; }
    public String getTitle() { return data[5]; }
    public String getName() { return data[6]; }
}
