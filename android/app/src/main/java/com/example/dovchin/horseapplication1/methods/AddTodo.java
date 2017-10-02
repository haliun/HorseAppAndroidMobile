package com.example.dovchin.horseapplication1.methods;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.dovchin.horseapplication1.Activities.MainActivity;

import com.example.dovchin.horseapplication1.Activities.TodoActivity;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.models.ToDoList;
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

public class AddTodo extends AppCompatActivity {
    Button savetodo;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    TextView stalCount, maleCount, femaleCount, coltCount;
    int day, year, month;
    private String mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo_activity);
        Button btnDatePicker=(Button)findViewById(R.id.btn_date);
        final EditText txtDate=(EditText)findViewById(R.id.in_date);
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
        //get current date
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dd = new DatePickerDialog(AddTodo.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy",java.util.Locale.getDefault());
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
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final Button buttonStallion=(Button)findViewById(R.id.buttonStallion);



        //
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTODO();

            }
        });
        FloatingActionButton fab_del=(FloatingActionButton)findViewById(R.id.floatingActionButton2);
        fab_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTODO();

            }
        });
        //get detials from item list
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final ToDoList todo = (ToDoList) extras.get("todo");
            if (todo != null) {
                EditText nameEdtText = (EditText)findViewById(R.id.todo_name);
                EditText messageEditText = (EditText)findViewById(R.id.todo_desc);
                EditText txtEdtDate=(EditText)findViewById(R.id.in_date);

                nameEdtText.setText(todo.getName());
                messageEditText.setText(todo.getDescription());
                txtEdtDate.setText(todo.getDate());
                FloatingActionButton fab2=(FloatingActionButton)findViewById(R.id.floatingActionButton);
                fab2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name=todo.getName();
                        updateTODO((String) name);

                    }
                });


            }
        }

    }
    public void updateTODO(String name) {
        EditText nameEditText = (EditText) findViewById(R.id.todo_name);
        EditText descEdiText = (EditText) findViewById(R.id.todo_desc);
        EditText txtDate = (EditText) findViewById(R.id.in_date);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(AddTodo.this, LoginActivity.class);
            startActivity(intent);
        } else {
            mUserId = mFirebaseUser.getUid();
            ToDoList todo = new ToDoList();
            todo.setName(nameEditText.getText().toString());
            todo.setDescription(descEdiText.getText().toString());
            todo.setDate(txtDate.getText().toString());
            String key = mDatabase.child("users").child(mUserId).child("todoList").push().getKey();
            Map<String, Object> childupdates = new HashMap<>();
            childupdates.put(key, todo.toFirebaseObject());
            String name_todo = nameEditText.getText().toString();

            if (TextUtils.isEmpty(name_todo)) {
                Toast.makeText(getApplicationContext(), "'Нэр' гэсэн талбарыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            } else {
                Query applesQuery = mDatabase.child("users").child(mUserId).child("todoList").orderByChild("name").equalTo(name);
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

                mDatabase.child("users").child(mUserId).child("todoList").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            finish();

                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddTodo.this, TodoActivity.class);
                startActivity(intent);
                AddTodo.this.finish();
            }


        }
    }
    public void saveTODO(){
        //save to Firebase
        EditText nameEditText=(EditText)findViewById(R.id.todo_name);
        EditText descEdiText=(EditText)findViewById(R.id.todo_desc);
        EditText txtDate=(EditText)findViewById(R.id.in_date);



        mDatabase=FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddTodo.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            ToDoList todo=new ToDoList();
            todo.setName(nameEditText.getText().toString());
            todo.setDescription(descEdiText.getText().toString());
            todo.setDate(txtDate.getText().toString());
            String key=mDatabase.child("users").child(mUserId).child("todoList").push().getKey();
            Map<String, Object> childupdates=new HashMap<>();
            childupdates.put(key,todo.toFirebaseObject());
            String name_todo=nameEditText.getText().toString();

            if(TextUtils.isEmpty(name_todo)){
                Toast.makeText(getApplicationContext(), "'Нэр' гэсэн талбарыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            }else{

                mDatabase.child("users").child(mUserId).child("todoList").updateChildren(childupdates,new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            finish();

                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Амжилттай нэмэгдлээ!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddTodo.this, TodoActivity.class);
                startActivity(intent);
                AddTodo.this.finish();
            }




        }
    }
    public void deleteTODO(){
        EditText nameEditText=(EditText)findViewById(R.id.todo_name);
        mDatabase=FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddTodo.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            final String name_todo=nameEditText.getText().toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(AddTodo.this);
            builder.setMessage("Та устгахдаа итгэлтэй байна уу?").setCancelable(false)
                    .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("todoList").orderByChild("name").equalTo(name_todo);
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(getApplicationContext(), "Амжилттай устгагдлаа!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddTodo.this, TodoActivity.class);
                                    startActivity(intent);
                                    AddTodo.this.finish();

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    })
                    .setNegativeButton("Үгүй", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.setTitle("Батлах");
            dialog.show();

            }




        }

}
