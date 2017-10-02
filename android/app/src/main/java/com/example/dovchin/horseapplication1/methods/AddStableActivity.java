package com.example.dovchin.horseapplication1.methods;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dovchin.horseapplication1.Activities.FoodVitActivity;
import com.example.dovchin.horseapplication1.Activities.StableActivity;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.models.FoodVit;
import com.example.dovchin.horseapplication1.models.Stable;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddStableActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    int day, year, month;
    private String mUserId;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stable);

        Bundle extras = getIntent().getExtras();
        final String food_type=extras.getString("type_stable");
        String type_array[]={food_type};
        //spinner of listing type
        Spinner types = (Spinner) findViewById(R.id.spinner_stable);
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,type_array );
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        types.setPrompt("Төрлийг сонгоно уу!");
        types.setAdapter(Adapter);
        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton11);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.type_numbers, android.R.layout.simple_spinner_item);
        Spinner sun = (Spinner) findViewById(R.id.spinner_sun);
        // Set the layout to use for each dropdown item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sun.setAdapter(adapter);
        ArrayAdapter adapter_water = ArrayAdapter.createFromResource(this, R.array.type_numbers, android.R.layout.simple_spinner_item);
        Spinner water = (Spinner) findViewById(R.id.spinner_water);
        // Set the layout to use for each dropdown item
        adapter_water.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        water.setAdapter(adapter);
        ArrayAdapter adapter_grass = ArrayAdapter.createFromResource(this, R.array.type_numbers, android.R.layout.simple_spinner_item);
        Spinner grass = (Spinner) findViewById(R.id.spinner_grass);
        // Set the layout to use for each dropdown item
        adapter_grass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        grass.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFood();

            }
        });
        if (getIntent().getExtras() != null) {
            extras = getIntent().getExtras();
            final Stable foodVit = (Stable) extras.get("stable");

            if (foodVit != null) {
                Spinner type = (Spinner) findViewById(R.id.spinner_stable);
                EditText nameEdtText = (EditText) findViewById(R.id.editText_stName);
                EditText aimg = (EditText) findViewById(R.id.editText_stAimag);
                EditText season = (EditText) findViewById(R.id.editText_stSeason );
                EditText sum = (EditText) findViewById(R.id.editText_stSum);
                EditText num_horse=(EditText)findViewById(R.id.editText_StAduutoo) ;
                EditText num_ajilchin = (EditText) findViewById(R.id.editText_stMaclhdtoo);
                EditText malchin = (EditText) findViewById(R.id.editText_stMalchin);
                Spinner us = (Spinner) findViewById(R.id.spinner_water);
                EditText talbai = (EditText) findViewById(R.id.editText6);
                Spinner uvs=(Spinner)findViewById(R.id.spinner_grass) ;
                Spinner nar=(Spinner)findViewById(R.id.spinner_sun) ;

                nameEdtText.setText(foodVit.getName());
                final String type_txt = foodVit.getType();
                String food="Тэжээл";
                if(type_txt==food){
                    final String type_array1[]={type_txt,"Зүчээ"};
                    Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,type_array1);
                    Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    type.setPrompt("Төрлийг сонгоно уу!");
                    //Adapter.add(type_txt);
                    type.setAdapter(Adapter);
                }else{
                    final String type_array1[]={type_txt,"Бэлчээр"};
                    Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,type_array1);
                    Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    type.setPrompt("Төрлийг сонгоно уу!");
                    //Adapter.add(type_txt);
                    type.setAdapter(Adapter);
                }


                //type.setText(.getDescription());
                aimg.setText(foodVit.getCity());
                season.setText(foodVit.getSeason());
                malchin.setText(foodVit.getMain_person());
                num_horse.setText(foodVit.getNum_horse());
                num_ajilchin.setText(foodVit.getNum_person());
                talbai.setText(foodVit.getSquare());
                sum.setText(foodVit.getDistrict());
                String suntxt=foodVit.getSun();
                String watertxt= foodVit.getWater();
                String grassTxt=foodVit.getGrass();
                final List<String> grass_list = new ArrayList<String>();
                grass_list.add(grassTxt);
                final List<String> sun_list = new ArrayList<String>();
                sun_list.add(suntxt);

                final List<String> water_list = new ArrayList<String>();
                water_list.add(watertxt);
                ArrayAdapter<String> sunadapter = new ArrayAdapter<String>(AddStableActivity.this, android.R.layout.simple_spinner_item, sun_list);
                sunadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sun.setPrompt("Нарны хэмжээг сонгоно уу!");
                sun.setAdapter(sunadapter);
                ArrayAdapter<String> wateradapter = new ArrayAdapter<String>(AddStableActivity.this, android.R.layout.simple_spinner_item, water_list);
                wateradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                water.setPrompt("Усны хэмжээг сонгоно уу!");
                water.setAdapter(wateradapter);
                ArrayAdapter<String> grassadapter = new ArrayAdapter<String>(AddStableActivity.this, android.R.layout.simple_spinner_item, grass_list);
                grassadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                grass.setPrompt("Өвсний хэмжээг сонгоно уу!");
                grass.setAdapter(sunadapter);
                FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.floatingActionButton11);
                fab2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = foodVit.getId();
                        //Toast.makeText(getApplicationContext(), key_horse, Toast.LENGTH_SHORT).show();
                        updateFood((String) key);

                    }
                });


            }
        }


    }
    public void saveFood(){
        //save to Firebase
        Spinner type = (Spinner) findViewById(R.id.spinner_stable);
        EditText nameEdtText = (EditText) findViewById(R.id.editText_stName);
        EditText aimg = (EditText) findViewById(R.id.editText_stAimag);
        EditText season = (EditText) findViewById(R.id.editText_stSeason );
        EditText sum = (EditText) findViewById(R.id.editText_stSum);
        EditText num_horse=(EditText)findViewById(R.id.editText_StAduutoo) ;
        EditText num_ajilchin = (EditText) findViewById(R.id.editText_stMaclhdtoo);
        EditText malchin = (EditText) findViewById(R.id.editText_stMalchin);
        Spinner us = (Spinner) findViewById(R.id.spinner_water);
        EditText talbai = (EditText) findViewById(R.id.editText6);
        Spinner uvs=(Spinner)findViewById(R.id.spinner_grass) ;
        Spinner nar=(Spinner)findViewById(R.id.spinner_sun) ;

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddStableActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{

            mUserId=mFirebaseUser.getUid();
            Stable foodVit=new Stable();
            final String selected_type=type.getSelectedItem().toString();
            final String selected_water=us.getSelectedItem().toString();
            final String selected_uvs=uvs.getSelectedItem().toString();
            final String selected_nar=nar.getSelectedItem().toString();
            String key=mDatabase.child("users").child(mUserId).child("Stable").push().getKey();
            foodVit.setId(key);
            foodVit.setName(nameEdtText.getText().toString());
            foodVit.setDistrict(sum.getText().toString());
            foodVit.setCity(aimg.getText().toString());
            foodVit.setMain_person(malchin.getText().toString());
            foodVit.setNum_horse(num_horse.getText().toString());
            foodVit.setType(selected_type);
            foodVit.setSquare(talbai.getText().toString());
            foodVit.setNum_person(num_ajilchin.getText().toString());
            foodVit.setSeason(season.getText().toString());
            foodVit.setWater(selected_water);
            foodVit.setSun(selected_nar);
            foodVit.setGrass(selected_uvs);
            Map<String, Object> childupdates=new HashMap<>();
            childupdates.put(key,foodVit.StableToFirebase());
            String name=nameEdtText.getText().toString();
            String aimag=aimg.getText().toString();


            if(TextUtils.isEmpty(name)||TextUtils.isEmpty(aimag)){
                Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Аймаг' гэсэн талбарыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            }else{

                mDatabase.child("users").child(mUserId).child("Stable").updateChildren(childupdates,new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            finish();

                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Амжилттай нэмэгдлээ!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddStableActivity.this, StableActivity.class);
                startActivity(intent);
                AddStableActivity.this.finish();
            }




        }
    }
    public void updateFood(String key_id){
        //save to Firebase
        Spinner type = (Spinner) findViewById(R.id.spinner_stable);
        EditText nameEdtText = (EditText) findViewById(R.id.editText_stName);
        EditText aimg = (EditText) findViewById(R.id.editText_stAimag);
        EditText season = (EditText) findViewById(R.id.editText_stSeason );
        EditText sum = (EditText) findViewById(R.id.editText_stSum);
        EditText num_horse=(EditText)findViewById(R.id.editText_StAduutoo) ;
        EditText num_ajilchin = (EditText) findViewById(R.id.editText_stMaclhdtoo);
        EditText malchin = (EditText) findViewById(R.id.editText_stMalchin);
        Spinner us = (Spinner) findViewById(R.id.spinner_water);
        EditText talbai = (EditText) findViewById(R.id.editText6);
        Spinner uvs=(Spinner)findViewById(R.id.spinner_grass) ;
        Spinner nar=(Spinner)findViewById(R.id.spinner_sun) ;

        mDatabase=FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddStableActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{

            mUserId=mFirebaseUser.getUid();
            Stable foodVit=new Stable();
            final String selected_type=type.getSelectedItem().toString();
            final String selected_water=us.getSelectedItem().toString();
            final String selected_uvs=uvs.getSelectedItem().toString();
            final String selected_nar=nar.getSelectedItem().toString();
            String key=mDatabase.child("users").child(mUserId).child("Stable").push().getKey();
            foodVit.setId(key);
            foodVit.setName(nameEdtText.getText().toString());
            foodVit.setDistrict(sum.getText().toString());
            foodVit.setCity(aimg.getText().toString());
            foodVit.setMain_person(malchin.getText().toString());
            foodVit.setNum_horse(num_horse.getText().toString());
            foodVit.setType(selected_type);
            foodVit.setSquare(talbai.getText().toString());
            foodVit.setNum_person(num_ajilchin.getText().toString());
            foodVit.setSeason(season.getText().toString());
            foodVit.setWater(selected_water);
            foodVit.setSun(selected_nar);
            foodVit.setGrass(selected_uvs);
            Map<String, Object> childupdates=new HashMap<>();
            childupdates.put(key,foodVit.StableToFirebase());
            String name=nameEdtText.getText().toString();
            String aimag=aimg.getText().toString();

            if(TextUtils.isEmpty(name)){
                Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Аймаг' гэсэн талбарыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            }else{
                Query applesQuery = mDatabase.child("users").child(mUserId).child("Stable").orderByChild("id").equalTo(key_id);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                mDatabase.child("users").child(mUserId).child("Stable").updateChildren(childupdates,new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            finish();

                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddStableActivity.this, StableActivity.class);
                startActivity(intent);
                AddStableActivity.this.finish();
            }




        }
    }
}

