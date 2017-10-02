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
import com.example.dovchin.horseapplication1.methods.AddFemaleHorseActivity;
import com.example.dovchin.horseapplication1.methods.SearchFemaleHorse;
import com.example.dovchin.horseapplication1.models.FemaleHorse;
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

public class FemaleHorsesDetails extends AppCompatActivity {
    ArrayList<FemaleHorse> femalehorses_list;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_horses_details);
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final FemaleHorse femaleHorse = (FemaleHorse) extras.get("female_horses");
            if (femaleHorse != null) {
                final TextView nameEdtText = (TextView)findViewById(R.id.textViewName_female);
                TextView breedEditText = (TextView)findViewById(R.id.textViewBreed_female);
                TextView txtEdtDate=(TextView)findViewById(R.id.textViewDay_female);
                TextView bloodtext=(TextView)findViewById(R.id.textViewBlood_female);
                TextView colortxt=(TextView)findViewById(R.id.textViewColor_female);
                TextView placetxt=(TextView)findViewById(R.id.textViewPlace_female);
                TextView mothertxt=(TextView)findViewById(R.id.textViewMom_female);
                TextView fathertxt=(TextView)findViewById(R.id.textViewDad_female);
                TextView idtxt=(TextView)findViewById(R.id.textViewId_female);
                final ImageView img_horse=(ImageView)findViewById(R.id.imageViewImgFemale);
                final ImageView img_mark=(ImageView)findViewById(R.id.imageViewMarkFemale);
                Button group_btn=(Button)findViewById(R.id.button_group_female);
                TextView activeVal=(TextView)findViewById(R.id.textViewStatFem);
                if(femaleHorse.getActivity().toString().equals("true")){
                    activeVal.setText("Идэвхитэй");
                }else{
                    activeVal.setText("Идэвхигүй");
                }

                group_btn.setText(femaleHorse.getGroup());
                nameEdtText.setText(femaleHorse.getName_horse());
                breedEditText.setText(femaleHorse.getBreed());
                bloodtext.setText(femaleHorse.getBlood());
                colortxt.setText(femaleHorse.getColor_horse());
                placetxt.setText(femaleHorse.getBirth_place());
                mothertxt.setText(femaleHorse.getMother_id());
                fathertxt.setText(femaleHorse.getFather_id());
                idtxt.setText(femaleHorse.getPrivate_number());
                txtEdtDate.setText(femaleHorse.getBirth_date());
                Picasso.with(img_horse.getContext()).load(femaleHorse.getHorse_image()).networkPolicy(NetworkPolicy.OFFLINE).into(img_horse, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(img_horse.getContext()).load(femaleHorse.getHorse_image()).into(img_horse);
                    }
                });
                Picasso.with(img_mark.getContext()).load(femaleHorse.getMarking_image()).networkPolicy(NetworkPolicy.OFFLINE).into(img_mark, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(img_mark.getContext()).load(femaleHorse.getMarking_image()).into(img_mark);
                    }
                });

                Button editbtn=(Button)findViewById(R.id.button_edit_female);
                Button delbtn=(Button)findViewById(R.id.button_del_female);


                final String s_key=femaleHorse.getFemale_id();

                editbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(FemaleHorsesDetails.this, AddFemaleHorseActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("female_horses",femaleHorse);
                        newIntent.putExtras(bundle);
                        startActivity(newIntent);

                    }
                });
                delbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        deleteFemaleHorses((String) s_key);
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
    public void deleteFemaleHorses(final String s_key){
        final TextView nameEditText=(TextView) findViewById(R.id.textViewName_female);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(FemaleHorsesDetails.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();

            AlertDialog.Builder builder = new AlertDialog.Builder(FemaleHorsesDetails.this);
            builder.setMessage("Та энэ гүүг устгахдаа итгэлтэй байна уу?").setCancelable(false)
                    .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("FemaleHorses").orderByChild("female_id").equalTo(s_key);
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(getApplicationContext(), "Амжилттай устгагдлаа!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(FemaleHorsesDetails.this, SearchFemaleHorse.class);
                                    startActivity(intent);
                                    FemaleHorsesDetails.this.finish();

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
