package com.classify.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView;
    Bitmap bitmap;
    EditText editText;
    Button button_login, button_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        imageView = (ImageView) findViewById(R.id.imageView_logo);
        editText = (EditText) findViewById(R.id.inviteText);

        button_login = (Button) findViewById(R.id.button_login);
        button_buy = (Button) findViewById(R.id.button_buy);

        button_login.setOnClickListener(this);
        button_buy.setOnClickListener(this);

        try {
            bitmap = BitmapFactory.decodeStream(getAssets().open("mask.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);


    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_login:

                String invitecode = editText.getText().toString();

                if (invitecode.equals("1015")){
                    Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
                    Intent choose_activity = new Intent(this, ChooseActivity.class);
                    startActivity(choose_activity);
                }
                else{
                    Toast.makeText(this,"登录失败",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.button_buy:
                Toast.makeText(this,"购买",Toast.LENGTH_SHORT).show();
                Intent buy_activity = new Intent(this,BuyActivity.class);
                startActivity(buy_activity);
                break;
        }
    }
}