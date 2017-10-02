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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.example.dovchin.horseapplication1.Activities.MainActivity;
import com.example.dovchin.horseapplication1.Activities.PrizeActivity;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;

import com.example.dovchin.horseapplication1.models.Prize;
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

public class AddPrizeActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    int day, year, month;
    private String mUserId;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prize);
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
        Button btnDatePicker = (Button) findViewById(R.id.buttondatePrize);
        final EditText txtDate = (EditText) findViewById(R.id.editTextPEdate);
        //get current date
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dd = new DatePickerDialog(AddPrizeActivity.this,
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

        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonSavePrize);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                final String horse_id=extras.getString("horse_id");
                savePrize((String) horse_id);

            }
        });
        Bundle extras = getIntent().getExtras();
        // final String horse_id=extras.getString("horse_id");

        if (getIntent().getExtras() != null) {
            extras = getIntent().getExtras();
            final Prize prize = (Prize) extras.get("prizes");
            final String horse_id_key=extras.getString("horse_id");

            if (prize != null) {
                EditText nameEdtText = (EditText) findViewById(R.id.editTextPEName);
                EditText txtEdtDate = (EditText) findViewById(R.id.editTextPEdate);
                EditText olymp = (EditText) findViewById(R.id.editTextPEOlymp);
                EditText desc = (EditText) findViewById(R.id.editTextPEDesc);
                nameEdtText.setText(prize.getName_prize());
                //type.setText(.getDescription());
                txtEdtDate.setText(prize.getDatePrize());
                olymp.setText(prize.getOlymp_name());
                desc.setText(prize.getDescription());
                FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.floatingActionButtonSavePrize);
                fab2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = prize.getPrize_id();
                        String key_horse=horse_id_key;
                        //Toast.makeText(getApplicationContext(), key_horse, Toast.LENGTH_SHORT).show();
                        updatePrize((String) key, (String) key_horse);

                    }
                });


            }
        }


    }
    public void savePrize(String horse_id){
        //save to Firebase
        EditText nameEdtText = (EditText) findViewById(R.id.editTextPEName);
        EditText txtEdtDate = (EditText) findViewById(R.id.editTextPEdate);
        EditText olymp = (EditText) findViewById(R.id.editTextPEOlymp);
        EditText desc = (EditText) findViewById(R.id.editTextPEDesc);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddPrizeActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{

            mUserId=mFirebaseUser.getUid();
            Prize prize=new Prize();

            String key=mDatabase.child("users").child(mUserId).child("Prizes").push().getKey();
            prize.setPrize_id(key);
            prize.setName_prize(nameEdtText.getText().toString());
            prize.setDescription(desc.getText().toString());
            prize.setDate_prize(txtEdtDate.getText().toString());
            prize.setOlymp_name(olymp.getText().toString());
            prize.setHorse_id(horse_id);
            Map<String, Object> childupdates=new HashMap<>();
            childupdates.put(key,prize.PrizetoFirebaseObject());
            String name=nameEdtText.getText().toString();
            String olymp_name=olymp.getText().toString();

            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(olymp_name)){
                Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Уралдаан'  гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            }else{

                mDatabase.child("users").child(mUserId).child("Prizes").updateChildren(childupdates,new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            finish();

                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Амжилттай!", Toast.LENGTH_SHORT).show();
                Bundle bundle=getIntent().getExtras();
                Intent intent = new Intent(AddPrizeActivity.this, PrizeActivity.class);
                intent.putExtra("horse_id",horse_id);
                startActivity(intent);
                AddPrizeActivity.this.finish();
            }




        }
    }
    public void updatePrize(String key_id, String key_horse){
        //save to Firebase
        EditText nameEdtText = (EditText) findViewById(R.id.editTextPEName);
        EditText txtEdtDate = (EditText) findViewById(R.id.editTextPEdate);
        EditText olymp = (EditText) findViewById(R.id.editTextPEOlymp);
        EditText desc = (EditText) findViewById(R.id.editTextPEDesc);

        mDatabase=FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddPrizeActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{

            mUserId=mFirebaseUser.getUid();
            Prize prize=new Prize();

            String key=mDatabase.child("users").child(mUserId).child("Prizes").push().getKey();
            prize.setPrize_id(key_id);
            prize.setName_prize(nameEdtText.getText().toString());
            prize.setDescription(desc.getText().toString());
            prize.setDate_prize(txtEdtDate.getText().toString());
            prize.setOlymp_name(olymp.getText().toString());
            prize.setHorse_id(key_horse);
            Map<String, Object> childupdates=new HashMap<>();
            childupdates.put(key,prize.PrizetoFirebaseObject());
            String name=nameEdtText.getText().toString();
            String olymp_name=olymp.getText().toString();

            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(olymp_name)){
                Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Уралдаан'  гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            }else{
                Query applesQuery = mDatabase.child("users").child(mUserId).child("Prizes").orderByChild("prize_id").equalTo(key_id);
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


                mDatabase.child("users").child(mUserId).child("Prizes").updateChildren(childupdates,new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            finish();

                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Анжилттай!", Toast.LENGTH_SHORT).show();
                Bundle bundle=getIntent().getExtras();
                final String horse_id=bundle.getString("horse_id");
                Intent intent = new Intent(AddPrizeActivity.this, PrizeActivity.class);
                intent.putExtra("horse_id",horse_id);
                startActivity(intent);
                AddPrizeActivity.this.finish();
            }




        }
    }
}

