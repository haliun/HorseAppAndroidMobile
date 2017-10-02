package com.example.dovchin.horseapplication1.methods;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import com.github.clans.fab.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dovchin.horseapplication1.Activities.MainActivity;
import com.example.dovchin.horseapplication1.Activities.OlympActivity;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.models.Olymp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddOlympActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    int day, year, month;
    private String mUserId;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_olymp);
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
        Button btnDatePicker = (Button) findViewById(R.id.buttonOLDdate);
        final EditText txtDate = (EditText) findViewById(R.id.editTextOlympDate);
        //get current date
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dd = new DatePickerDialog(AddOlympActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);

                                    txtDate.setText(formatter.format(date));


                                } catch (Exception ex) {

                                }
                            }
                        }, year, month, day);
                dd.show();
            }
        });
        //spinner of listing type
        Spinner types = (Spinner) findViewById(R.id.spinnerOlymp);
        ArrayAdapter Adapter = ArrayAdapter.createFromResource(this, R.array.type_olymps, android.R.layout.simple_spinner_item);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        types.setPrompt("Уралдааны төрлийг сонгоно уу!");
        types.setAdapter(Adapter);

        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonOsave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                final String horse_id=extras.getString("horse_id");
                saveOlymp((String) horse_id);

            }
        });
        Bundle extras = getIntent().getExtras();
       // final String horse_id=extras.getString("horse_id");

        if (getIntent().getExtras() != null) {
            extras = getIntent().getExtras();
            final Olymp olymp = (Olymp) extras.get("olymps");
            final String horse_id_key=extras.getString("horse_id");

            if (olymp != null) {
                    EditText nameEdtText = (EditText) findViewById(R.id.editTextOlympName);
                    Spinner type = (Spinner) findViewById(R.id.spinnerOlymp);
                    EditText txtEdtDate = (EditText) findViewById(R.id.editTextOlympDate);
                    EditText place = (EditText) findViewById(R.id.editTextOlympPlace);
                    EditText category = (EditText) findViewById(R.id.editTextOlympCat);
                    EditText horses = (EditText) findViewById(R.id.editTextOlympNumb);
                    EditText desc = (EditText) findViewById(R.id.editTextOlympDesc);
                    final String type_txt = olymp.getType();
                    Adapter = ArrayAdapter.createFromResource(this, R.array.type_olymps, android.R.layout.simple_spinner_item);
                    Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    type.setPrompt("Уралдааны төрлийг сонгоно уу!");
                    //Adapter.add(type_txt);
                    type.setAdapter(Adapter);
                    nameEdtText.setText(olymp.getOlymp_name());
                    //type.setText(.getDescription());
                    txtEdtDate.setText(olymp.getDate());
                    place.setText(olymp.getPlace());
                    category.setText(olymp.getCategory());
                    horses.setText(olymp.getNum_horses());
                    desc.setText(olymp.getDescription());
                    FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.floatingActionButtonOsave);
                    fab2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String key = olymp.getOlymp_id();
                            String key_horse=horse_id_key;
                            //Toast.makeText(getApplicationContext(), key_horse, Toast.LENGTH_SHORT).show();
                            updateOlymp((String) key, (String) key_horse);

                        }
                    });


                }
            }


    }
    public void saveOlymp(String horse_id){
        //save to Firebase
        EditText nameEdtText = (EditText)findViewById(R.id.editTextOlympName);
        Spinner type=(Spinner)findViewById(R.id.spinnerOlymp) ;
        EditText txtEdtDate=(EditText)findViewById(R.id.editTextOlympDate);
        EditText place = (EditText)findViewById(R.id.editTextOlympPlace);
        EditText category = (EditText)findViewById(R.id.editTextOlympCat);
        EditText horses=(EditText)findViewById(R.id.editTextOlympNumb);
        EditText desc=(EditText)findViewById(R.id.editTextOlympDesc) ;


        mDatabase=FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddOlympActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{

            mUserId=mFirebaseUser.getUid();
            Olymp olymp=new Olymp();

            String key=mDatabase.child("users").child(mUserId).child("Olymp").push().getKey();
            olymp.setOlymp_id(key);
            olymp.setOlymp_name(nameEdtText.getText().toString());
            olymp.setDescription(desc.getText().toString());
            olymp.setDate(txtEdtDate.getText().toString());
            olymp.setCategory(category.getText().toString());
            final String selected_type=type.getSelectedItem().toString();
            olymp.setPlace(place.getText().toString());
            olymp.setNum_horses(horses.getText().toString());
            olymp.setHorse_id(horse_id);
            olymp.setType(selected_type);
            Map<String, Object> childupdates=new HashMap<>();
            childupdates.put(key,olymp.OlymptoFirebaseObject());
            String name=nameEdtText.getText().toString();
            String date=txtEdtDate.getText().toString();

            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(date)){
                Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Явагдсан огноо' гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            }else{

                mDatabase.child("users").child(mUserId).child("Olymps").updateChildren(childupdates,new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            finish();

                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Амжилттай!", Toast.LENGTH_SHORT).show();
                Bundle bundle=getIntent().getExtras();
                Intent intent = new Intent(AddOlympActivity.this, OlympActivity.class);
                intent.putExtra("horse_id",horse_id);
                startActivity(intent);
                AddOlympActivity.this.finish();
            }




        }
    }
    public void updateOlymp(String key_id, String key_horse){
        //save to Firebase
        EditText nameEdtText = (EditText)findViewById(R.id.editTextOlympName);
        Spinner type=(Spinner)findViewById(R.id.spinnerOlymp) ;
        EditText txtEdtDate=(EditText)findViewById(R.id.editTextOlympDate);
        EditText place = (EditText)findViewById(R.id.editTextOlympPlace);
        EditText category = (EditText)findViewById(R.id.editTextOlympCat);
        EditText horses=(EditText)findViewById(R.id.editTextOlympNumb);
        EditText desc=(EditText)findViewById(R.id.editTextOlympDesc) ;


        mDatabase=FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddOlympActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{

            mUserId=mFirebaseUser.getUid();
            Olymp olymp=new Olymp();

            String key=mDatabase.child("users").child(mUserId).child("Olymp").push().getKey();
            olymp.setOlymp_id(key_id);
            olymp.setOlymp_name(nameEdtText.getText().toString());
            olymp.setDescription(desc.getText().toString());
            olymp.setDate(txtEdtDate.getText().toString());
            olymp.setCategory(category.getText().toString());
            final String selected_type=type.getSelectedItem().toString();
            olymp.setPlace(place.getText().toString());
            olymp.setNum_horses(horses.getText().toString());
            olymp.setType(selected_type);
            olymp.setHorse_id(key_horse);
            Map<String, Object> childupdates=new HashMap<>();
            childupdates.put(key,olymp.OlymptoFirebaseObject());
            String name_=nameEdtText.getText().toString();
            String date=txtEdtDate.getText().toString();

            if(TextUtils.isEmpty(name_) || TextUtils.isEmpty(date)){
                Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Явагдсан огноо'  гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            }else{
                Query applesQuery = mDatabase.child("users").child(mUserId).child("Olymps").orderByChild("olymp_id").equalTo(key_id);
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


                mDatabase.child("users").child(mUserId).child("Olymps").updateChildren(childupdates,new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            finish();

                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Амжилттай!", Toast.LENGTH_SHORT).show();
                Bundle bundle=getIntent().getExtras();
                final String horse_id=bundle.getString("horse_id");
                Intent intent = new Intent(AddOlympActivity.this, OlympActivity.class);
                intent.putExtra("horse_id",horse_id);
                startActivity(intent);
                AddOlympActivity.this.finish();
            }




        }
    }
}
