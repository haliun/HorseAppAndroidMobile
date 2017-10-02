package com.example.dovchin.horseapplication1.Activities;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import com.example.dovchin.horseapplication1.methods.SearchColt;
import com.example.dovchin.horseapplication1.methods.SearchFemaleHorse;
import com.example.dovchin.horseapplication1.methods.SearchMaleHorse;
import com.example.dovchin.horseapplication1.methods.SearchStallion;
import com.github.clans.fab.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.methods.AddTodo;
import com.example.dovchin.horseapplication1.models.ToDoList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TodoActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    RecycleAdapter adapter;
    ArrayList<ToDoList> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

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
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.floatingActionButton5);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoActivity.this, AddTodo.class);
                startActivity(intent);
            }
        });

        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance().getReference();
        todoList = new ArrayList<>();
        RecyclerView recyclerview=(RecyclerView)findViewById(R.id.todo_recycview);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        recyclerview.setLayoutManager(llm);
        adapter=new RecycleAdapter();
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
    @Override
    protected void onResume(){
        super.onResume();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(TodoActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            mDatabase.child("users").child(mUserId).child("todoList").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        todoList.clear();
                        Log.w("TodoApp", "getUser:onCancelled " + dataSnapshot.toString());
                        Log.w("TodoApp", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            ToDoList todo = data.getValue(ToDoList.class);
                            todoList.add(todo);
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());

                    }


                }
        );


        }
    }


    private class RecycleAdapter extends RecyclerView.Adapter {
        private List<ToDoList> todo;
        protected Context context;

        @Override
        public int getItemCount() {
            return todoList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_row, parent, false);
            SimpleItemViewHolder pvh = new SimpleItemViewHolder(v,todo);
            return pvh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SimpleItemViewHolder viewHolder = (SimpleItemViewHolder) holder;
            viewHolder.position = position;
            ToDoList todo = todoList.get(position);
            ((SimpleItemViewHolder) holder).name.setText(todo.getName());
            ((SimpleItemViewHolder) holder).date.setText(todo.getDate());
        }

        public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView name;
            private final String TAG = SimpleItemViewHolder.class.getSimpleName();
            public TextView date;
            public ImageView deleteIcon;
            public int position;
            private List<ToDoList> todo_list;
            public SimpleItemViewHolder(View itemView, final List<ToDoList> todo_list) {
                super(itemView);
                itemView.setOnClickListener(this);
                name = (TextView) itemView.findViewById(R.id.name_todolist);
                date = (TextView) itemView.findViewById(R.id.date_todolist);
            }


            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(TodoActivity.this,AddTodo.class);
                newIntent.putExtra("todo", todoList.get(position));
                TodoActivity.this.startActivity(newIntent);
                name = (TextView)findViewById(R.id.name_todolist);

        }
        }




}}



