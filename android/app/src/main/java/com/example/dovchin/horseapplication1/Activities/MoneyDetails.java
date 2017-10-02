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
import com.example.dovchin.horseapplication1.methods.AddMoneyActivity;
import com.example.dovchin.horseapplication1.models.FoodVit;
import com.example.dovchin.horseapplication1.models.Money;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MoneyDetails extends AppCompatActivity {
    ArrayList<Money> foodVits;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_details);
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final Money foodVit = (Money) extras.get("money");
            if (foodVit != null) {
                final TextView nameEdtText = (TextView)findViewById(R.id.textView_money_name);
                TextView typeTxtView = (TextView)findViewById(R.id.textView_money_type);
                TextView txtEdtDate=(TextView)findViewById(R.id.textView_money_date);
                TextView quant=(TextView)findViewById(R.id.textViewFoodQuant);
                TextView sum=(TextView)findViewById(R.id.textView_money_sum);
                TextView desc=(TextView)findViewById(R.id.textView_money_desc);


                nameEdtText.setText(foodVit.getName());
                typeTxtView.setText(foodVit.getType());
                sum.setText(foodVit.getSum());
                desc.setText(foodVit.getDesc());
                txtEdtDate.setText(foodVit.getDate());

                Button editbtn=(Button)findViewById(R.id.button8);
                Button delbtn=(Button)findViewById(R.id.button9);


                final String s_key=foodVit.getId();

                editbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(MoneyDetails.this, AddMoneyActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("money",foodVit);
                        newIntent.putExtras(bundle);
                        startActivity(newIntent);

                    }
                });
                delbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        deleteOlymps((String) s_key);
                        //Toast.makeText(getApplicationContext(), hash.toString(), Toast.LENGTH_SHORT).show();

                    }
                });




            }
        }
    }
    public void deleteOlymps(final String s_key){
        final TextView nameEdtText = (TextView)findViewById(R.id.textView_money_name);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(MoneyDetails.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();

            AlertDialog.Builder builder = new AlertDialog.Builder(MoneyDetails.this);
            builder.setMessage("Та устгахдаа итгэлтэй байна уу?").setCancelable(false)
                    .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("Money").orderByChild("id").equalTo(s_key);
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(getApplicationContext(), "Амжилттай устгагдлаа!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MoneyDetails.this, MoneyActivity.class);
                                    startActivity(intent);
                                    MoneyDetails.this.finish();

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
