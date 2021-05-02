package com.example.imaginibus.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.imaginibus.Adapter.ListAlbumAdapter;
import com.example.imaginibus.Model.AlbumModel;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.MyApplication;
import com.example.imaginibus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class FaceGroup extends AppCompatActivity {
    ImageButton btn_back, btn_menu;
    Hashtable<Integer, List<Integer>> listIdImage;
    List<AlbumModel> listImageFace;
    RecyclerView recyclerView;
    TextView numImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_face_group);

        //setup resource
        setupButton();
        listIdImage = new Hashtable<>();
        faceDetecting();
        setupAlbum();

        //setup adapter, view
        numImage = findViewById(R.id.num_images);
        numImage.setText(String.valueOf(listImageFace.size()) + " ");
        recyclerView = findViewById(R.id.list_album);
        ListAlbumAdapter listAlbumAdapter = new ListAlbumAdapter(this, R.id.list_album, listImageFace);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAlbumAdapter);
    }

    private void setupButton() {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FaceGroup.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void faceDetecting() {
        //Step 1, configure the face detector
        FaceDetectorOptions faceDetectorOptions = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                .enableTracking()
                .build();

        //Step 2, get an instance of face detector
        FaceDetector detector = FaceDetection.getClient(faceDetectorOptions);

        //Step 3, prepare the input image and process it
        faceTracking(detector);
    }

    private void faceTracking(FaceDetector detector) {
        //get all image of the computer
        List<ImageModel> allImage = ((MyApplication) this.getApplication()).getListImage();

        for (int i=0; i<10; i++) {
            //get the image
            ImageModel imageModel = allImage.get(i);

            //Convert to bitmap
            Bitmap src = BitmapFactory.decodeFile(imageModel.getImageUrl());

            //Convert to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            src.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            InputImage inputImage = InputImage.fromByteArray(data, 480, 360, 0, InputImage.IMAGE_FORMAT_NV21);
            //process image
            processImage(inputImage, detector);
        }
    }

    private void processImage(InputImage image, FaceDetector detector) {
        Task<List<Face>> result =
                detector.process(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<Face>>() {
                                    @Override
                                    public void onSuccess(List<Face> faces) {
                                        List<Integer> listId = new ArrayList<>();
                                        for (Face face : faces) {
                                            Log.i("ID", String.valueOf(face.getTrackingId()));
                                            listId.add(face.getTrackingId());
                                        }
                                        listIdImage.put(listIdImage.size(), listId);
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        listIdImage.put(listIdImage.size(), null);
                                    }
                                });
    }

    private void setupAlbum() {
        List<ImageModel> allImage = ((MyApplication) this.getApplication()).getListImage();
        Set<Integer> faceIdKey = listIdImage.keySet();
        listImageFace = new ArrayList<>();

        for (int key : faceIdKey) {
            List<Integer> faceIdVal = listIdImage.get(key);
            for (int id : faceIdVal) {
                for (int j=0; j<listImageFace.size(); j++) {
                    if (listImageFace.get(j).getAlbumName().equals(String.valueOf(id))) {
                        listImageFace.get(j).addImage(allImage.get(key));
                        break;
                    }
                    AlbumModel albumModel = new AlbumModel(allImage.get(key), String.valueOf(id));
                    listImageFace.add(albumModel);
                }
            }
        }
    }
}