package com.example.dovchin.horseapplication1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by dovchin on 8/9/2016.
 */
public class AddStallion extends AppCompatActivity {
    EditText name,id_azarga,huis,zus,
        ugshil,tsus,tursungazar,tursunognoo,etseg,eh;
    Spinner turul;
    ImageView zurag, tamga;
    String aturul,aname, aid, ahuis, azus, augshil, atsus, atursungazar, atursunognoo,aetseg,aeh;
    byte azurag[], atamga[];
    Bitmap bitmap=null;
    Bitmap bitmap1=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stallion_add);

        name=(EditText) findViewById(R.id.editTextname);
        id_azarga=(EditText)findViewById(R.id.editTextID);

        zurag=(ImageView) findViewById(R.id.imageViewzurag);
        //tamga=(ImageView) findViewById(R.id.imageViewtamga);
        zus=(EditText)findViewById(R.id.editTextZus);
        ugshil=(EditText)findViewById(R.id.editTextUgshil);
        tsus=(EditText)findViewById(R.id.editTexttsus);
        tursungazar=(EditText)findViewById(R.id.editTextTGazar);
        tursunognoo=(EditText)findViewById(R.id.editTextOgnoo);
        etseg=(EditText)findViewById(R.id.editTextetseg);
        eh=(EditText)findViewById(R.id.editTexteh);
        //spinner
       // turul=(Spinner)findViewById(R.id.spinner2);
       // adapter= ArrayAdapter.createFromResource(this,R.array.TypeList,android.R.layout.simple_spinner_item);
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //turul.setAdapter(adapter);
       // turul.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
         //   @Override
        //    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //
        //    }

         //   @Override
         //   public void onNothingSelected(AdapterView<?> adapterView) {

        //    }
       // });

    }
    public void selectImage(View view){
        Intent intent=new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK ){
            Uri selectedImage=data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
                azurag = bos.toByteArray();
               // atamga = bos.toByteArray();
                zurag.setImageBitmap(bitmap);

            } catch(FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public void submit(View view){
        aname=name.getText().toString();
        aid=id_azarga.getText().toString();
        augshil=ugshil.getText().toString();
        azus=zus.getText().toString();
        atsus=tsus.getText().toString();
        atursungazar=tursungazar.getText().toString();
        atursunognoo=tursunognoo.getText().toString();
        aetseg=etseg.getText().toString();
        aeh=eh.getText().toString();
        MyHelper hob=new MyHelper(this);
        hob.putInfo(hob,aname,aid,augshil,azus,atsus,atursunognoo,atursungazar,azurag,aetseg,aeh);
        finish();

    }
}
