package com.example.imaginibus.Service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.IBinder;


import androidx.annotation.Nullable;

import com.example.imaginibus.FaceComparator;
import com.example.imaginibus.Model.AlbumModel;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.MyApplication;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FaceGrouping extends IntentService {
    HashMap<String, String> faceApiKey;
    String faceApiUrl;
    List<Boolean> listFace;
    List<ImageModel> listAllImage;
    HashMap<Integer, Rect> faceRect;
    List<AlbumModel> listAlbumFace;
    List<Integer> checkedImage;

    public FaceGrouping() {
        super("FaceGrouping");

        faceApiKey = new HashMap<>();
        faceApiKey.put("api_key", "5Csi1FBARPtwqyri_ddAf8q_ETH1ufQm");
        faceApiKey.put("api_secret", "I3TQFdu9GieiJDvz9pdYjnSpXD-tc5Re");
        faceApiUrl = "https://api-us.faceplusplus.com/facepp/v3/compare";
        listAlbumFace = new ArrayList<>();
        checkedImage = new ArrayList<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        listFace = (List<Boolean>) intent.getSerializableExtra("AVAILABLE_FACES");
        faceRect = (HashMap<Integer, Rect>) intent.getSerializableExtra("FACES_RECT");
        listAllImage = ((MyApplication) this.getApplication()).getListImage();

        for (int i = 0; i<listFace.size(); i++) {
            //if this image is checked --> continue
            if (checkedImage.contains(i))
                continue;
            else checkedImage.add(i);

            //if this image contain a face
            if (listFace.get(i)) {
                //create a new album for that image
                listAlbumFace.add(new AlbumModel(listAllImage.get(i),"Person " + (listAlbumFace.size() + 1)));

                //find all relevant without checked
                for (int j = i + 1; j<listFace.size(); j++) {
                    if (checkedImage.contains(j))
                        continue;

                    if (listFace.get(j))
                        compareTwoImage(listAllImage.get(i), listAllImage.get(j), faceRect.get(i), faceRect.get(j), i, j, listAlbumFace.size() - 1);
                }
            }
        }

        ((MyApplication) this.getApplication()).setListFace(listAlbumFace);
        saveFaceGroup();
    }

    private synchronized void compareTwoImage(ImageModel imageOne, ImageModel imageTwo, Rect imgOne, Rect imgTwo, int posOne, int posTwo, int curAlbum) {
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

        try{
            byte[] result = faceComparator.post(faceApiUrl, faceApiKey, byteMap);
            String str = new String(result);
            JSONObject object = new JSONObject(str);
            int confidence = object.getInt("confidence");

            if (confidence >= 50) {
                listAlbumFace.get(curAlbum).addImage(imageTwo);
                //mark the image as checked
                checkedImage.add(posTwo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFaceGroup() {
        //SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.imaginibus.PREFERENCES", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString("FACE_LIST", gson.toJson(listAlbumFace));
        editor.commit();
    }
}
