package com.example.dovchin.horseapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    public Button button_save, button_home;
    public void init(){
        button_save=(Button)findViewById(R.id.button_save);
        button_save.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent=new Intent(WelcomeActivity.this,User_detial.class);
                        startActivity(intent);

                    }}
        );

    }
    public void init2(){
        button_home=(Button)findViewById(R.id.button_home);
        button_home.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent=new Intent(WelcomeActivity.this,ViewActStallion.class);
                        startActivity(intent);
                    }}
        );
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user);
        init();
        init2();

    }

}
