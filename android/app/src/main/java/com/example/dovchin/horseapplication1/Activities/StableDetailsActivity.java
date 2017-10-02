package com.example.dovchin.horseapplication1.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.methods.AddFoodVitActivity;
import com.example.dovchin.horseapplication1.methods.AddStableActivity;
import com.example.dovchin.horseapplication1.models.FoodVit;
import com.example.dovchin.horseapplication1.models.Stable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StableDetailsActivity extends AppCompatActivity {
    ArrayList<Stable> foodVits;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stable_details);
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final Stable foodVit = (Stable) extras.get("stable");
            if (foodVit != null) {
                final TextView nameEdtText = (TextView)findViewById(R.id.textView_st_name);
                TextView typeTxtView = (TextView)findViewById(R.id.textView_st_type);
                TextView txtEdtAimg=(TextView)findViewById(R.id.textView_st_aimag);
                TextView sum=(TextView)findViewById(R.id.textView_st_sum);
                TextView season=(TextView)findViewById(R.id.textView_st_season);
                TextView malchintoo=(TextView)findViewById(R.id.textView_st_malch_too);
                TextView aduu_too=(TextView)findViewById(R.id.textView_st_aduu_too);
                TextView malchin=(TextView)findViewById(R.id.textView_st_malchin);
                TextView us=(TextView)findViewById(R.id.textView_st_us);
                TextView uvs=(TextView)findViewById(R.id.textView_st_uvs);
                TextView nar=(TextView)findViewById(R.id.textView_st_nar);
                TextView talbai=(TextView)findViewById(R.id.textView_st_talbai);
                nameEdtText.setText(foodVit.getName());
                typeTxtView.setText(foodVit.getType());
                txtEdtAimg.setText(foodVit.getCity());
                sum.setText(foodVit.getDistrict());
                season.setText(foodVit.getSeason());
                malchintoo.setText(foodVit.getNum_person());
                malchin.setText(foodVit.getMain_person());
                aduu_too.setText(foodVit.getNum_horse());
                us.setText(foodVit.getWater());
                uvs.setText(foodVit.getGrass());
                nar.setText(foodVit.getSun());
                talbai.setText(foodVit.getSquare());

                Button editbtn=(Button)findViewById(R.id.button2);
                Button delbtn=(Button)findViewById(R.id.button7);


                final String s_key=foodVit.getId();

                editbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(StableDetailsActivity.this, AddStableActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("stable",foodVit);
                        newIntent.putExtras(bundle);
                        startActivity(newIntent);

                    }
                });
                delbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        deleteOlymps((String) s_key);

                    }
                });




            }
        }
    }
    public void deleteOlymps(final String s_key){
        final TextView nameEdtText = (TextView)findViewById(R.id.textView_st_name);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(StableDetailsActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            AlertDialog.Builder builder = new AlertDialog.Builder(StableDetailsActivity.this);
            builder.setMessage("Та устгахдаа итгэлтэй байна уу?").setCancelable(false)
                    .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("Stable").orderByChild("id").equalTo(s_key);
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(getApplicationContext(), "Амжилттай устгагдлаа!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(StableDetailsActivity.this, StableActivity.class);
                                    startActivity(intent);
                                    StableDetailsActivity.this.finish();

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

