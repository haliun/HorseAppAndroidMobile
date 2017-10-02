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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.dovchin.horseapplication1.Adapters.DataAdapterStallions;
import com.example.dovchin.horseapplication1.Adapters.ViewPagerAdapter;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.fragments.ColtAdapterFragment;
import com.example.dovchin.horseapplication1.fragments.FemaleHorseFragmentAdapter;
import com.example.dovchin.horseapplication1.fragments.FragmentFood;
import com.example.dovchin.horseapplication1.fragments.FragmentVit;
import com.example.dovchin.horseapplication1.fragments.MaleHorseAdapterFragment;
import com.example.dovchin.horseapplication1.methods.AddColtActivity;
import com.example.dovchin.horseapplication1.methods.AddFemaleHorseActivity;
import com.example.dovchin.horseapplication1.methods.AddMaleHorseActivity;
import com.example.dovchin.horseapplication1.methods.AddStallionActivity;
import com.example.dovchin.horseapplication1.methods.SearchColt;
import com.example.dovchin.horseapplication1.methods.SearchFemaleHorse;
import com.example.dovchin.horseapplication1.methods.SearchMaleHorse;
import com.example.dovchin.horseapplication1.methods.SearchStallion;
import com.example.dovchin.horseapplication1.models.Colt;
import com.example.dovchin.horseapplication1.models.FemaleHorse;
import com.example.dovchin.horseapplication1.models.MaleHorse;
import com.example.dovchin.horseapplication1.models.Olymp;
import com.example.dovchin.horseapplication1.models.Stallion;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StallionGroupActivity extends AppCompatActivity {
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3,floatingActionButton4;

    private RecyclerView mRecyclerView;
    private ArrayList<Stallion> mArrayListStallion;
    private ArrayList<FemaleHorse> mArrayListFemale;
    private ArrayList<MaleHorse> mArrayListMale;
    private ArrayList<Colt> mArrayListColt;
    private DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DataAdapterStallions mAdapter;
    private String horse_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stallion_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        Bundle extras = getIntent().getExtras();
        horse_name = extras.getString("horse_name");


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.floatingMenuGroup);
        //floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.fabGroupMale);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.fabGroupFemale);
        floatingActionButton4 = (FloatingActionButton) findViewById(R.id.fabGroupColt);
        Button btn_back=(Button)findViewById(R.id.button_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), SearchStallion.class);
                startActivity(intent2);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(StallionGroupActivity.this, AddMaleHorseActivity.class);
                startActivity(intent);

            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(StallionGroupActivity.this, AddFemaleHorseActivity.class);
                startActivity(intent);

            }
        });
        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(StallionGroupActivity.this, AddColtActivity.class);
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
    public String getHorse_name(){
        return horse_name;
    }
    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent(getApplicationContext(), SearchStallion.class);
        startActivity(intent2);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MaleHorseAdapterFragment(), "Морьд");
        adapter.addFragment(new FemaleHorseFragmentAdapter(), "Гүүнүүд");
        adapter.addFragment(new ColtAdapterFragment(), "Унаганууд");
        viewPager.setAdapter(adapter);

    }

}
