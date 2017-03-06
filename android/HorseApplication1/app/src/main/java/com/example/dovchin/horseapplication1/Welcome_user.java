package com.example.dovchin.horseapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by amour on 2/9/2017.
 */
public class Welcome_user extends AppCompatActivity {
    public  Button button25;
    public void init(){
        button25=(Button)findViewById(R.id.button25);
        button25.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent=new Intent(Welcome_user.this,WelcomeActivity.class);
                        startActivity(intent);

                    }}
        );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        init();

    }

}