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
import android.widget.TextView;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;

import com.example.dovchin.horseapplication1.methods.AddPrizeActivity;

import com.example.dovchin.horseapplication1.models.Prize;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrizeActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    RecycleAdapter adapter;
    ArrayList<Prize> prize_list;
    private String horse_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize);
        Bundle extras = getIntent().getExtras();
        horse_key = extras.getString("horse_id");
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
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.floatingActionButton7);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=getIntent().getExtras();
                final String horse_id=bundle.getString("horse_id");
                // Toast.makeText(getApplicationContext(), horse_id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PrizeActivity.this, AddPrizeActivity.class);
                Bundle bundle_extra=new Bundle();
                bundle_extra.putString("horse_id",horse_id);
                intent.putExtras(bundle_extra);
                startActivity(intent);
            }
        });

        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        prize_list = new ArrayList<>();
        RecyclerView recyclerview=(RecyclerView)findViewById(R.id.RecViewPrize);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        recyclerview.setLayoutManager(llm);
        adapter=new PrizeActivity.RecycleAdapter();
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
            Intent intent = new Intent(PrizeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            String horse_id=getHorseKey();
            Query applesQuery=mDatabase.child("users").child(mUserId).child("Prizes").orderByChild("horse_id").equalTo(horse_id);
            applesQuery.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            prize_list.clear();
                            Log.w("Prizes", "getUser:onCancelled " + dataSnapshot.toString());
                            Log.w("Prizes", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Prize prize = data.getValue(Prize.class);
                                prize_list.add(prize);

                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Prizes", "getUser:onCancelled", databaseError.toException());

                        }


                    }
            );


        }
    }
    public class RecycleAdapter extends RecyclerView.Adapter {
        private List<Prize> prizes;
        protected Context context;

        @Override
        public int getItemCount() {
            return prize_list.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Bundle bundle=getIntent().getExtras();
//            final String horse_id=bundle.getString("horse_id");
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_prize, parent, false);
            PrizeActivity.RecycleAdapter.SimpleItemViewHolder pvh = new PrizeActivity.RecycleAdapter.SimpleItemViewHolder(v,prizes);
            return pvh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            PrizeActivity.RecycleAdapter.SimpleItemViewHolder viewHolder = (PrizeActivity.RecycleAdapter.SimpleItemViewHolder) holder;
            viewHolder.position = position;
            Prize prize = prize_list.get(position);
            ((PrizeActivity.RecycleAdapter.SimpleItemViewHolder) holder).name.setText(prize.getName_prize());
            ((PrizeActivity.RecycleAdapter.SimpleItemViewHolder) holder).olymp.setText(prize.getOlymp_name());


        }


        public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView name;
            private final String TAG = PrizeActivity.RecycleAdapter.SimpleItemViewHolder.class.getSimpleName();
            public TextView olymp;
            public int position;
            private List<Prize> prizes;
            public SimpleItemViewHolder(View itemView, final List<Prize> prizes) {
                super(itemView);
                itemView.setOnClickListener(this);
                Bundle bundle=getIntent().getExtras();
                //horse_id=bundle.getString("horse_id");
                name = (TextView) itemView.findViewById(R.id.prize_name_srow);
                olymp = (TextView) itemView.findViewById(R.id.prize_olymp_srow);


            }


            @Override
            public void onClick(View view) {
                Bundle bundle=getIntent().getExtras();
                final String horse_id=bundle.getString("horse_id");
                Intent newIntent = new Intent(PrizeActivity.this,PrizeDetailsActivity.class);
                newIntent.putExtra("horse_id",horse_id);
                newIntent.putExtra("prizes", prize_list.get(position));
                //Toast.makeText(getApplicationContext(), horse_id, Toast.LENGTH_SHORT).show();
                PrizeActivity.this.startActivity(newIntent);
                name = (TextView)findViewById(R.id.prize_name_srow);

            }
        }




    }
}
