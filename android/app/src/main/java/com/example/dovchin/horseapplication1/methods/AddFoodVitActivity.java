package com.example.dovchin.horseapplication1.methods;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

import com.example.dovchin.horseapplication1.Activities.FoodVitActivity;
import com.example.dovchin.horseapplication1.Activities.MainActivity;
import com.example.dovchin.horseapplication1.Activities.PrizeActivity;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.models.FoodVit;
import com.example.dovchin.horseapplication1.models.Prize;
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

public class AddFoodVitActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    int day, year, month;
    private String mUserId;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_vit);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        Intent intent = new Intent(getApplicationContext(), SearchStallion.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_schedules:
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.action_music:
                        Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent2);
                        return true;
                }
                return true;
            }
        });
        Button btnDatePicker = (Button) findViewById(R.id.button_date_food);
        final EditText txtDate = (EditText) findViewById(R.id.editTextFoodDate);
        //get current date
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dd = new DatePickerDialog(AddFoodVitActivity.this,
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
        Bundle extras = getIntent().getExtras();
        final String food_type=extras.getString("type_food");
        String type_array[]={food_type};
        //spinner of listing type
        Spinner types = (Spinner) findViewById(R.id.spinner_food);
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,type_array );
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        types.setPrompt("Төрлийг сонгоно уу!");
        types.setAdapter(Adapter);
        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_save_food);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFood();

            }
        });
        if (getIntent().getExtras() != null) {
            extras = getIntent().getExtras();
            final FoodVit foodVit = (FoodVit) extras.get("foods");

            if (foodVit != null) {
                Spinner type = (Spinner) findViewById(R.id.spinner_food);
                EditText nameEdtText = (EditText) findViewById(R.id.editTextFoodName);
                EditText txtEdtDate = (EditText) findViewById(R.id.editTextFoodDate);
                EditText quan = (EditText) findViewById(R.id.editTextFoddQuan);
                EditText sum = (EditText) findViewById(R.id.editTextFoodSum);
                EditText desc=(EditText)findViewById(R.id.editTextFoodDEsc) ;
                nameEdtText.setText(foodVit.getName());
                final String type_txt = foodVit.getType();
                String food="Тэжээл";
                if(type_txt==food){
                    final String type_array1[]={type_txt,"Витамин"};
                    Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,type_array1);
                    Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    type.setPrompt("Төрлийг сонгоно уу!");
                    //Adapter.add(type_txt);
                    type.setAdapter(Adapter);
                }else{
                    final String type_array1[]={type_txt,"Тэжээл"};
                    Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,type_array1);
                    Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    type.setPrompt("Төрлийг сонгоно уу!");
                    //Adapter.add(type_txt);
                    type.setAdapter(Adapter);
                }


                //type.setText(.getDescription());
                txtEdtDate.setText(foodVit.getDate());
                quan.setText(foodVit.getQuantity());
                desc.setText(foodVit.getDescription());
                sum.setText(foodVit.getSum());
                FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab_save_food);
                fab2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = foodVit.getFood_id();
                        //Toast.makeText(getApplicationContext(), key_horse, Toast.LENGTH_SHORT).show();
                        updateFood((String) key);

                    }
                });


            }
        }


    }
    public void saveFood(){
        //save to Firebase
        Spinner type = (Spinner) findViewById(R.id.spinner_food);
        EditText nameEdtText = (EditText) findViewById(R.id.editTextFoodName);
        EditText txtEdtDate = (EditText) findViewById(R.id.editTextFoodDate);
        EditText quan = (EditText) findViewById(R.id.editTextFoddQuan);
        EditText sum = (EditText) findViewById(R.id.editTextFoodSum);
        EditText desc=(EditText)findViewById(R.id.editTextFoodDEsc) ;
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddFoodVitActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{

            mUserId=mFirebaseUser.getUid();
            FoodVit foodVit=new FoodVit();
            final String selected_type=type.getSelectedItem().toString();
            String key=mDatabase.child("users").child(mUserId).child("FoodVits").push().getKey();
            foodVit.setFood_id(key);
            foodVit.setName(nameEdtText.getText().toString());
            foodVit.setDescription(desc.getText().toString());
            foodVit.setDate(txtEdtDate.getText().toString());
            foodVit.setQuantity(quan.getText().toString());
            foodVit.setSum(sum.getText().toString());
            foodVit.setType(selected_type);
            Map<String, Object> childupdates=new HashMap<>();
            childupdates.put(key,foodVit.FoodtoHashMap());
            String name=nameEdtText.getText().toString();


            if(TextUtils.isEmpty(name)){
                Toast.makeText(getApplicationContext(), "'Нэр' гэсэн талбарыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            }else{

                mDatabase.child("users").child(mUserId).child("FoodVits").updateChildren(childupdates,new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            finish();

                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Амжилттай нэмэгдлээ!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddFoodVitActivity.this, FoodVitActivity.class);
                startActivity(intent);
                AddFoodVitActivity.this.finish();
            }




        }
    }
    public void updateFood(String key_id){
        //save to Firebase
        Spinner type = (Spinner) findViewById(R.id.spinner_food);
        EditText nameEdtText = (EditText) findViewById(R.id.editTextFoodName);
        EditText txtEdtDate = (EditText) findViewById(R.id.editTextFoodDate);
        EditText quan = (EditText) findViewById(R.id.editTextFoddQuan);
        EditText sum = (EditText) findViewById(R.id.editTextFoodSum);
        EditText desc=(EditText)findViewById(R.id.editTextFoodDEsc) ;

        mDatabase=FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddFoodVitActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{

            mUserId=mFirebaseUser.getUid();
            FoodVit foodVit=new FoodVit();

            String key=mDatabase.child("users").child(mUserId).child("FoodVits").push().getKey();
            foodVit.setFood_id(key_id);
            foodVit.setName(nameEdtText.getText().toString());
            foodVit.setDescription(desc.getText().toString());
            foodVit.setDate(txtEdtDate.getText().toString());
            foodVit.setQuantity(quan.getText().toString());
            foodVit.setSum(sum.getText().toString());
            Map<String, Object> childupdates=new HashMap<>();
            childupdates.put(key,foodVit.FoodtoHashMap());
            String name=nameEdtText.getText().toString();


            if(TextUtils.isEmpty(name)){
                Toast.makeText(getApplicationContext(), "'Нэр' гэсэн талбарыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            }else{
                Query applesQuery = mDatabase.child("users").child(mUserId).child("FoodVits").orderByChild("food_id").equalTo(key_id);
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


                mDatabase.child("users").child(mUserId).child("FoodVits").updateChildren(childupdates,new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            finish();

                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddFoodVitActivity.this, FoodVitActivity.class);
                startActivity(intent);
                AddFoodVitActivity.this.finish();
            }




        }
    }
}
