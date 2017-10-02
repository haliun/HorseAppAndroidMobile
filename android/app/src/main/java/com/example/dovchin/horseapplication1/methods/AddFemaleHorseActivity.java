package com.example.dovchin.horseapplication1.methods;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.models.FemaleHorse;
import com.example.dovchin.horseapplication1.models.MaleHorse;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFemaleHorseActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private static final int GALLERY_REQUEST=1, GALLERY_MARKING_REQUEST=2;
    private Uri selectedImage=null, selectedMarkinImg=null;
    int day, year, month;
    Bitmap bitmap=null;
    ImageButton img_horse, img_marking;
    private ProgressDialog mProgress;
    private FirebaseStorage storage;
    private StorageReference mStorage;
    boolean clicked_img=false;
    boolean clicked_marking=false;

    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_female_horse);

        Button btnDatePicker=(Button)findViewById(R.id.buttonDay_female);
        final EditText txtDate=(EditText)findViewById(R.id.editTextDay_female);
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

                DatePickerDialog dd = new DatePickerDialog(AddFemaleHorseActivity.this,
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
        img_marking=(ImageButton)findViewById(R.id.imageButtonMark_female);
        img_horse=(ImageButton)findViewById(R.id.imageButtonImg_female);
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

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(AddFemaleHorseActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {

            mUserId = mFirebaseUser.getUid();
            mDatabase.child("users").child(mUserId).child("Stallions").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final List<String> stallions = new ArrayList<String>();
                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                        String stallionName = areaSnapshot.child("name_horse").getValue(String.class);
                        stallions.add(stallionName);
                    }
                    Spinner group=(Spinner)findViewById(R.id.spinner_female);
                    ArrayAdapter<String> stallionAdapter = new ArrayAdapter<String>(AddFemaleHorseActivity.this, android.R.layout.simple_spinner_item, stallions);
                    stallionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    group.setPrompt("Сүрэгийн толгой азаргыг сонгоно уу!!");
                    group.setAdapter(stallionAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //Button of saving stallion
        Button save_but=(Button)findViewById(R.id.buttonSave_female);
        save_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveFemaleHorse();

            }
        });

        //updating details of horse
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final FemaleHorse femaleHorse = (FemaleHorse) extras.getSerializable("female_horses");
            if (femaleHorse != null) {
                final EditText nameEditText=(EditText)findViewById(R.id.editTextName_female);
                final EditText breedEdiText=(EditText)findViewById(R.id.editTextBreed_female);
                final EditText textDate=(EditText)findViewById(R.id.editTextDay_female);
                final EditText priv_num=(EditText)findViewById(R.id.editTextId_female);
                final EditText blood=(EditText)findViewById(R.id.editTextBlood_female);
                final EditText color=(EditText)findViewById(R.id.editTextColor_female);
                final EditText birth_place=(EditText)findViewById(R.id.editTextPlace_female);
                img_marking=(ImageButton)findViewById(R.id.imageButtonMark_female);
                img_horse=(ImageButton)findViewById(R.id.imageButtonImg_female);
                final Spinner group=(Spinner)findViewById(R.id.spinner_female);
                final String grouptxt=femaleHorse.getGroup();
                final CheckBox active=(CheckBox)findViewById(R.id.checkBox2);
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser == null) {
                    Intent intent = new Intent(AddFemaleHorseActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {

                    mUserId = mFirebaseUser.getUid();

                    mDatabase.child("users").child(mUserId).child("Stallions").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final List<String> stallions = new ArrayList<String>();
                            stallions.add(grouptxt);
                            for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                String stallionName = areaSnapshot.child("name_horse").getValue(String.class);

                                stallions.add(stallionName);

                            }


                            Spinner group=(Spinner)findViewById(R.id.spinner_female);
                            ArrayAdapter<String> stallionAdapter = new ArrayAdapter<String>(AddFemaleHorseActivity.this, android.R.layout.simple_spinner_item, stallions);
                            stallionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            group.setPrompt("Сүрэгийн толгой азаргыг сонгоно уу!!");
                            group.setAdapter(stallionAdapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                nameEditText.setText(femaleHorse.getName_horse());
                breedEdiText.setText(femaleHorse.getBreed());
                priv_num.setText(femaleHorse.getPrivate_number());
                textDate.setText(femaleHorse.getBirth_date());
                birth_place.setText(femaleHorse.getBirth_place());
                blood.setText(femaleHorse.getBlood());
                color.setText(femaleHorse.getColor_horse());
                group.getSelectedItem();
                active.setChecked(femaleHorse.getActivity());
                Picasso.with(img_horse.getContext()).load(femaleHorse.getHorse_image()).into(img_horse);
                Picasso.with(img_marking.getContext()).load(femaleHorse.getMarking_image()).into(img_marking);
                Button btn_upd=(Button)findViewById(R.id.buttonSave_female);
                btn_upd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name=femaleHorse.getName_horse();
                        String img=femaleHorse.getHorse_image();
                        String img_marking=femaleHorse.getMarking_image();
                        String key_horse=femaleHorse.getFemale_id();
                        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                        UpdateFemaleHorse((String) name,(Boolean) clicked_img, (Boolean) clicked_marking,(String) img, (String) key_horse, (String) img_marking);



                    }
                });

            }
        }


    }
    private void UpdateFemaleHorse(final String name, boolean clicked_img, boolean clicked_marking, String img ,final String key_horse, final String marking_img) {
        final EditText nameEditText=(EditText)findViewById(R.id.editTextName_female);
        final EditText breedEdiText=(EditText)findViewById(R.id.editTextBreed_female);
        final EditText textDate=(EditText)findViewById(R.id.editTextDay_female);
        final EditText priv_num=(EditText)findViewById(R.id.editTextId_female);
        final EditText blood=(EditText)findViewById(R.id.editTextBlood_female);
        final EditText color=(EditText)findViewById(R.id.editTextColor_female);
        final EditText birth_place=(EditText)findViewById(R.id.editTextPlace_female);
        img_marking = (ImageButton) findViewById(R.id.imageButtonMark_female);
        final Spinner group=(Spinner)findViewById(R.id.spinner_female);
        final String selected_group=group.getSelectedItem().toString();
        final CheckBox active=(CheckBox)findViewById(R.id.checkBox2);
        final boolean activeVal=active.isChecked();
        img_horse = (ImageButton) findViewById(R.id.imageButtonImg_female);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(AddFemaleHorseActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {

            mUserId = mFirebaseUser.getUid();
            String name_horse = nameEditText.getText().toString().trim();
            String breed = breedEdiText.getText().toString().trim();
            if(clicked_img && clicked_marking){

                if (TextUtils.isEmpty(name_horse) || TextUtils.isEmpty(breed) || selectedImage==null) {
                    Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Угшил' эсвэл 'Зураг' гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.setMessage("Уншиж байна ...");
                    mProgress.show();


                    StorageReference filepath= mStorage.child("users").child(mUserId).child("FemaleHorseImages/"+selectedImage.getLastPathSegment());
                    final StorageReference filepath_marking= mStorage.child("users").child(mUserId).child("FemaleHorseMarkingImages/"+selectedMarkinImg.getLastPathSegment());
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
                                    String key = mDatabase.child("users").child(mUserId).child("FemaleHorses").push().getKey();
                                    FemaleHorse femaleHorse = new FemaleHorse();
                                    femaleHorse.setFemale_id(key_horse);
                                    femaleHorse.setName_horse(nameEditText.getText().toString());
                                    femaleHorse.setBreed(breedEdiText.getText().toString());
                                    femaleHorse.setPrivate_number(priv_num.getText().toString());
                                    femaleHorse.setBirth_date(textDate.getText().toString());
                                    femaleHorse.setBirth_place(birth_place.getText().toString());
                                    femaleHorse.setBlood(blood.getText().toString());
                                    femaleHorse.setColor_horse(color.getText().toString());
                                    femaleHorse.setHorse_image(downloadUrl.toString());
                                    femaleHorse.setMarking_image(downloadUrl_marking.toString());
                                    femaleHorse.setGroup(selected_group);
                                    femaleHorse.setActivity(activeVal);
                                    final Map<String, Object> childupdates = new HashMap<>();
                                    childupdates.put(key, femaleHorse.FemaleToFirebaseObject());
                                    Query applesQuery = mDatabase.child("users").child(mUserId).child("FemaleHorses").orderByChild("female_id").equalTo(key_horse);
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

                                    mDatabase.child("users").child(mUserId).child("FemaleHorses").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                finish();

                                            }
                                        }
                                    });
                                    mProgress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddFemaleHorseActivity.this, SearchFemaleHorse.class);
                                    startActivity(intent);
                                    AddFemaleHorseActivity.this.finish();
                                }
                            });

                        }
                    });


                }
                //if image button of img_horse is clicked and marking is not clicked
            }else if(clicked_img && clicked_marking==false){


                if (TextUtils.isEmpty(name_horse) || TextUtils.isEmpty(breed) || selectedImage==null) {
                    Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Угшил' эсвэл 'Зураг' гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.setMessage("Уншиж байна...");
                    mProgress.show();
                    StorageReference filepath= mStorage.child("users").child(mUserId).child("FemaleHorseImages/"+selectedImage.getLastPathSegment());
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
                            String key = mDatabase.child("users").child(mUserId).child("FemaleHorses").push().getKey();
                            FemaleHorse femaleHorse = new FemaleHorse();
                            femaleHorse.setFemale_id(key_horse);
                            femaleHorse.setName_horse(nameEditText.getText().toString());
                            femaleHorse.setBreed(breedEdiText.getText().toString());
                            femaleHorse.setPrivate_number(priv_num.getText().toString());
                            femaleHorse.setBirth_date(textDate.getText().toString());
                            femaleHorse.setBirth_place(birth_place.getText().toString());
                            femaleHorse.setBlood(blood.getText().toString());
                            femaleHorse.setColor_horse(color.getText().toString());
                            femaleHorse.setHorse_image(downloadUrl.toString());
                            femaleHorse.setMarking_image(downloadUrl_marking);
                            femaleHorse.setGroup(selected_group);
                            femaleHorse.setActivity(activeVal);
                            final Map<String, Object> childupdates = new HashMap<>();
                            childupdates.put(key, femaleHorse.FemaleToFirebaseObject());
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("FemaleHorses").orderByChild("female_id").equalTo(key_horse);
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

                            mDatabase.child("users").child(mUserId).child("FemaleHorses").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        finish();

                                    }
                                }
                            });
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddFemaleHorseActivity.this, SearchFemaleHorse.class);
                            startActivity(intent);
                            AddFemaleHorseActivity.this.finish();
                        }
                    });

                }
                //if image of horse is not clicked and marking is clicked
            }else if(clicked_img==false && clicked_marking){


                if (TextUtils.isEmpty(name_horse) || TextUtils.isEmpty(breed)) {
                    Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Угшил' гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.setMessage("Уншиж байна...");
                    mProgress.show();
                    StorageReference filepath_marking= mStorage.child("users").child(mUserId).child("FemaleHorseMarkingImages/"+selectedMarkinImg.getLastPathSegment());
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
                            String key = mDatabase.child("users").child(mUserId).child("FemaleHorses").push().getKey();
                            FemaleHorse femaleHorse = new FemaleHorse();
                            femaleHorse.setFemale_id(key_horse);
                            femaleHorse.setName_horse(nameEditText.getText().toString());
                            femaleHorse.setBreed(breedEdiText.getText().toString());
                            femaleHorse.setPrivate_number(priv_num.getText().toString());
                            femaleHorse.setBirth_date(textDate.getText().toString());
                            femaleHorse.setBirth_place(birth_place.getText().toString());
                            femaleHorse.setBlood(blood.getText().toString());
                            femaleHorse.setColor_horse(color.getText().toString());
                            femaleHorse.setHorse_image(downloadUrl);
                            femaleHorse.setMarking_image(downloadUrl_marking.toString());
                            femaleHorse.setGroup(selected_group);
                            femaleHorse.setActivity(activeVal);
                            final Map<String, Object> childupdates = new HashMap<>();
                            childupdates.put(key, femaleHorse.FemaleToFirebaseObject());
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("FemaleHorses").orderByChild("female_id").equalTo(key_horse);
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

                            mDatabase.child("users").child(mUserId).child("FemaleHorses").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        finish();

                                    }
                                }
                            });
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddFemaleHorseActivity.this, SearchFemaleHorse.class);
                            startActivity(intent);
                            AddFemaleHorseActivity.this.finish();
                        }
                    });



                }
                //if marking image and image of horse are both not clicked
            }else{

                if (TextUtils.isEmpty(name_horse) || TextUtils.isEmpty(breed) ) {
                    Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Угшил'  гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.setMessage("Уншиж байна...");
                    mProgress.show();
                    final String downloadUrl=img;
                    final String downloadUrl_marking=marking_img;
                    String key = mDatabase.child("users").child(mUserId).child("FemaleHorses").push().getKey();
                    FemaleHorse femaleHorse = new FemaleHorse();
                    femaleHorse.setFemale_id(key_horse);
                    femaleHorse.setName_horse(nameEditText.getText().toString());
                    femaleHorse.setBreed(breedEdiText.getText().toString());
                    femaleHorse.setPrivate_number(priv_num.getText().toString());
                    femaleHorse.setBirth_date(textDate.getText().toString());
                    femaleHorse.setBirth_place(birth_place.getText().toString());
                    femaleHorse.setBlood(blood.getText().toString());
                    femaleHorse.setColor_horse(color.getText().toString());
                    femaleHorse.setHorse_image(downloadUrl);
                    femaleHorse.setMarking_image(downloadUrl_marking);
                    femaleHorse.setGroup(selected_group);
                    femaleHorse.setActivity(activeVal);
                    final Map<String, Object> childupdates = new HashMap<>();
                    childupdates.put(key, femaleHorse.FemaleToFirebaseObject());
                    Query applesQuery = mDatabase.child("users").child(mUserId).child("FemaleHorses").orderByChild("female_id").equalTo(key_horse);
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

                    mDatabase.child("users").child(mUserId).child("FemaleHorses").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                finish();

                            }
                        }
                    });
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddFemaleHorseActivity.this, SearchFemaleHorse.class);
                    startActivity(intent);
                    AddFemaleHorseActivity.this.finish();
                }

            }
        }
    }

    private void saveFemaleHorse(){
        //save to Firebase
        final EditText nameEditText=(EditText)findViewById(R.id.editTextName_female);
        final EditText breedEdiText=(EditText)findViewById(R.id.editTextBreed_female);
        final EditText textDate=(EditText)findViewById(R.id.editTextDay_female);
        final EditText priv_num=(EditText)findViewById(R.id.editTextId_female);
        final EditText blood=(EditText)findViewById(R.id.editTextBlood_female);
        final EditText color=(EditText)findViewById(R.id.editTextColor_female);
        final EditText birth_place=(EditText)findViewById(R.id.editTextPlace_female);
        final Spinner group=(Spinner)findViewById(R.id.spinner_female);
        final String selected_group=group.getSelectedItem().toString();
        final CheckBox active=(CheckBox)findViewById(R.id.checkBox2);
        final boolean activeVal=active.isChecked();
        img_marking = (ImageButton) findViewById(R.id.imageButtonMark_female);

        mDatabase=FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddFemaleHorseActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            String name_horse=nameEditText.getText().toString().trim();
            String breed=breedEdiText.getText().toString().trim();
            StorageMetadata metadata=new StorageMetadata.Builder().setContentType("image/*").build();
            if(TextUtils.isEmpty(name_horse) || TextUtils.isEmpty(breed) || selectedImage==null){
                Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Угшил' эсвэл 'Зураг' гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            }else{
                mProgress.setMessage("Уншиж байна...");
                mProgress.show();
                StorageReference filepath= mStorage.child("users").child(mUserId).child("FemaleHorseImages").child(selectedImage.getLastPathSegment());
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
                            String key = mDatabase.child("users").child(mUserId).child("FemaleHorses").push().getKey();
                            FemaleHorse femaleHorse = new FemaleHorse();
                            femaleHorse.setFemale_id(key);
                            femaleHorse.setName_horse(nameEditText.getText().toString());
                            femaleHorse.setBreed(breedEdiText.getText().toString());
                            femaleHorse.setPrivate_number(priv_num.getText().toString());
                            femaleHorse.setBirth_date(textDate.getText().toString());
                            femaleHorse.setBirth_place(birth_place.getText().toString());
                            femaleHorse.setBlood(blood.getText().toString());
                            femaleHorse.setActivity(activeVal);
                            femaleHorse.setColor_horse(color.getText().toString());
                            femaleHorse.setHorse_image(downloadUrl.toString());
                            femaleHorse.setMarking_image(downloadUrl_marking);
                            femaleHorse.setGroup(selected_group);
                            final Map<String, Object> childupdates = new HashMap<>();
                            childupdates.put(key, femaleHorse.FemaleToFirebaseObject());

                            mDatabase.child("users").child(mUserId).child("FemaleHorses").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        finish();

                                    }
                                }
                            });
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddFemaleHorseActivity.this, SearchFemaleHorse.class);
                            startActivity(intent);
                            AddFemaleHorseActivity.this.finish();
                        }
                    });


                }else{
                    final StorageReference filepath_marking= mStorage.child("users").child(mUserId).child("FemaleHorseMarkingImages").child(selectedMarkinImg.getLastPathSegment());
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
                                    String key = mDatabase.child("users").child(mUserId).child("FemaleHorses").push().getKey();
                                    FemaleHorse femaleHorse = new FemaleHorse();
                                    femaleHorse.setFemale_id(key);
                                    femaleHorse.setActivity(activeVal);
                                    femaleHorse.setName_horse(nameEditText.getText().toString());
                                    femaleHorse.setBreed(breedEdiText.getText().toString());
                                    femaleHorse.setPrivate_number(priv_num.getText().toString());
                                    femaleHorse.setBirth_date(textDate.getText().toString());
                                    femaleHorse.setBirth_place(birth_place.getText().toString());
                                    femaleHorse.setBlood(blood.getText().toString());
                                    femaleHorse.setColor_horse(color.getText().toString());
                                    femaleHorse.setHorse_image(downloadUrl.toString());
                                    femaleHorse.setMarking_image(downloadUrl_marking.toString());
                                    femaleHorse.setGroup(selected_group);
                                    final Map<String, Object> childupdates = new HashMap<>();
                                    childupdates.put(key, femaleHorse.FemaleToFirebaseObject());

                                    mDatabase.child("users").child(mUserId).child("FemaleHorses").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                finish();

                                            }
                                        }
                                    });
                                    mProgress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Амжилттай нэмэгдлээ!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddFemaleHorseActivity.this, SearchFemaleHorse.class);
                                    startActivity(intent);
                                    AddFemaleHorseActivity.this.finish();
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
            if(null!=selectedImage){
                img_horse.setImageURI(selectedImage);
            }

        }else if(requestCode==GALLERY_MARKING_REQUEST && resultCode== Activity.RESULT_OK ){
            selectedMarkinImg=data.getData();
            if(null!=selectedMarkinImg){
                img_marking.setImageURI(selectedMarkinImg);
            }


        }
    }

}

