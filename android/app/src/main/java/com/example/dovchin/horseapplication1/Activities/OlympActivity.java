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
import android.widget.Toast;

import com.example.dovchin.horseapplication1.R;

import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.methods.AddOlympActivity;
import com.example.dovchin.horseapplication1.models.Olymp;
import com.example.dovchin.horseapplication1.models.Stallion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OlympActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    RecycleAdapter adapter;
    ArrayList<Olymp> olymps_list;
    private String horse_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olymp);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.floatingActionButton6);
        Bundle extras = getIntent().getExtras();
        horse_key = extras.getString("horse_id");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=getIntent().getExtras();
                final String horse_id=bundle.getString("horse_id");
               // Toast.makeText(getApplicationContext(), horse_id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OlympActivity.this, AddOlympActivity.class);
                Bundle bundle_extra=new Bundle();
                bundle_extra.putString("horse_id",horse_id);
                intent.putExtras(bundle_extra);
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
        olymps_list = new ArrayList<>();
        RecyclerView recyclerview=(RecyclerView)findViewById(R.id.reviewOlymp);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        recyclerview.setLayoutManager(llm);
        adapter=new RecycleAdapter();
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
    public String getHorseKey(){
        return horse_key;
    }
    @Override
    protected void onResume(){
        super.onResume();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(OlympActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            String horse_id=getHorseKey();
            Query applesQuery=mDatabase.child("users").child(mUserId).child("Olymps").orderByChild("horse_id").equalTo(horse_id);
            applesQuery.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            olymps_list.clear();
                            Log.w("Olymps", "getUser:onCancelled " + dataSnapshot.toString());
                            Log.w("Olymps", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Olymp olymp = data.getValue(Olymp.class);
                                olymps_list.add(olymp);

                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Olymps", "getUser:onCancelled", databaseError.toException());

                        }


                    }
            );


        }
    }
    public class RecycleAdapter extends RecyclerView.Adapter {
        private List<Olymp> olymps;
        protected Context context;

        @Override
        public int getItemCount() {
            return olymps_list.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Bundle bundle=getIntent().getExtras();
//            final String horse_id=bundle.getString("horse_id");
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_olymp, parent, false);
            RecycleAdapter.SimpleItemViewHolder pvh = new RecycleAdapter.SimpleItemViewHolder(v,olymps);
            return pvh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RecycleAdapter.SimpleItemViewHolder viewHolder = (RecycleAdapter.SimpleItemViewHolder) holder;
            viewHolder.position = position;
            Olymp olymp = olymps_list.get(position);
            ((SimpleItemViewHolder) holder).name.setText(olymp.getOlymp_name());
            ((SimpleItemViewHolder) holder).date.setText(olymp.getDate());


        }


        public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView name;
            private final String TAG = RecycleAdapter.SimpleItemViewHolder.class.getSimpleName();
            public TextView date;
            public int position;
            private List<Olymp> olymps;
            public SimpleItemViewHolder(View itemView, final List<Olymp> olymps) {
                super(itemView);
                itemView.setOnClickListener(this);
                Bundle bundle=getIntent().getExtras();
                //horse_id=bundle.getString("horse_id");
                name = (TextView) itemView.findViewById(R.id.textViewOlymp_name_row);
                date = (TextView) itemView.findViewById(R.id.textViewOlymp_date_row);


            }


            @Override
            public void onClick(View view) {
                Bundle bundle=getIntent().getExtras();
                final String horse_id=bundle.getString("horse_id");
                Intent newIntent = new Intent(OlympActivity.this,OlympDetailsActivity.class);
                newIntent.putExtra("horse_id",horse_id);
                newIntent.putExtra("olymps", olymps_list.get(position));
                //Toast.makeText(getApplicationContext(), horse_id, Toast.LENGTH_SHORT).show();
                OlympActivity.this.startActivity(newIntent);
                name = (TextView)findViewById(R.id.textViewOlymp_name_row);

            }
        }




    }
}
