package com.example.dovchin.horseapplication1.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.dovchin.horseapplication1.Adapters.ViewPagerAdapter;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.fragments.FragmentFood;
import com.example.dovchin.horseapplication1.fragments.FragmentVit;
import com.example.dovchin.horseapplication1.methods.AddFoodVitActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class FoodVitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_vit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button button_back=(Button)findViewById(R.id.button5);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoodVitActivity.this,MainActivity.class));
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoodVitActivity.this,MainActivity.class));
            }
        });



        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        FloatingActionMenu materialDesignFAM = (FloatingActionMenu) findViewById(R.id.fab_add_food);
        FloatingActionButton floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item_food);
        FloatingActionButton floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item_vit);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String food="Тэжээл";
                Bundle bundle=new Bundle();
                bundle.putString("type_food",food);
                Intent intent = new Intent(FoodVitActivity.this, AddFoodVitActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String food="Витамин";
                Bundle bundle=new Bundle();
                bundle.putString("type_food",food);
                Intent intent = new Intent(FoodVitActivity.this, AddFoodVitActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentFood(), "Тэжээл");
        adapter.addFragment(new FragmentVit(), "Витамин");
        viewPager.setAdapter(adapter);

    }




}



