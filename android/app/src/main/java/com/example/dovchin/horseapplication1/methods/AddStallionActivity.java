package com.example.dovchin.horseapplication1.methods;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.gesture.GestureLibraries;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.models.Stallion;
import com.example.dovchin.horseapplication1.models.ToDoList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khaliun on 4/17/2017.
 */

public class AddStallionActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    Activity mActivity;
    private FirebaseUser mFirebaseUser;
    private static final int GALLERY_REQUEST=1, GALLERY_MARKING_REQUEST=2;
    private Uri selectedImage=null, selectedMarkinImg=null;
    int day, year, month;
    Bitmap bitmap=null,bitmap_tamga=null;
    Context ctx;
    ImageButton img_horse, img_marking;
    Intent intent;
    private ProgressDialog mProgress;
    private FirebaseStorage storage;
    private StorageReference mStorage;
    byte azurag[],atamga[];
    boolean clicked_img=false;
    boolean clicked_marking=false;
    private String mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stallion_add);
        Button btnDatePicker=(Button)findViewById(R.id.button_date_stal);
        final EditText txtDate=(EditText)findViewById(R.id.editTextOgnoo);

        mStorage= FirebaseStorage.getInstance().getReference();
        mProgress=new ProgressDialog(this);
        //get current date
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dd = new DatePickerDialog(AddStallionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy",java.util.Locale.getDefault());
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);

                                    txtDate.setText(formatter.format(date));


                                } catch (Exception ex) {

                                }
                            }
                        }, year, month, day);
                dd.show();
            }
        });
        img_marking=(ImageButton)findViewById(R.id.imageButton2);
        img_horse=(ImageButton)findViewById(R.id.imageButton);
        img_horse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });
        img_marking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_MARKING_REQUEST);

            }
        });
        //Button of saving stallion
        Button save_but=(Button)findViewById(R.id.button_save_stallion);
        save_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveStallion();

            }
        });

        //updating details of horse
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final Stallion stallions = (Stallion) extras.getSerializable("stallions");
            if (stallions != null) {
                final EditText nameEditText=(EditText)findViewById(R.id.editTextname);
                final EditText breedEdiText=(EditText)findViewById(R.id.editTextUgshil);
                final EditText textDate=(EditText)findViewById(R.id.editTextOgnoo);
                final EditText priv_num=(EditText)findViewById(R.id.editTextID);
                final EditText blood=(EditText)findViewById(R.id.editTexttsus);
                final EditText color=(EditText)findViewById(R.id.editTextZus);
                final EditText birth_place=(EditText)findViewById(R.id.editTextTGazar);
                final CheckBox active=(CheckBox)findViewById(R.id.checkBox_Stallion);
                final EditText mom=(EditText)findViewById(R.id.editTexteh);
                final EditText dad=(EditText)findViewById(R.id.editTextetseg);
                final String local_img;
                final String local_mark;
                img_marking=(ImageButton)findViewById(R.id.imageButton2);
                img_horse=(ImageButton)findViewById(R.id.imageButton);
                img_horse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(img_horse.isPressed()) {
                            clicked_img=true;
                        }else{
                            clicked_img=false;
                        }
                        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent,GALLERY_REQUEST);

                    }
                });
                img_marking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(img_marking.isPressed()) {
                            clicked_marking=true;
                        }else{
                            clicked_marking=false;
                        }
                        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent,GALLERY_MARKING_REQUEST);

                    }
                });
                nameEditText.setText(stallions.getName_horse());
                breedEdiText.setText(stallions.getBreed());
                priv_num.setText(stallions.getPrivate_number());
                textDate.setText(stallions.getBirth_date());
                birth_place.setText(stallions.getBirth_place());
                blood.setText(stallions.getBlood());
                color.setText(stallions.getColor_horse());
                active.setChecked(stallions.getActivity());
                mom.setText(stallions.getMother_id());
                dad.setText(stallions.getFather_id());
                Picasso.with(img_horse.getContext()).load(stallions.getHorse_image()).into(img_horse);
                Picasso.with(img_marking.getContext()).load(stallions.getMarking_image()).into(img_marking);
                Button btn_upd=(Button)findViewById(R.id.button_save_stallion);
                btn_upd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name=stallions.getName_horse();
                        String img=stallions.getHorse_image();
                        String img_marking=stallions.getMarking_image();
                        String key_horse=stallions.getStallion_id();
                        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                        UpdateStallion((String) name,(Boolean) clicked_img, (Boolean) clicked_marking,(String) img, (String) key_horse, (String) img_marking);



                    }
                });

            }
        }


    }
    private void UpdateStallion(final String name, boolean clicked_img, boolean clicked_marking, String img ,final String key_horse, final String marking_img) {
        final EditText nameEditText = (EditText) findViewById(R.id.editTextname);
        final EditText breedEdiText = (EditText) findViewById(R.id.editTextUgshil);
        final EditText txtDate = (EditText) findViewById(R.id.editTextOgnoo);
        final EditText priv_num = (EditText) findViewById(R.id.editTextID);
        final EditText blood = (EditText) findViewById(R.id.editTexttsus);
        final EditText color = (EditText) findViewById(R.id.editTextZus);
        final EditText birth_place = (EditText) findViewById(R.id.editTextTGazar);
        final CheckBox active=(CheckBox)findViewById(R.id.checkBox_Stallion);
        final EditText mom=(EditText)findViewById(R.id.editTexteh);
        final EditText dad=(EditText)findViewById(R.id.editTextetseg);
        final String localpath_img;
        final String localpath_markimg;
        final boolean activeVal=active.isChecked();

        img_marking = (ImageButton) findViewById(R.id.imageButton2);
        img_horse = (ImageButton) findViewById(R.id.imageButton);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(AddStallionActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {

            mUserId = mFirebaseUser.getUid();
            String name_stallion = nameEditText.getText().toString().trim();
            String breed = breedEdiText.getText().toString().trim();
            //
            //StorageMetadata metadata=new StorageMetadata.Builder().setContentType("image/*").build();
            //if both imagebuttons are clicked
            if(clicked_img && clicked_marking){

                if (TextUtils.isEmpty(name_stallion) || TextUtils.isEmpty(breed) || selectedImage==null) {
                    Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Угшил' эсвэл 'Зураг' гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.setMessage("Уншиж байна ...");
                    mProgress.show();


                    StorageReference filepath= mStorage.child("users").child(mUserId).child("StallionsImages/"+selectedImage.getLastPathSegment());
                        final StorageReference filepath_marking= mStorage.child("users").child(mUserId).child("StallionMarkingImages/"+selectedMarkinImg.getLastPathSegment());
                        filepath.putFile(selectedImage).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot){
                                @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                System.out.println("Upload is " + progress + "% done");
                            }}).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                                System.out.println("Upload is paused");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                @SuppressWarnings("VisibleForTests") final Uri downloadUrl=taskSnapshot.getDownloadUrl();
                                filepath_marking.putFile(selectedMarkinImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        @SuppressWarnings("VisibleForTests") Uri downloadUrl_marking = taskSnapshot.getDownloadUrl();
                                        String key = mDatabase.child("users").child(mUserId).child("Stallions").push().getKey();
                                        Stallion stallions = new Stallion();
                                        stallions.setStallion_id(key_horse);
                                        stallions.setName_horse(nameEditText.getText().toString());
                                        stallions.setBreed(breedEdiText.getText().toString());
                                        stallions.setPrivate_number(priv_num.getText().toString());
                                        stallions.setBirth_date(txtDate.getText().toString());
                                        stallions.setBirth_place(birth_place.getText().toString());
                                        stallions.setBlood(blood.getText().toString());
                                        stallions.setActivity(activeVal);

                                        stallions.setColor_horse(color.getText().toString());
                                        stallions.setHorse_image(downloadUrl.toString());
                                        stallions.setMother_id(mom.getText().toString());
                                        stallions.setFather_id(dad.getText().toString());
                                        stallions.setMarking_image(downloadUrl_marking.toString());
                                        final Map<String, Object> childupdates = new HashMap<>();
                                        childupdates.put(key, stallions.StalliontoFirebaseObject());
                                        Query applesQuery = mDatabase.child("users").child(mUserId).child("Stallions").orderByChild("stallion_id").equalTo(key_horse);
                                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                    appleSnapshot.getRef().removeValue();
                                                }


                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        mDatabase.child("users").child(mUserId).child("Stallions").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if (databaseError == null) {
                                                    finish();

                                                }
                                            }
                                        });
                                        mProgress.dismiss();
                                        Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddStallionActivity.this, SearchStallion.class);
                                        startActivity(intent);
                                        AddStallionActivity.this.finish();
                                    }
                                });

                            }
                        });


                    }
                    //if image button of img_horse is clicked and marking is not clicked
            }else if(clicked_img && clicked_marking==false){


                if (TextUtils.isEmpty(name_stallion) || TextUtils.isEmpty(breed) || selectedImage==null) {
                    Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Угшил' эсвэл 'Зураг' гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.setMessage("Уншиж байна...");
                    mProgress.show();
                    StorageReference filepath= mStorage.child("users").child(mUserId).child("StallionsImages/"+selectedImage.getLastPathSegment());
                    final String downloadUrl_marking=marking_img;
                    filepath.putFile(selectedImage).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot){
                            @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            System.out.println("Upload is " + progress + "% done");
                        }}).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Upload is paused");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") final Uri downloadUrl=taskSnapshot.getDownloadUrl();

                            String key = mDatabase.child("users").child(mUserId).child("Stallions").push().getKey();
                            Stallion stallions = new Stallion();
                            stallions.setStallion_id(key_horse);
                            stallions.setName_horse(nameEditText.getText().toString());
                            stallions.setBreed(breedEdiText.getText().toString());
                            stallions.setPrivate_number(priv_num.getText().toString());
                            stallions.setBirth_date(txtDate.getText().toString());
                            stallions.setBirth_place(birth_place.getText().toString());
                            stallions.setBlood(blood.getText().toString());
                            stallions.setActivity(activeVal);

                            stallions.setColor_horse(color.getText().toString());
                            stallions.setHorse_image(downloadUrl.toString());
                            stallions.setMother_id(mom.getText().toString());
                            stallions.setFather_id(dad.getText().toString());
                            stallions.setMarking_image(downloadUrl_marking);
                            final Map<String, Object> childupdates = new HashMap<>();
                            childupdates.put(key, stallions.StalliontoFirebaseObject());
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("Stallions").orderByChild("stallion_id").equalTo(key_horse);
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }


                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            mDatabase.child("users").child(mUserId).child("Stallions").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                finish();

                                            }
                                        }
                                    });
                                    mProgress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddStallionActivity.this, SearchStallion.class);
                                    startActivity(intent);
                                    AddStallionActivity.this.finish();
                                }
                            });

                }
                //if image of horse is not clicked and marking is clicked
            }else if(clicked_img==false && clicked_marking){


                if (TextUtils.isEmpty(name_stallion) || TextUtils.isEmpty(breed)) {
                    Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Угшил' гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.setMessage("Уншиж байна...");
                    mProgress.show();
                    StorageReference filepath_marking= mStorage.child("users").child(mUserId).child("StallionsMarkingImages/"+selectedMarkinImg.getLastPathSegment());
                    final String downloadUrl=img;
                    filepath_marking.putFile(selectedMarkinImg).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot){
                            @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            System.out.println("Upload is " + progress + "% done");
                        }}).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Upload is paused");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") final Uri downloadUrl_marking=taskSnapshot.getDownloadUrl();

                            String key = mDatabase.child("users").child(mUserId).child("Stallions").push().getKey();
                            Stallion stallions = new Stallion();
                            stallions.setStallion_id(key_horse);
                            stallions.setName_horse(nameEditText.getText().toString());
                            stallions.setBreed(breedEdiText.getText().toString());
                            stallions.setPrivate_number(priv_num.getText().toString());
                            stallions.setBirth_date(txtDate.getText().toString());
                            stallions.setBirth_place(birth_place.getText().toString());
                            stallions.setBlood(blood.getText().toString());
                            stallions.setActivity(activeVal);
                            stallions.setColor_horse(color.getText().toString());
                            stallions.setHorse_image(downloadUrl);
                            stallions.setMother_id(mom.getText().toString());
                            stallions.setFather_id(dad.getText().toString());
                            stallions.setMarking_image(downloadUrl_marking.toString());
                            final Map<String, Object> childupdates = new HashMap<>();
                            childupdates.put(key, stallions.StalliontoFirebaseObject());
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("Stallions").orderByChild("stallion_id").equalTo(key_horse);
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }


                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            mDatabase.child("users").child(mUserId).child("Stallions").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        finish();

                                    }
                                }
                            });
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddStallionActivity.this, SearchStallion.class);
                            startActivity(intent);
                            AddStallionActivity.this.finish();
                        }
                    });



                    }
                //if marking image and image of horse are both not clicked
            }else{

                if (TextUtils.isEmpty(name_stallion) || TextUtils.isEmpty(breed) ) {
                    Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Угшил' гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.setMessage("Уншиж байна...");
                    mProgress.show();
                    final String downloadUrl=img;
                    final String downloadUrl_marking=marking_img;

                    String key = mDatabase.child("users").child(mUserId).child("Stallions").push().getKey();
                    Stallion stallions = new Stallion();
                    stallions.setStallion_id(key_horse);
                    stallions.setName_horse(nameEditText.getText().toString());
                    stallions.setBreed(breedEdiText.getText().toString());
                    stallions.setPrivate_number(priv_num.getText().toString());
                    stallions.setBirth_date(txtDate.getText().toString());
                    stallions.setBirth_place(birth_place.getText().toString());
                    stallions.setBlood(blood.getText().toString());
                    stallions.setActivity(activeVal);
                    stallions.setColor_horse(color.getText().toString());
                    stallions.setHorse_image(downloadUrl);
                    stallions.setMother_id(mom.getText().toString());
                    stallions.setFather_id(dad.getText().toString());
                    stallions.setMarking_image(downloadUrl_marking);
                    final Map<String, Object> childupdates = new HashMap<>();
                    childupdates.put(key, stallions.StalliontoFirebaseObject());
                    Query applesQuery = mDatabase.child("users").child(mUserId).child("Stallions").orderByChild("stallion_id").equalTo(key_horse);
                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }


                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mDatabase.child("users").child(mUserId).child("Stallions").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                finish();

                            }
                        }
                    });
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "Амжилттай!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddStallionActivity.this, SearchStallion.class);
                    startActivity(intent);
                    AddStallionActivity.this.finish();
                }

                }
            }
    }

    private void saveStallion(){
        //save to Firebase
        final EditText nameEditText=(EditText)findViewById(R.id.editTextname);
        final EditText breedEdiText=(EditText)findViewById(R.id.editTextUgshil);
        final EditText txtDate=(EditText)findViewById(R.id.editTextOgnoo);
        final EditText priv_num=(EditText)findViewById(R.id.editTextID);
        final EditText blood=(EditText)findViewById(R.id.editTexttsus);
        final EditText color=(EditText)findViewById(R.id.editTextZus);
        final CheckBox active=(CheckBox)findViewById(R.id.checkBox_Stallion);
        final boolean activeVal=active.isChecked();
        final EditText mom=(EditText)findViewById(R.id.editTexteh);
        final EditText dad=(EditText)findViewById(R.id.editTextetseg);
        final EditText birth_place=(EditText)findViewById(R.id.editTextTGazar);
        img_marking=(ImageButton)findViewById(R.id.imageButton2);
        img_horse=(ImageButton)findViewById(R.id.imageButton);
        mDatabase=FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddStallionActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            String name_stallion=nameEditText.getText().toString().trim();
            String breed=breedEdiText.getText().toString().trim();
            StorageMetadata metadata=new StorageMetadata.Builder().setContentType("image/*").build();
            if(TextUtils.isEmpty(name_stallion) || TextUtils.isEmpty(breed) || selectedImage==null){
                Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Угшил' эсвэл 'Зураг' гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            }else{
                mProgress.setMessage("Уншиж байна...");
                mProgress.show();

                StorageReference filepath= mStorage.child("users").child(mUserId).child("StallionsImages").child(selectedImage.getLastPathSegment());
                if(selectedMarkinImg==null){
                    final String downloadUrl_marking="No Image";
                    filepath.putFile(selectedImage,metadata).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot){
                            @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            System.out.println("Upload is " + progress + "% done");
                        }}).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Upload is paused");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") final Uri downloadUrl=taskSnapshot.getMetadata().getDownloadUrl();
                                    String key = mDatabase.child("users").child(mUserId).child("Stallions").push().getKey();
                                    Stallion stallions = new Stallion();
                                    stallions.setStallion_id(key);
                                    stallions.setActivity(activeVal);
                                    stallions.setName_horse(nameEditText.getText().toString());
                                    stallions.setBreed(breedEdiText.getText().toString());
                                    stallions.setPrivate_number(priv_num.getText().toString());
                                    stallions.setBirth_date(txtDate.getText().toString());
                                    stallions.setBirth_place(birth_place.getText().toString());
                                    stallions.setBlood(blood.getText().toString());
                                    stallions.setColor_horse(color.getText().toString());
                                    stallions.setHorse_image(downloadUrl.toString());
                                    stallions.setMarking_image(downloadUrl_marking);
                                    stallions.setMother_id(mom.getText().toString());
                                    stallions.setFather_id(dad.getText().toString());

                                    final Map<String, Object> childupdates = new HashMap<>();
                                    childupdates.put(key, stallions.StalliontoFirebaseObject());

                                    mDatabase.child("users").child(mUserId).child("Stallions").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                finish();

                                            }
                                        }
                                    });
                                    mProgress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Амжилттай нэмэгдлээ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddStallionActivity.this, SearchStallion.class);
                                    startActivity(intent);
                                    AddStallionActivity.this.finish();
                                }
                            });


                }else{
                    final StorageReference filepath_marking= mStorage.child("users").child(mUserId).child("StallionMarkingImages").child(selectedMarkinImg.getLastPathSegment());
                    final UploadTask uploadtask=filepath.putFile(selectedImage,metadata);
                    uploadtask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot){
                            @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            System.out.println("Upload is " + progress + "% done");
                        }}).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Upload is paused");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") final Uri downloadUrl=taskSnapshot.getMetadata().getDownloadUrl();
                            filepath_marking.putFile(selectedMarkinImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    @SuppressWarnings("VisibleForTests") Uri downloadUrl_marking = taskSnapshot.getDownloadUrl();
                                    String key = mDatabase.child("users").child(mUserId).child("Stallions").push().getKey();
                                    Stallion stallions = new Stallion();
                                    stallions.setStallion_id(key);
                                    stallions.setActivity(activeVal);
                                    stallions.setName_horse(nameEditText.getText().toString());
                                    stallions.setBreed(breedEdiText.getText().toString());
                                    stallions.setPrivate_number(priv_num.getText().toString());
                                    stallions.setBirth_date(txtDate.getText().toString());
                                    stallions.setBirth_place(birth_place.getText().toString());
                                    stallions.setBlood(blood.getText().toString());
                                    stallions.setColor_horse(color.getText().toString());
                                    stallions.setHorse_image(downloadUrl.toString());
                                    stallions.setMother_id(mom.getText().toString());
                                    stallions.setFather_id(dad.getText().toString());
                                    stallions.setMarking_image(downloadUrl_marking.toString());
                                    final Map<String, Object> childupdates = new HashMap<>();
                                    childupdates.put(key, stallions.StalliontoFirebaseObject());

                                    mDatabase.child("users").child(mUserId).child("Stallions").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                finish();

                                            }
                                        }
                                    });
                                    mProgress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Амжилттай нэмэгдлээ!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddStallionActivity.this, SearchStallion.class);
                                    startActivity(intent);
                                    AddStallionActivity.this.finish();
                                }
                            });

                        }
                    });

                }}




        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==GALLERY_REQUEST && resultCode== Activity.RESULT_OK ){
            selectedImage=data.getData();
            if(selectedImage!=null) {

                    Glide.with(img_horse.getContext()).load(selectedImage).into(img_horse);


            }

        }else if(requestCode==GALLERY_MARKING_REQUEST && resultCode== Activity.RESULT_OK ){
            selectedMarkinImg=data.getData();
            if(selectedMarkinImg!=null) {
                Glide.with(img_marking.getContext()).load(selectedMarkinImg)
                        .into(img_marking);

            }

        }
    }

}
