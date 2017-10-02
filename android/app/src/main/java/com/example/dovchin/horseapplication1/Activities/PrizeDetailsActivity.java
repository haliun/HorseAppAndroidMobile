package com.example.dovchin.horseapplication1.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.methods.AddPrizeActivity;

import com.example.dovchin.horseapplication1.methods.SearchColt;
import com.example.dovchin.horseapplication1.methods.SearchFemaleHorse;
import com.example.dovchin.horseapplication1.methods.SearchMaleHorse;
import com.example.dovchin.horseapplication1.methods.SearchStallion;
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

public class PrizeDetailsActivity extends AppCompatActivity {
    ArrayList<Prize> prizes;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize_details);
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
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final Prize prize = (Prize) extras.get("prizes");
            final String horse_id=extras.getString("horse_id");
            if (prize != null) {
                final TextView nameEdtText = (TextView)findViewById(R.id.textViewDetPName);
                TextView olympname = (TextView)findViewById(R.id.textViewDetPOlymp);
                TextView txtEdtDate=(TextView)findViewById(R.id.textViewdetPDate);
                TextView desc=(TextView)findViewById(R.id.textViewDetPdesc);


                nameEdtText.setText(prize.getName_prize());
                olympname.setText(prize.getOlymp_name());
                desc.setText(prize.getDescription());
                txtEdtDate.setText(prize.getDatePrize());

                Button editbtn=(Button)findViewById(R.id.button_edit_prize);
                Button delbtn=(Button)findViewById(R.id.button_del_prize);


                final String s_key=prize.getPrize_id();

                editbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(PrizeDetailsActivity.this, AddPrizeActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("prizes",prize);
                        bundle.putString("horse_id",horse_id);
                        newIntent.putExtras(bundle);
                        startActivity(newIntent);

                    }
                });
                delbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        deletePrizes((String) s_key);
                        //Toast.makeText(getApplicationContext(), hash.toString(), Toast.LENGTH_SHORT).show();

                    }
                });




            }
        }
    }
    public void deletePrizes(final String s_key){
        final TextView nameEditText=(TextView) findViewById(R.id.textViewDetPName);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(PrizeDetailsActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();

            AlertDialog.Builder builder = new AlertDialog.Builder(PrizeDetailsActivity.this);
            builder.setMessage("Та устгахдаа итгэлтэй байна уу?").setCancelable(false)
                    .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("Prizes").orderByChild("prize_id").equalTo(s_key);
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                    Bundle bundle=getIntent().getExtras();
                                    final String horse_id=bundle.getString("horse_id");
                                    Toast.makeText(getApplicationContext(), "Амжилттай устгагдлаа!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PrizeDetailsActivity.this, PrizeActivity.class);
                                    intent.putExtra("horse_id",horse_id);
                                    startActivity(intent);

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

