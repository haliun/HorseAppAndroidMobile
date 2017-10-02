package com.example.dovchin.horseapplication1.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.methods.AddMaleHorseActivity;
import com.example.dovchin.horseapplication1.methods.AddStallionActivity;
import com.example.dovchin.horseapplication1.methods.SearchMaleHorse;
import com.example.dovchin.horseapplication1.models.MaleHorse;
import com.example.dovchin.horseapplication1.models.Stallion;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MaleHorseDetails extends AppCompatActivity {
    ArrayList<MaleHorse> maleHorse_list;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_horse_details);
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final MaleHorse maleHorse = (MaleHorse) extras.get("maleHorses");
            if (maleHorse != null) {
                final TextView nameEdtText = (TextView)findViewById(R.id.textView_ner_MAle);
                TextView breedEditText = (TextView)findViewById(R.id.textView102);
                TextView txtEdtDate=(TextView)findViewById(R.id.textView105);
                TextView bloodtext=(TextView)findViewById(R.id.textView107);
                TextView colortxt=(TextView)findViewById(R.id.textView104);
                TextView placetxt=(TextView)findViewById(R.id.textView106);
                TextView mothertxt=(TextView)findViewById(R.id.textView109);
                TextView fathertxt=(TextView)findViewById(R.id.textView108);
                final String group=maleHorse.getGroup_id();
                Button group_btn=(Button)findViewById(R.id.button24);
                group_btn.setText(maleHorse.getGroup_id());
                TextView idtxt=(TextView)findViewById(R.id.textView103);
                final ImageView img_horse=(ImageView)findViewById(R.id.imageViewzuragm);
                final ImageView img_mark=(ImageView)findViewById(R.id.imageView_marking_image);
                TextView activeVal=(TextView)findViewById(R.id.textView150);
                if(maleHorse.getActivity().toString().equals("true")){
                    activeVal.setText("Идэвхитэй");
                }else{
                    activeVal.setText("Идэвхигүй");
                }
                nameEdtText.setText(maleHorse.getName_horse());
                breedEditText.setText(maleHorse.getBreed());
                bloodtext.setText(maleHorse.getBlood());
                colortxt.setText(maleHorse.getColor_horse());
                placetxt.setText(maleHorse.getBirth_place());
                mothertxt.setText(maleHorse.getMother_id());
                fathertxt.setText(maleHorse.getFather_id());
                idtxt.setText(maleHorse.getPrivate_number());
                txtEdtDate.setText(maleHorse.getBirth_date());
                Picasso.with(img_horse.getContext()).load(maleHorse.getHorse_image()).networkPolicy(NetworkPolicy.OFFLINE).into(img_horse, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(img_horse.getContext()).load(maleHorse.getHorse_image()).into(img_horse);
                    }
                });
                Picasso.with(img_mark.getContext()).load(maleHorse.getMarking_image()).networkPolicy(NetworkPolicy.OFFLINE).into(img_mark, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(img_mark.getContext()).load(maleHorse.getMarking_image()).into(img_mark);
                    }
                });
                group_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mFirebaseAuth = FirebaseAuth.getInstance();
                        mFirebaseUser = mFirebaseAuth.getCurrentUser();
                        mUserId=mFirebaseUser.getUid();
                        Query applesQuery = mDatabase.child("users").child(mUserId).child("Stallions").orderByChild("name_horse").equalTo(group);
                        applesQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    String classy=dataSnapshot.getValue().toString();

                                    /*stallion.setName_horse(name);
                                    Intent newIntent = new Intent(MaleHorseDetails.this,StallionDetails.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("stallions",stallion);
                                    newIntent.putExtras(bundle);
                                    startActivity(newIntent);*/

                                }



                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
                Button editbtn=(Button)findViewById(R.id.button_editMale);
                Button delbtn=(Button)findViewById(R.id.button_delMale);
               // Button groupbtn=(Button)findViewById(R.id.button16);
                Button olympbtn=(Button)findViewById(R.id.button_olympMale);
                Button prizebtn=(Button)findViewById(R.id.button_prizemale);
                final String s_key=maleHorse.getMaleHorse_id();

                editbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(MaleHorseDetails.this,AddMaleHorseActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("maleHorses",maleHorse);
                        newIntent.putExtras(bundle);
                        startActivity(newIntent);

                    }
                });
                delbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        deleteMaleHorse((String) s_key);
                        //Toast.makeText(getApplicationContext(), hash.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

                prizebtn.setOnClickListener(new View.OnClickListener() {
                    public int position;
                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(MaleHorseDetails.this, PrizeActivity.class);
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
                        Intent newIntent = new Intent(MaleHorseDetails.this, OlympActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("horse_id",s_key);
                        newIntent.putExtras(bundle);
                        startActivity(newIntent);


                    }
                });



            }
        }
    }
    public void deleteMaleHorse(final String s_key){
        final TextView nameEditText=(TextView) findViewById(R.id.textView_azarganer);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(MaleHorseDetails.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();

            AlertDialog.Builder builder = new AlertDialog.Builder(MaleHorseDetails.this);
            builder.setMessage("Та энэ морийг устгахдаа итэлтэй байна уу?").setCancelable(false)
                    .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("MaleHorses").orderByChild("male_horse_id").equalTo(s_key);
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(getApplicationContext(), "Амжилттай устгагдлаа!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MaleHorseDetails.this, SearchMaleHorse.class);
                                    Bundle bundle=getIntent().getExtras();
                                    final String horse_id=bundle.getString("horse_id");
                                    Toast.makeText(getApplicationContext(), horse_id, Toast.LENGTH_SHORT).show();
                                    intent.putExtra("horse_id",horse_id);
                                    startActivity(intent);
                                    MaleHorseDetails.this.finish();

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    })
                    .setNegativeButton("Тийм", new DialogInterface.OnClickListener() {
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



