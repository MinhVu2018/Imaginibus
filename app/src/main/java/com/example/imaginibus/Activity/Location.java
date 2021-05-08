package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.example.imaginibus.Adapter.ListAlbumAdapter;
import com.example.imaginibus.Model.AlbumModel;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Utils.MyApplication;
import com.example.imaginibus.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Location extends AppCompatActivity {
    ImageButton btn_back;
    RecyclerView recyclerView;
    List<AlbumModel> listImageLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_location);

        //setup button
        SetUpButton();
        //setup location
        findListImageLocation();

        //setup adapter
        recyclerView = (RecyclerView) findViewById(R.id.list_location);
        ListAlbumAdapter listAlbumAdapter = new ListAlbumAdapter(this, R.id.list_album, listImageLocation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAlbumAdapter);
    }

    private void SetUpButton() {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Location.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findListImageLocation() {
        //geocoder
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        String city = "Location not found";

        //allocate memory
        listImageLocation = new ArrayList<>();
        //get all image
        List<ImageModel> allImage = ((MyApplication) this.getApplication()).getListImage();

        for (ImageModel item : allImage) {
            //find the city of this item
            if (item.getLat() != 0 || item.getLong() != 0) {
                try {
                    address = geocoder.getFromLocation(item.getLat(), item.getLong(), 1);
                    city = address.get(0).getCountryName();
                    if (city == null) {
                        city = "Location not found";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            addImageToAlbum(item, city);
            city = "Location not found";
        }
    }

    private void addImageToAlbum(ImageModel image, String albumName) {
        int albumSize = listImageLocation.size();
        for (int i = 0; i < albumSize; i++) {
            if (listImageLocation.get(i).getAlbumName().equals(albumName)) {
                listImageLocation.get(i).addImage(image);
                return;
            }
        }

        AlbumModel newAlbum = new AlbumModel(image, albumName);
        listImageLocation.add(newAlbum);
        return;
    }
}