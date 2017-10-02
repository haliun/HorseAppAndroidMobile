package com.example.dovchin.horseapplication1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.dovchin.horseapplication1.Activities.MainActivity;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.methods.AddFemaleHorseActivity;
import com.example.dovchin.horseapplication1.methods.SearchColt;
import com.example.dovchin.horseapplication1.methods.SearchFemaleHorse;
import com.example.dovchin.horseapplication1.methods.SearchMaleHorse;
import com.example.dovchin.horseapplication1.methods.SearchStallion;

/**
 * Created by amour on 2/10/2017.
 */
public class FemaleHorsefragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.female_horse_layout);
        Button btnAdd=(Button)findViewById(R.id.button3);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FemaleHorsefragment.this, AddFemaleHorseActivity.class);
                startActivity(intent);
            }
        });
        Button btnView=(Button)findViewById(R.id.button12);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FemaleHorsefragment.this, SearchFemaleHorse.class);
                startActivity(intent);
            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        Intent intent = new Intent(getApplicationContext(), SearchStallion.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_male:
                        Intent intent3=new Intent(getApplicationContext(), SearchMaleHorse.class);
                        startActivity(intent3);
                        return true;
                    case R.id.action_schedules:
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.action_colt:
                        Intent intent4=new Intent(getApplicationContext(), SearchColt.class);
                        startActivity(intent4);
                        return true;
                    case R.id.action_music:
                        Intent intent2 = new Intent(getApplicationContext(), SearchFemaleHorse.class);
                        startActivity(intent2);
                        return true;
                }
                return true;
            }
        });
    }


}

