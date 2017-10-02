package com.example.dovchin.horseapplication1.Activities;

import android.content.Intent;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dovchin.horseapplication1.Launcher.PrefManager;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.methods.AddColtActivity;
import com.example.dovchin.horseapplication1.methods.AddUserDatas;
import com.example.dovchin.horseapplication1.methods.CircleTransform;
import com.example.dovchin.horseapplication1.methods.SearchColt;
import com.example.dovchin.horseapplication1.methods.SearchFemaleHorse;
import com.example.dovchin.horseapplication1.methods.SearchMaleHorse;
import com.example.dovchin.horseapplication1.methods.SearchStallion;
import com.example.dovchin.horseapplication1.models.Stallion;
import com.example.dovchin.horseapplication1.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
    ArrayList<User> users;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
    private User value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if(mFirebaseUser==null){
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            final TextView nameEdtText = (TextView)findViewById(R.id.textView154);
            final TextView surname = (TextView)findViewById(R.id.textView153);
            final TextView email=(TextView)findViewById(R.id.textView155);
            final TextView city=(TextView)findViewById(R.id.textView157);
            final TextView sum=(TextView)findViewById(R.id.textView159);
            final TextView notStal=(TextView)findViewById(R.id.badge_notification_1);
            final TextView notMale=(TextView)findViewById(R.id.badge_notification_2);
            final TextView notColt=(TextView)findViewById(R.id.badge_notification_3);
            final TextView notFemale=(TextView)findViewById(R.id.badge_notification_4);
            final CircularImageView img=(CircularImageView)findViewById(R.id.circularImageView3);
            Button stallion=(Button)findViewById(R.id.not_stal);
            Button male=(Button)findViewById(R.id.not_male);
            Button female=(Button)findViewById(R.id.not_female);
            Button colt=(Button)findViewById(R.id.not_colt);
            Button edit=(Button)findViewById(R.id.button_edit_user) ;

            mDatabase.child("users").child(mUserId).child("UserData").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.w("StallionList", "getUser:onCancelled " + dataSnapshot.toString());
                            Log.w("StallionList", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                value = data.getValue(User.class);
                                nameEdtText.setText(value.getFirst_name());
                                surname.setText(value.getLast_name());
                                email.setText(value.getEmail());
                                city.setText(value.getCity());
                                sum.setText(value.getDegree());
                                Glide.with(getApplicationContext()).load(value.getProfile_img()).transform(new CircleTransform(UserProfileActivity.this)).into(img);

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("StallionList", "getUser:onCancelled", databaseError.toException());

                        }


                    }
            );
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newIntent = new Intent(UserProfileActivity.this, AddUserDatas.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("users",value);
                    newIntent.putExtras(bundle);
                    startActivity(newIntent);
                }
            });

            if(mFirebaseUser==null){
                PrefManager prefManager = new PrefManager(getApplicationContext());

                // make first time launch TRUE
                prefManager.setFirstTimeLaunch(true);

                startActivity(new Intent(UserProfileActivity.this, WelcomeActivity.class));
            }else {
                mUserId=mFirebaseUser.getUid();
                Query query = mDatabase.child("users").child(mUserId).child("Stallions").orderByChild("activity").equalTo(true);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long countStal= dataSnapshot.getChildrenCount();
                        notStal.setText(String.valueOf(countStal));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Query query1 = mDatabase.child("users").child(mUserId).child("Colts").orderByChild("activity").equalTo(true);
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long countColt= dataSnapshot.getChildrenCount();
                        notColt.setText(String.valueOf(countColt));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Query query2 = mDatabase.child("users").child(mUserId).child("FemaleHorses").orderByChild("activity").equalTo(true);
                query2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long countColt= dataSnapshot.getChildrenCount();
                        notFemale.setText(String.valueOf(countColt));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Query query3 = mDatabase.child("users").child(mUserId).child("MaleHorses").orderByChild("activity").equalTo(true);
                query3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long countColt= dataSnapshot.getChildrenCount();
                        notMale.setText(String.valueOf(countColt));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        }
            stallion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SearchStallion.class);
                    startActivity(intent);
                }
            });
            colt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SearchColt.class);
                    startActivity(intent);
                }
            });
            female.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SearchFemaleHorse.class);
                    startActivity(intent);
                }
            });
            male.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SearchMaleHorse.class);
                    startActivity(intent);
                }
            });
        }
}}
