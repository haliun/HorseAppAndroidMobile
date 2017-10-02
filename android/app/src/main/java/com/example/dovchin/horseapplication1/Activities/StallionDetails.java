package com.example.dovchin.horseapplication1.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.methods.AddStallionActivity;
import com.example.dovchin.horseapplication1.methods.SearchStallion;
import com.example.dovchin.horseapplication1.models.Stallion;
import com.example.dovchin.horseapplication1.models.ToDoList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StallionDetails extends AppCompatActivity {
    ArrayList<Stallion> stallion_list;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stallion_details);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final Stallion stallions = (Stallion) extras.getSerializable("stallions");
            if (stallions != null) {
                final TextView nameEdtText = (TextView)findViewById(R.id.textView_azarganer);
                TextView breedEditText = (TextView)findViewById(R.id.textView_infugshil);
                TextView txtEdtDate=(TextView)findViewById(R.id.textView_infognoo);
                TextView bloodtext=(TextView)findViewById(R.id.textView_inftsus);
                TextView colortxt=(TextView)findViewById(R.id.textView_infzus);
                TextView placetxt=(TextView)findViewById(R.id.textView_infgazar);
                TextView mothertxt=(TextView)findViewById(R.id.textView_infeh);
                TextView fathertxt=(TextView)findViewById(R.id.textView_infetseg);
                TextView activeVal=(TextView)findViewById(R.id.textViewStalAct);
                TextView idtxt=(TextView)findViewById(R.id.textView_infid);
                final ImageView img_horse=(ImageView)findViewById(R.id.imageView4);
                final ImageView img_mark=(ImageView)findViewById(R.id.imageView8);
                if(stallions.getActivity().equals(true)){
                    activeVal.setText("Идэвхитэй");
                }else{
                    activeVal.setText("Идэвхигүй");
                }

                nameEdtText.setText(stallions.getName_horse());
                breedEditText.setText(stallions.getBreed());
                bloodtext.setText(stallions.getBlood());
                colortxt.setText(stallions.getColor_horse());
                placetxt.setText(stallions.getBirth_place());
                mothertxt.setText(stallions.getMother_id());
                fathertxt.setText(stallions.getFather_id());
                idtxt.setText(stallions.getPrivate_number());
                txtEdtDate.setText(stallions.getBirth_date());
                Picasso.with(img_horse.getContext()).load(stallions.getHorse_image()).networkPolicy(NetworkPolicy.OFFLINE).into(img_horse, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(img_horse.getContext()).load(stallions.getHorse_image()).into(img_horse);
                    }
                });
                Picasso.with(img_mark.getContext()).load(stallions.getMarking_image()).networkPolicy(NetworkPolicy.OFFLINE).into(img_mark, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(img_mark.getContext()).load(stallions.getMarking_image()).into(img_mark);
                    }
                });
                //activeVal.setText(stallions.getActivity().toString());
                Button editbtn=(Button)findViewById(R.id.button13);
                Button delbtn=(Button)findViewById(R.id.button14);
                Button groupbtn=(Button)findViewById(R.id.button16);
                Button olympbtn=(Button)findViewById(R.id.button17);
                Button prizebtn=(Button)findViewById(R.id.button18);
                final String s_key=stallions.getStallion_id();
                final String horse_name=stallions.getName_horse();

                editbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(StallionDetails.this,AddStallionActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("stallions",stallions);
                        newIntent.putExtras(bundle);
                        startActivity(newIntent);
                        finish();

                    }
                });
                delbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        deleteStallion((String) s_key);
                        //Toast.makeText(getApplicationContext(), hash.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
                groupbtn.setOnClickListener(new View.OnClickListener() {
                    public int position;
                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(StallionDetails.this, StallionGroupActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("horse_name",horse_name);
                        newIntent.putExtras(bundle);
                        startActivity(newIntent);

                    }
                });
                prizebtn.setOnClickListener(new View.OnClickListener() {
                    public int position;
                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(StallionDetails.this, PrizeActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("horse_id",s_key);
                        newIntent.putExtras(bundle);
                        startActivity(newIntent);

                    }
                });
                olympbtn.setOnClickListener(new View.OnClickListener() {
                    public int position;
                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(StallionDetails.this, OlympActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("horse_id",s_key);
                        newIntent.putExtras(bundle);
                        startActivity(newIntent);


                    }
                });



            }
        }
    }
    public void deleteStallion(final String s_key){
        final TextView nameEditText=(TextView) findViewById(R.id.textView_azarganer);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(StallionDetails.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();

            AlertDialog.Builder builder = new AlertDialog.Builder(StallionDetails.this);
            builder.setMessage("Та энэ азаргыг устгахдаа итгэлтэй байна уу?").setCancelable(false)
                    .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("Stallions").orderByChild("stallion_id").equalTo(s_key);
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(getApplicationContext(), "Амжилттай устгагдлаа!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(StallionDetails.this, SearchStallion.class);
                                    Bundle bundle=getIntent().getExtras();
                                    final String horse_id=bundle.getString("horse_id");
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
