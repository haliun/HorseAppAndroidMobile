package com.example.dovchin.horseapplication1;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final int REQUEST_CODE_ZURAG=123;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //dialog=new Dialog(MainActivity.this);
        //dialog.setContentView(R.layout.welcome);
       // isFirstTime();
        SharedPreferences prefs = getSharedPreferences("KEY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        boolean isFirst = prefs.getBoolean("KEY_IS_FIRST_TIME", true);
        if(isFirst) {
            Intent intent = new Intent(this, Welcome_user.class);
            editor.putBoolean("KEY_IS_FIRST_TIME", false);
            editor.commit();
            startActivity(intent);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        int hasPermission=
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(hasPermission!= PackageManager.PERMISSION_GRANTED)
        {
            if( !ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ZURAG);
                return;
            }
        }
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_ZURAG);

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
        if (id == R.id.action_settings) {
            return true;
        }else if(id==R.id.action_add){
            getFragmentManager().beginTransaction().replace(R.id.content_frame
                    ,new Add_fragment()).commit();

        }else if(id==R.id.action_haih){

        }

        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_azarga_layout) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame
                    ,new StallionFragment()).commit();
        } else if (id == R.id.nav_morid_layout) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame
                    ,new MaleHorseFragment()).commit();

        } else if (id == R.id.nav_guu_layout) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame
                    ,new FemaleHorsefragment()).commit();

        } else if (id == R.id.nav_unaga_layout) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame
                    ,new Colb_fragment()).commit();
        }else if(id==R.id.nav_profile) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame
                    ,new Colb_fragment()).commit();
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
    public void addAzarga(View view)
    {
        startActivity(new Intent(this, AddStallion.class));
    }
    public void addMori(View view)
    {
        startActivity(new Intent(this, AddMaleHorse.class));
    }
    public void viewAllAzarga(View view)
    {
        startActivity(new Intent(this,ViewActStallion.class));
    }
    public void viewAzargaSureg(View view){startActivity(new Intent(this,ViewStallionGroup.class));}
   public void viewAllMori(View view)
    {
        startActivity(new Intent(this,ViewActMaleHorse.class));
    }
    public void ToHome(View view){startActivity(new Intent(this,ViewActStallion.class));}



}
