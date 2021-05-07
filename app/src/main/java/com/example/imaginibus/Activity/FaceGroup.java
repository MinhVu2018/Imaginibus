package com.example.imaginibus.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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
import com.example.imaginibus.Service.FaceDetection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
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
        listImageFace = ((MyApplication) this.getApplication()).getListFace();

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
}