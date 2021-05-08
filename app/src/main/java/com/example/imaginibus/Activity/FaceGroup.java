package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.imaginibus.Adapter.ListAlbumAdapter;
import com.example.imaginibus.Model.AlbumModel;
import com.example.imaginibus.Utils.MyApplication;
import com.example.imaginibus.R;

import java.util.List;

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