package com.classify.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class ChooseActivity extends AppCompatActivity implements View.OnClickListener{


    Button mButtonf;
    Button mButtonz;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        mButtonf = (Button) findViewById(R.id.button_f);
        mButtonz = (Button) findViewById(R.id.button_z);


        mButtonf.setOnClickListener(this);
        mButtonz.setOnClickListener(this);

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_f:
                Toast.makeText(this,"家属",Toast.LENGTH_SHORT).show();
                Intent main_activity = new Intent(this, MainActivity.class);
                startActivity(main_activity);

                break;
            case R.id.button_z:
                Toast.makeText(this,"该功能还在开发中",Toast.LENGTH_SHORT).show();

                break;
        }
    }




}