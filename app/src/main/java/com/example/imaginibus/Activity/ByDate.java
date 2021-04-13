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

import com.example.imaginibus.Adapter.ByDateAdapter;
import com.example.imaginibus.MyApplication;
import com.example.imaginibus.R;

public class ByDate extends AppCompatActivity {
    ImageButton btn_back; //back btn
    TextView num_image;
    RecyclerView recycler; //recycler view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_by_date);
        SetUpButton();

        //show all image
        recycler = findViewById(R.id.list);
        ByDateAdapter imageAdapter = new ByDateAdapter(this, R.id.list, ((MyApplication) this.getApplication()).getListImage());
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(imageAdapter);
        //set number image
        num_image = findViewById(R.id.num_images);
        num_image.setText(String.valueOf(((MyApplication) this.getApplication()).getListImageSize()) + " ");
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