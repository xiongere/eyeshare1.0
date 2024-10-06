package com.classify.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;

public class BuyActivity extends AppCompatActivity {
    ImageView imageView,imageView2;
    Bitmap bitmap,bitmap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        //imageView = (ImageView) findViewById(R.id.imageView_pay);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        try {
            //bitmap = BitmapFactory.decodeStream(getAssets().open("cy_pay.jpg"));
            bitmap2 = BitmapFactory.decodeStream(getAssets().open("xgj_pay.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //imageView.setImageBitmap(bitmap);
        imageView2.setImageBitmap(bitmap2);
    }
}