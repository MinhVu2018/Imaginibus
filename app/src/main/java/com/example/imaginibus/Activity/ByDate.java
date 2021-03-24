package com.example.imaginibus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.imaginibus.Adapter.ByDateAdapter;
import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ByDate extends AppCompatActivity {
    ImageButton btn_back; //back btn
    TextView num_image;
    RecyclerView recycler; //recycler view
    List<ImageModel> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_by_date);
        SetUpButton();

        //read all image from external and internal
        externalReadImage();
        //internalReadImage();


        //number of image
        num_image = findViewById(R.id.num_images);
        num_image.setText(String.format("%s ", imageList.size()));

        //show all image
        recycler = findViewById(R.id.list);
        ByDateAdapter imageAdapter = new ByDateAdapter(this, R.id.list, imageList);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(imageAdapter);
    }

    private void externalReadImage() {
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED };
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();
        //Date format
        String format = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int dateColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);

            String dateTaken = cursor.getString(dateColumnIndex);
            Log.i("DATE", dateTaken);
            String date = formatter.format(new Date(Long.parseLong(dateTaken) * 1000L));

            //Store the path of the image
            ImageModel imageModel = new ImageModel();
            imageModel.setImage(cursor.getString(dataColumnIndex), date);


            imageList.add(0, imageModel);
        }
        // The cursor should be freed up after use with close()

        cursor.close();
    }

//    private void internalReadImage() {
//        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED };
//        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
//        //Stores all the images from the gallery in Cursor
//        Cursor cursor = getContentResolver().query(
//                MediaStore.Images.Media.INTERNAL_CONTENT_URI, columns, null,
//                null, orderBy);
//        //Total number of images
//        int count = cursor.getCount();
//
//        for (int i = arrPath.length; i < arrPath.length + count; i++) {
//            cursor.moveToPosition(i - arrPath.length);
//            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//            //Store the path of the image
//            arrPath[i] = cursor.getString(dataColumnIndex);
//            Log.i("PATH", arrPath[i]);
//        }
//        // The cursor should be freed up after use with close()
//        cursor.close();
//    }

    private void SetUpButton() {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ByDate.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}