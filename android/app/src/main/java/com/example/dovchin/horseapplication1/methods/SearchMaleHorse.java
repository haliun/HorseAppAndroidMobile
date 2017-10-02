package com.example.dovchin.horseapplication1.methods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dovchin.horseapplication1.Activities.MainActivity;
import com.example.dovchin.horseapplication1.Adapters.DataAdapterMAleHorse;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.models.MaleHorse;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Khaliun on 6/18/2017.
 */

public class SearchMaleHorse extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<MaleHorse> mArrayList;
    private DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DataAdapterMAleHorse mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_stallion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.floatingActionButton8);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchMaleHorse.this, AddMaleHorseActivity.class);
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
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mArrayList = new ArrayList<>();
        RecyclerView recyclerview=(RecyclerView)findViewById(R.id.recViewMale);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(llm);
        mAdapter=new DataAdapterMAleHorse(mArrayList);
        recyclerview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


    }
    @Override
    protected void onResume(){
        super.onResume();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(SearchMaleHorse.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            mDatabase.child("users").child(mUserId).child("MaleHorses").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mArrayList.clear();
                            Log.w("StallionList", "getUser:onCancelled " + dataSnapshot.toString());
                            Log.w("StallionList", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                MaleHorse stallions = data.getValue(MaleHorse.class);
                                mArrayList.add(stallions);
                            }
                            mAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("StallionList", "getUser:onCancelled", databaseError.toException());

                        }


                    }
            );


        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }



}
