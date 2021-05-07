package com.example.imaginibus.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.JsonReader;
import android.util.Log;



import com.example.imaginibus.FaceComparator;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.MyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class FaceGrouping extends Service {
    HashMap<String, String> faceApiKey;
    String faceApiUrl;
    List<Boolean> listFace;
    List<ImageModel> listAllImage;
    HashMap<Integer, Rect> faceRect;

    @Override
    public void onCreate() {
        faceApiKey = new HashMap<>();
        faceApiKey.put("api_key", "5Csi1FBARPtwqyri_ddAf8q_ETH1ufQm");
        faceApiKey.put("api_secret", "I3TQFdu9GieiJDvz9pdYjnSpXD-tc5Re");
        faceApiUrl = "https://api-us.faceplusplus.com/facepp/v3/compare";
    }

    @Override
    public void onStart(Intent intent, int startId) {
        listFace = (List<Boolean>) intent.getSerializableExtra("AVAILABLE_FACES");
        faceRect = (HashMap<Integer, Rect>) intent.getSerializableExtra("FACES_RECT");
        listAllImage = ((MyApplication) this.getApplication()).getListImage();

        for (int i = 0; i<listFace.size(); i++) {
            if (listFace.get(i)) {
                for (int j = i + 1; j<listFace.size(); j++) {
                    if (listFace.get(j)) {
                        compareTwoImage(listAllImage.get(i), listAllImage.get(j), faceRect.get(i), faceRect.get(j));
                    }
                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void compareTwoImage(ImageModel imageOne, ImageModel imageTwo, Rect imgOne, Rect imgTwo) {
        FaceComparator faceComparator = new FaceComparator();

        //insert image to bit map
        Bitmap bitmapOne = BitmapFactory.decodeFile(imageOne.getImageUrl());
        Bitmap bitmapTwo = BitmapFactory.decodeFile(imageTwo.getImageUrl());

        //crop the face
        bitmapOne = Bitmap.createBitmap(bitmapOne, imgOne.left, imgOne.top, imgOne.right - imgOne.left, imgOne.bottom - imgOne.top);
        bitmapTwo = Bitmap.createBitmap(bitmapTwo, imgTwo.left, imgTwo.top, imgTwo.right - imgTwo.left, imgTwo.bottom - imgTwo.top);
        byte[] buffOne;
        byte[] buffTwo;

        //resize image if it is too big
        if ((Integer.valueOf(bitmapOne.getWidth()) > 4000) || (Integer.valueOf(bitmapOne.getHeight()) > 4000)) {
            bitmapOne = Bitmap.createScaledBitmap(bitmapOne, bitmapOne.getWidth() / 4, bitmapOne.getHeight() / 4, true);
        }
        if ((Integer.valueOf(bitmapTwo.getWidth()) > 4000) || (Integer.valueOf(bitmapTwo.getHeight()) > 4000)) {
            bitmapTwo = Bitmap.createScaledBitmap(bitmapTwo, bitmapTwo.getWidth() / 4, bitmapTwo.getHeight() / 4, true);
        }

        //convert bitmap to byte
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        bitmapOne.compress(Bitmap.CompressFormat.PNG, 100, stream1);;
        bitmapTwo.compress(Bitmap.CompressFormat.PNG, 100, stream2);;
        buffOne = stream1.toByteArray();
        buffTwo = stream2.toByteArray();
        bitmapOne.recycle();
        bitmapTwo.recycle();

        //put buffer to a hashmap to post for response
        HashMap<String, byte[]> byteMap = new HashMap<>();
        byteMap.put("image_file1", buffOne);
        byteMap.put("image_file2", buffTwo);

        //must post in a thread, if not it will have exception
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    byte[] bacd = faceComparator.post(faceApiUrl, faceApiKey, byteMap);
                    String str = new String(bacd);
                    JSONObject object = new JSONObject(str);
                    int confidence = object.getInt("confidence");
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
