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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.methods.AddColtActivity;
import com.example.dovchin.horseapplication1.methods.SearchColt;
import com.example.dovchin.horseapplication1.models.Colt;
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

public class ColtDetailsActivity extends AppCompatActivity {
    ArrayList<Colt> colt_list;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colt_details);
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final Colt colt = (Colt) extras.get("colts");
            if (colt != null) {
                final TextView nameEdtText = (TextView)findViewById(R.id.textView_colt_name);
                TextView breedEditText = (TextView)findViewById(R.id.textView_colt_creed);
                TextView txtEdtDate=(TextView)findViewById(R.id.textView_colt_date);
                TextView bloodtext=(TextView)findViewById(R.id.textView_colt_blood);
                TextView colortxt=(TextView)findViewById(R.id.textView_colt_color);
                TextView placetxt=(TextView)findViewById(R.id.textView_colt_place);
                TextView mothertxt=(TextView)findViewById(R.id.textView_colt_mom);
                TextView fathertxt=(TextView)findViewById(R.id.textView_colt_dad);
                TextView idtxt=(TextView)findViewById(R.id.textView_colt_id);
                final ImageView img_horse=(ImageView)findViewById(R.id.imageView19);
                final ImageView img_mark=(ImageView)findViewById(R.id.imageViewColtMark);
                Button group_btn=(Button)findViewById(R.id.button_colt_group);
                TextView activeVal=(TextView)findViewById(R.id.textViewStatusColt);
                if(colt.getActivity().toString().equals("true")){
                    activeVal.setText("Идэвхитэй");
                }else{
                    activeVal.setText("Идэвхигүй");
                }
                group_btn.setText(colt.getGroup());
                nameEdtText.setText(colt.getName_horse());
                breedEditText.setText(colt.getBreed());
                bloodtext.setText(colt.getBlood());
                colortxt.setText(colt.getColor_horse());
                placetxt.setText(colt.getBirth_place());
                mothertxt.setText(colt.getMother_id());
                fathertxt.setText(colt.getFather_id());
                idtxt.setText(colt.getPrivate_number());
                txtEdtDate.setText(colt.getBirth_date());
                Picasso.with(img_horse.getContext()).load(colt.getHorse_image()).networkPolicy(NetworkPolicy.OFFLINE).into(img_horse, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(img_horse.getContext()).load(colt.getHorse_image()).into(img_horse);
                    }
                });
                Picasso.with(img_mark.getContext()).load(colt.getMarking_image()).networkPolicy(NetworkPolicy.OFFLINE).into(img_mark, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(img_mark.getContext()).load(colt.getMarking_image()).into(img_mark);
                    }
                });
                Button editbtn=(Button)findViewById(R.id.button27);
                Button delbtn=(Button)findViewById(R.id.button4);


                final String s_key=colt.getColt_id();

                editbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(ColtDetailsActivity.this, AddColtActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("colts",colt);
                        newIntent.putExtras(bundle);
                        startActivity(newIntent);

                    }
                });
                delbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        deleteColts((String) s_key);
                        //Toast.makeText(getApplicationContext(), hash.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
                group_btn.setOnClickListener(new View.OnClickListener() {
                    public int position;
                    @Override
                    public void onClick(View v) {

                    }
                });




            }

        }
    }
    public void deleteColts(final String s_key){
        final TextView nameEditText=(TextView) findViewById(R.id.textView_colt_name);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(ColtDetailsActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();

            AlertDialog.Builder builder = new AlertDialog.Builder(ColtDetailsActivity.this);
            builder.setMessage("Та энэ унагыг устгахдаа итгэлтэй байна уу?").setCancelable(false)
                    .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("Colts").orderByChild("colt_id").equalTo(s_key);
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(getApplicationContext(), "Амжилттай устгагдлаа!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ColtDetailsActivity.this, SearchColt.class);
                                    startActivity(intent);
                                    ColtDetailsActivity.this.finish();

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
