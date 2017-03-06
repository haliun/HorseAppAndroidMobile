package com.example.dovchin.horseapplication1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by dovchin on 8/19/2016.
 */
public class AddMaleHorse extends AppCompatActivity{
    EditText mname,mid_azarga,mzus,
            mugshil,mtsus,mtursungazar,mtursunognoo,metseg,meh;
    ImageView mzurag, tamga;
    String maname, maid, mazus,maugshil, matsus,matursungazar, matursunognoo,maetseg,maeh;
    byte mazurag[], matamga[];
    Bitmap mbitmap=null;


    ArrayAdapter<CharSequence> madapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.male_horse_add);

        mname=(EditText) findViewById(R.id.editTextnamem);
        mid_azarga=(EditText)findViewById(R.id.editTextIDm);
        mzurag=(ImageView) findViewById(R.id.imageViewzuragm);
        //tamga=(ImageView) findViewById(R.id.imageViewtamga);
        mzus=(EditText)findViewById(R.id.editTextZusm);
        mugshil=(EditText)findViewById(R.id.editTextUgshilm);
        mtsus=(EditText)findViewById(R.id.editTexttsusm);
        mtursungazar=(EditText)findViewById(R.id.editTextTGazarm);
        mtursunognoo=(EditText)findViewById(R.id.editTextOgnoom);
        metseg=(EditText)findViewById(R.id.editTextetsegm);
        meh=(EditText)findViewById(R.id.editTextehm);



    }
    public void selectImage(View view){
        Intent intent=new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK ){
            Uri selectedImage=data.getData();
            try {
                mbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                mbitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
                mazurag = bos.toByteArray();
                matamga = bos.toByteArray();
                mzurag.setImageBitmap(mbitmap);

            } catch(FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public void submitMori(View view){
        maname=mname.getText().toString();
        maid=mid_azarga.getText().toString();
        maugshil=mugshil.getText().toString();
        mazus=mzus.getText().toString();
        matsus=mtsus.getText().toString();
        matursungazar=mtursungazar.getText().toString();
        matursunognoo=mtursunognoo.getText().toString();
        maetseg=metseg.getText().toString();
        maeh=meh.getText().toString();
        MyHelper mmhob=new MyHelper(this);
        mmhob.putInfoMori(mmhob,maname,maid,maugshil,mazus,matsus,matursunognoo,matursungazar,mazurag,maetseg,maeh);
        finish();

    }

}
