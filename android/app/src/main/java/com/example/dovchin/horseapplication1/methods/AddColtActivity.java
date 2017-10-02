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
import com.example.dovchin.horseapplication1.models.Colt;
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

public class AddColtActivity extends AppCompatActivity {

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
    private String mUserId;
    boolean clicked_img=false;
    boolean clicked_marking=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_colt);
        Button btnDatePicker=(Button)findViewById(R.id.button28);
        final EditText txtDate=(EditText)findViewById(R.id.editTextColtDate);
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

                DatePickerDialog dd = new DatePickerDialog(AddColtActivity.this,
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
        img_marking=(ImageButton)findViewById(R.id.imageButtonColtMark);
        img_horse=(ImageButton)findViewById(R.id.imageButtonColtImg);
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
        //spinner of listing group

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(AddColtActivity.this, LoginActivity.class);
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
                    Spinner group=(Spinner)findViewById(R.id.spinner_colt);
                    ArrayAdapter<String> stallionAdapter = new ArrayAdapter<String>(AddColtActivity.this, android.R.layout.simple_spinner_item, stallions);
                    stallionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    group.setPrompt("Сүрэгийн толгой азаргыг сонгоно уу!");
                    group.setAdapter(stallionAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //Button of saving stallion
        Button save_but=(Button)findViewById(R.id.buttonSaveColt);
        save_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveColt();

            }
        });

        //updating details of horse
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final Colt colts = (Colt) extras.getSerializable("colts");
            if (colts != null) {
                final EditText nameEditText=(EditText)findViewById(R.id.editTextColtName);
                final EditText breedEdiText=(EditText)findViewById(R.id.editTextColtBreed);
                final EditText textDate=(EditText)findViewById(R.id.editTextColtDate);
                final EditText priv_num=(EditText)findViewById(R.id.editTextColtId);
                final EditText blood=(EditText)findViewById(R.id.editTextColtBlood);
                final EditText color=(EditText)findViewById(R.id.editTextColtColor);
                final EditText birth_place=(EditText)findViewById(R.id.editTextColtPlace);
                final CheckBox active=(CheckBox)findViewById(R.id.checkBox_colt);
                img_marking=(ImageButton)findViewById(R.id.imageButtonColtMark);
                img_horse=(ImageButton)findViewById(R.id.imageButtonColtImg);
                final Spinner group=(Spinner)findViewById(R.id.spinner_colt);

                final String grouptxt=colts.getGroup();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser == null) {
                    Intent intent = new Intent(AddColtActivity.this, LoginActivity.class);
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


                            Spinner group=(Spinner)findViewById(R.id.spinner_colt);
                            ArrayAdapter<String> stallionAdapter = new ArrayAdapter<String>(AddColtActivity.this, android.R.layout.simple_spinner_item, stallions);
                            stallionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            group.setPrompt("Сүрэгийн толгой азаргыг сонгоно уу!");
                            group.setAdapter(stallionAdapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                nameEditText.setText(colts.getName_horse());
                breedEdiText.setText(colts.getBreed());
                priv_num.setText(colts.getPrivate_number());
                textDate.setText(colts.getBirth_date());
                birth_place.setText(colts.getBirth_place());
                blood.setText(colts.getBlood());
                active.setChecked(colts.getActivity());
                color.setText(colts.getColor_horse());
                group.getSelectedItem();
                Picasso.with(img_horse.getContext()).load(colts.getHorse_image()).into(img_horse);
                Picasso.with(img_marking.getContext()).load(colts.getMarking_image()).into(img_marking);
                Button btn_upd=(Button)findViewById(R.id.buttonSaveColt);
                btn_upd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name=colts.getName_horse();
                        String img_marking=colts.getMarking_image();
                        String img=colts.getHorse_image();
                        String key_horse=colts.getColt_id();
                        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                        UpdateColt((String) name,(Boolean) clicked_img, (Boolean) clicked_marking,(String) img, (String) key_horse, (String) img_marking);



                    }
                });

            }
        }


    }
    private void UpdateColt(final String name, boolean clicked_img, boolean clicked_marking, String img ,final String key_horse, final String marking_img) {
        final EditText nameEditText=(EditText)findViewById(R.id.editTextColtName);
        final EditText breedEdiText=(EditText)findViewById(R.id.editTextColtBreed);
        final EditText textDate=(EditText)findViewById(R.id.editTextColtDate);
        final EditText priv_num=(EditText)findViewById(R.id.editTextColtId);
        final EditText blood=(EditText)findViewById(R.id.editTextColtBlood);
        final EditText color=(EditText)findViewById(R.id.editTextColtColor);
        final EditText birth_place=(EditText)findViewById(R.id.editTextColtPlace);
        img_marking=(ImageButton)findViewById(R.id.imageButtonColtMark);
        img_horse=(ImageButton)findViewById(R.id.imageButtonColtImg);
        final Spinner group=(Spinner)findViewById(R.id.spinner_colt);
        final String selected_group=group.getSelectedItem().toString();
        final CheckBox active=(CheckBox)findViewById(R.id.checkBox_colt);
        final boolean activeVal=active.isChecked();
        //img_horse = (ImageButton) findViewById(R.id.imageButtonImg_female);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(AddColtActivity.this, LoginActivity.class);
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


                    StorageReference filepath= mStorage.child("users").child(mUserId).child("ColtImages/"+selectedImage.getLastPathSegment());
                    final StorageReference filepath_marking= mStorage.child("users").child(mUserId).child("ColtMarkingImages/"+selectedMarkinImg.getLastPathSegment());
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
                                    String key = mDatabase.child("users").child(mUserId).child("Colts").push().getKey();
                                    Colt colts = new Colt();
                                    colts.setColt_id(key_horse);
                                    colts.setName_horse(nameEditText.getText().toString());
                                    colts.setBreed(breedEdiText.getText().toString());
                                    colts.setPrivate_number(priv_num.getText().toString());
                                    colts.setBirth_date(textDate.getText().toString());
                                    colts.setBirth_place(birth_place.getText().toString());
                                    colts.setBlood(blood.getText().toString());
                                    colts.setColor_horse(color.getText().toString());
                                    colts.setHorse_image(downloadUrl.toString());
                                    colts.setActivity(activeVal);
                                    colts.setMarking_image(downloadUrl_marking.toString());
                                    colts.setGroup(selected_group);
                                    final Map<String, Object> childupdates = new HashMap<>();
                                    childupdates.put(key, colts.ColtToFirebaseObject());
                                    Query applesQuery = mDatabase.child("users").child(mUserId).child("Colts").orderByChild("colt_id").equalTo(key_horse);
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

                                    mDatabase.child("users").child(mUserId).child("Colts").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                finish();

                                            }
                                        }
                                    });
                                    mProgress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Успешно изменен!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddColtActivity.this, SearchColt.class);
                                    startActivity(intent);
                                    AddColtActivity.this.finish();
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
                    StorageReference filepath= mStorage.child("users").child(mUserId).child("ColtImages/"+selectedImage.getLastPathSegment());
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
                            String key = mDatabase.child("users").child(mUserId).child("Colts").push().getKey();
                            Colt colts = new Colt();
                            colts.setColt_id(key_horse);
                            colts.setName_horse(nameEditText.getText().toString());
                            colts.setBreed(breedEdiText.getText().toString());
                            colts.setPrivate_number(priv_num.getText().toString());
                            colts.setBirth_date(textDate.getText().toString());
                            colts.setBirth_place(birth_place.getText().toString());
                            colts.setBlood(blood.getText().toString());
                            colts.setColor_horse(color.getText().toString());
                            colts.setHorse_image(downloadUrl.toString());
                            colts.setActivity(activeVal);
                            colts.setMarking_image(downloadUrl_marking);
                            colts.setGroup(selected_group);
                            final Map<String, Object> childupdates = new HashMap<>();
                            childupdates.put(key, colts.ColtToFirebaseObject());
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("Colts").orderByChild("colt_id").equalTo(key_horse);
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

                            mDatabase.child("users").child(mUserId).child("Colts").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        finish();

                                    }
                                }
                            });
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Успешно изменен!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddColtActivity.this, SearchColt.class);
                            startActivity(intent);
                            AddColtActivity.this.finish();
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
                    StorageReference filepath_marking= mStorage.child("users").child(mUserId).child("ColtMarkingImages/"+selectedMarkinImg.getLastPathSegment());
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
                            String key = mDatabase.child("users").child(mUserId).child("Colts").push().getKey();
                            Colt colts = new Colt();
                            colts.setColt_id(key_horse);
                            colts.setName_horse(nameEditText.getText().toString());
                            colts.setBreed(breedEdiText.getText().toString());
                            colts.setPrivate_number(priv_num.getText().toString());
                            colts.setBirth_date(textDate.getText().toString());
                            colts.setBirth_place(birth_place.getText().toString());
                            colts.setBlood(blood.getText().toString());
                            colts.setColor_horse(color.getText().toString());
                            colts.setHorse_image(downloadUrl);
                            colts.setActivity(activeVal);
                            colts.setMarking_image(downloadUrl_marking.toString());
                            colts.setGroup(selected_group);
                            final Map<String, Object> childupdates = new HashMap<>();
                            childupdates.put(key, colts.ColtToFirebaseObject());
                            Query applesQuery = mDatabase.child("users").child(mUserId).child("Colts").orderByChild("colt_id").equalTo(key_horse);
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

                            mDatabase.child("users").child(mUserId).child("Colts").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        finish();

                                    }
                                }
                            });
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Успешно изменен!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddColtActivity.this, SearchColt.class);
                            startActivity(intent);
                            AddColtActivity.this.finish();
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
                    String key = mDatabase.child("users").child(mUserId).child("Colts").push().getKey();
                    Colt colts = new Colt();
                    colts.setColt_id(key_horse);
                    colts.setName_horse(nameEditText.getText().toString());
                    colts.setBreed(breedEdiText.getText().toString());
                    colts.setPrivate_number(priv_num.getText().toString());
                    colts.setBirth_date(textDate.getText().toString());
                    colts.setBirth_place(birth_place.getText().toString());
                    colts.setBlood(blood.getText().toString());
                    colts.setColor_horse(color.getText().toString());
                    colts.setHorse_image(downloadUrl);
                    colts.setActivity(activeVal);
                    colts.setMarking_image(downloadUrl_marking);
                    colts.setGroup(selected_group);
                    final Map<String, Object> childupdates = new HashMap<>();
                    childupdates.put(key, colts.ColtToFirebaseObject());
                    Query applesQuery = mDatabase.child("users").child(mUserId).child("Colts").orderByChild("colt_id").equalTo(key_horse);
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

                    mDatabase.child("users").child(mUserId).child("Colts").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                finish();

                            }
                        }
                    });
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "Успешно изменен!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddColtActivity.this, SearchColt.class);
                    startActivity(intent);
                    AddColtActivity.this.finish();
                }

            }
        }
    }



    private void saveColt(){
        //save to Firebase
        final EditText nameEditText=(EditText)findViewById(R.id.editTextColtName);
        final EditText breedEdiText=(EditText)findViewById(R.id.editTextColtBreed);
        final EditText textDate=(EditText)findViewById(R.id.editTextColtDate);
        final EditText priv_num=(EditText)findViewById(R.id.editTextColtId);
        final EditText blood=(EditText)findViewById(R.id.editTextColtBlood);
        final EditText color=(EditText)findViewById(R.id.editTextColtColor);
        final EditText birth_place=(EditText)findViewById(R.id.editTextColtPlace);
        final CheckBox active=(CheckBox)findViewById(R.id.checkBox_colt);
        final boolean activeVal=active.isChecked();
        img_marking=(ImageButton)findViewById(R.id.imageButtonColtMark);
        img_horse=(ImageButton)findViewById(R.id.imageButtonColtImg);
        final Spinner group=(Spinner)findViewById(R.id.spinner_colt);
        final String selected_group=group.getSelectedItem().toString();
        //img_marking = (ImageButton) findViewById(R.id.imageButtonMark_female);

        mDatabase=FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddColtActivity.this, LoginActivity.class);
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
                StorageReference filepath= mStorage.child("users").child(mUserId).child("ColtImages").child(selectedImage.getLastPathSegment());
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
                            String key = mDatabase.child("users").child(mUserId).child("Colts").push().getKey();
                            Colt colt = new Colt();
                            colt.setColt_id(key);
                            colt.setName_horse(nameEditText.getText().toString());
                            colt.setBreed(breedEdiText.getText().toString());
                            colt.setPrivate_number(priv_num.getText().toString());
                            colt.setBirth_date(textDate.getText().toString());
                            colt.setBirth_place(birth_place.getText().toString());
                            colt.setBlood(blood.getText().toString());
                            colt.setColor_horse(color.getText().toString());
                            colt.setHorse_image(downloadUrl.toString());
                            colt.setMarking_image(downloadUrl_marking);
                            colt.setActivity(activeVal);
                            colt.setGroup(selected_group);
                            final Map<String, Object> childupdates = new HashMap<>();
                            childupdates.put(key, colt.ColtToFirebaseObject());

                            mDatabase.child("users").child(mUserId).child("Colts").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        finish();

                                    }
                                }
                            });
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Амжилттай нэмэгдлээ!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddColtActivity.this, SearchColt.class);
                            startActivity(intent);
                            AddColtActivity.this.finish();
                        }
                    });


                }else{
                    final StorageReference filepath_marking= mStorage.child("users").child(mUserId).child("ColtMarkingImgs").child(selectedMarkinImg.getLastPathSegment());
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
                                    String key = mDatabase.child("users").child(mUserId).child("Colts").push().getKey();
                                    Colt colt = new Colt();
                                    colt.setColt_id(key);
                                    colt.setName_horse(nameEditText.getText().toString());
                                    colt.setBreed(breedEdiText.getText().toString());
                                    colt.setPrivate_number(priv_num.getText().toString());
                                    colt.setBirth_date(textDate.getText().toString());
                                    colt.setBirth_place(birth_place.getText().toString());
                                    colt.setBlood(blood.getText().toString());
                                    colt.setActivity(activeVal);
                                    colt.setColor_horse(color.getText().toString());
                                    colt.setHorse_image(downloadUrl.toString());
                                    colt.setMarking_image(downloadUrl_marking.toString());
                                    colt.setGroup(selected_group);
                                    final Map<String, Object> childupdates = new HashMap<>();
                                    childupdates.put(key, colt.ColtToFirebaseObject());

                                    mDatabase.child("users").child(mUserId).child("Colts").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                finish();

                                            }
                                        }
                                    });
                                    mProgress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Амжилттай нэмэгдлээ!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddColtActivity.this, SearchColt.class);
                                    startActivity(intent);
                                    AddColtActivity.this.finish();
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

