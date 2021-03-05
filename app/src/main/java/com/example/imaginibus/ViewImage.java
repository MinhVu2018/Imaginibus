package com.example.imaginibus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ViewImage extends AppCompatActivity {
    ImageButton btn_back;
    ImageView img;
    float x1,x2,y1,y2;
    int ImgList[] = {R.drawable.ca_phe_review, R.drawable.bong_chuyen, R.drawable.da_lat, R.drawable.hoa_la, R.drawable.phan_thiet};
    int curImg = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_view_image);

        SetUpButton();
    }

    private void SetUpButton(){
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ViewImage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        img = (ImageView) findViewById(R.id.viewed_image);
        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if (x1 < x2){
                    if (curImg > 0)
                        curImg--;
                }
                else if (x1 > x2){
                    if (curImg < ImgList.length-1)
                        curImg++;
                }
                img.setImageResource(ImgList[curImg]);
                break;
        }
        return false;
    }
}