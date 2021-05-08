package com.example.imaginibus.Utils;

import android.graphics.Rect;

import java.io.Serializable;

public class MyRect implements Serializable {
    public int top, right, bottom, left;

    public MyRect(Rect rect) {
        this.top = rect.top;
        this.right = rect.right;
        this.bottom = rect.bottom;
        this.left = rect.left;
    }
}
