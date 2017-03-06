package com.example.dovchin.horseapplication1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by amour on 2/9/2017.
 */
public class User_detial extends AppCompatActivity {
    EditText fisrtname, lastname, email, city, sum,u_id;
    String fname, lname, mail, ccity, ssum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stallion_layout);
        fisrtname = (EditText) findViewById(R.id.editTextfname);
        lastname = (EditText) findViewById(R.id.editTextlname);
        email = (EditText) findViewById(R.id.editTextemail);
        city = (EditText) findViewById(R.id.editTextcity);
        sum = (EditText) findViewById(R.id.editTextsum);
    }
    public void submit_user(View view){
        fname=fisrtname.getText().toString();
        lname=lastname.getText().toString();
        mail=email.getText().toString();
        ccity=email.getText().toString();
        ssum=sum.getText().toString();
        MyHelperUserAdd hobu=new MyHelperUserAdd(this);
        hobu.putInfoUser(hobu,fname,lname,mail,ccity,ssum);
        finish();
    }

}
