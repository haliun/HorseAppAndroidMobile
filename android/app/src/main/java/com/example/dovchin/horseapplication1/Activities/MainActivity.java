package com.example.dovchin.horseapplication1.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.dovchin.horseapplication1.Launcher.PrefManager;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.fragments.Colt_fragment;
import com.example.dovchin.horseapplication1.fragments.FemaleHorsefragment;
import com.example.dovchin.horseapplication1.fragments.MaleHorseFragment;
import com.example.dovchin.horseapplication1.fragments.StallionFragment;
import com.example.dovchin.horseapplication1.methods.AddColtActivity;
import com.example.dovchin.horseapplication1.methods.AddFemaleHorseActivity;
import com.example.dovchin.horseapplication1.methods.AddMaleHorseActivity;
import com.example.dovchin.horseapplication1.methods.AddStallionActivity;
import com.example.dovchin.horseapplication1.methods.SearchColt;
import com.example.dovchin.horseapplication1.methods.SearchFemaleHorse;
import com.example.dovchin.horseapplication1.methods.SearchMaleHorse;
import com.example.dovchin.horseapplication1.methods.SearchStallion;
import com.example.dovchin.horseapplication1.models.FemaleHorse;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3,floatingActionButton4;

    TextView stalCount, maleCount, femaleCount, coltCount;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final Button buttonStallion=(Button)findViewById(R.id.buttonStallion);
        buttonStallion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchStallion.class);
                startActivity(intent);
            }
        });
        Button btncolt=(Button)findViewById(R.id.buttonColt);
        btncolt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchColt.class);
                startActivity(intent);
            }
        });
        Button btnfemale=(Button)findViewById(R.id.buttonFemale);
        btnfemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchFemaleHorse.class);
                startActivity(intent);
            }
        });
        Button btnmale=(Button)findViewById(R.id.buttonMale);
        btnmale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchMaleHorse.class);
                startActivity(intent);
            }
        });
        if(mFirebaseUser==null){
            PrefManager prefManager = new PrefManager(getApplicationContext());

            // make first time launch TRUE
            prefManager.setFirstTimeLaunch(true);

            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
        }else {
            mUserId=mFirebaseUser.getUid();
            stalCount=(TextView)findViewById(R.id.textViewCountStal);
            maleCount=(TextView)findViewById(R.id.textView156);
            femaleCount=(TextView)findViewById(R.id.textView158);
            coltCount=(TextView)findViewById(R.id.textViewCountColt);
            Query query = mDatabase.child("users").child(mUserId).child("Stallions").orderByChild("activity").equalTo(true);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        Long countStal= dataSnapshot.getChildrenCount();
                        stalCount.setText(String.valueOf(countStal));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Query query1 = mDatabase.child("users").child(mUserId).child("Colts").orderByChild("activity").equalTo(true);
            query1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Long countColt= dataSnapshot.getChildrenCount();
                    coltCount.setText(String.valueOf(countColt));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Query query2 = mDatabase.child("users").child(mUserId).child("FemaleHorses").orderByChild("activity").equalTo(true);
            query2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Long countColt= dataSnapshot.getChildrenCount();
                    femaleCount.setText(String.valueOf(countColt));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Query query3 = mDatabase.child("users").child(mUserId).child("MaleHorses").orderByChild("activity").equalTo(true);
            query3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Long countColt= dataSnapshot.getChildrenCount();
                    maleCount.setText(String.valueOf(countColt));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);
        floatingActionButton4 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item4);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddStallionActivity.class);
                startActivity(intent);

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMaleHorseActivity.class);
                startActivity(intent);

            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddFemaleHorseActivity.class);
                startActivity(intent);

            }
        });
        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddColtActivity.class);
                startActivity(intent);

            }
        });


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id==R.id.action_add){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }else if(id==R.id.action_haih){
            startActivity(new Intent(MainActivity.this,UserProfileActivity.class));
        }

        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_azarga_layout) {
            Intent intent = new Intent(MainActivity.this, StallionFragment.class);
            startActivity(intent);
        } else if (id == R.id.nav_morid_layout) {
            Intent intent = new Intent(MainActivity.this, MaleHorseFragment.class);
            startActivity(intent);

        } else if (id == R.id.nav_guu_layout) {
            Intent intent = new Intent(MainActivity.this, FemaleHorsefragment.class);
            startActivity(intent);

        } else if (id == R.id.nav_unaga_layout) {
            Intent intent = new Intent(MainActivity.this, Colt_fragment.class);
            startActivity(intent);

        }else if(id==R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);

        }else if(id==R.id.nav_ajil_layout){
            Intent intent = new Intent(MainActivity.this, TodoActivity.class);
            startActivity(intent);
        }else if(id==R.id.nav_tejeel_layout){
            Intent intent = new Intent(MainActivity.this, FoodVitActivity.class);
            startActivity(intent);
        }else if(id==R.id.nav_belcheer_layout){
            Intent intent = new Intent(MainActivity.this, StableActivity.class);
            startActivity(intent);
        }else if(id==R.id.nav_orlogo_zarlaga){
            Intent intent = new Intent(MainActivity.this, MoneyActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }


}
