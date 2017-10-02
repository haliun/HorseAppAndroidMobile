package com.example.dovchin.horseapplication1.methods;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dovchin.horseapplication1.Activities.MainActivity;
import com.example.dovchin.horseapplication1.Activities.UserProfileActivity;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.models.User;
import com.github.clans.fab.FloatingActionButton;
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

import java.util.HashMap;
import java.util.Map;

public class AddUserDatas extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private static final int GALLERY_REQUEST=1;
    private Uri selectedImage=null;
    int day, year, month;
    Bitmap bitmap=null;
    ImageButton img_horse, img_marking;
    private ProgressDialog mProgress;
    private FirebaseStorage storage;
    private StorageReference mStorage;
    private String mUserId;
    private ImageView circularImageView;
    boolean clicked_img=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_datas);
        FirebaseUser userDB = FirebaseAuth.getInstance().getCurrentUser();
        final EditText emailfield=(EditText)findViewById(R.id.editText3);

        String email=userDB.getEmail();
        emailfield.setText(email);
        mStorage= FirebaseStorage.getInstance().getReference();
        mProgress=new ProgressDialog(this);
        circularImageView = (ImageView) findViewById(R.id.imageViewUser);
        final FloatingActionButton upload=(FloatingActionButton)findViewById(R.id.fab_user_image);
        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
        Button save_but=(Button)findViewById(R.id.button_save_user);
        save_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Save_user();

            }
        });
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            final User users = (User) extras.getSerializable("users");
            if (users != null) {
                final EditText nameEditText = (EditText) findViewById(R.id.editText2);
                final EditText surname = (EditText) findViewById(R.id.editText);
                final EditText city = (EditText) findViewById(R.id.editText4);
                final EditText sum = (EditText) findViewById(R.id.editText5);
                final EditText emailfield1 = (EditText) findViewById(R.id.editText3);
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                                if(upload.isPressed()) {
                                    clicked_img=true;
                                }else{
                                    clicked_img=false;
                                }
                                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                                galleryIntent.setType("image/*");
                                startActivityForResult(galleryIntent,GALLERY_REQUEST);

                            }
                        });

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                nameEditText.setText(users.getFirst_name());
                surname.setText(users.getLast_name());
                emailfield1.setText(users.getEmail());
                city.setText(users.getCity());
                sum.setText(users.getDegree());
                circularImageView = (ImageView) findViewById(R.id.imageViewUser);
                Glide.with(getApplicationContext()).load(users.getProfile_img()).transform(new CircleTransform(AddUserDatas.this)).into(circularImageView);

                Button edit=(Button)findViewById(R.id.button_save_user);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String img=users.getProfile_img();
                        String key_user=users.getUserkey();
                        update_user((String) key_user,(String) img,(Boolean) clicked_img);

                    }
                });

            }
        }
    }
    private void update_user(final String key_user, String img, Boolean clicked_img){
        final EditText nameEditText=(EditText)findViewById(R.id.editText2);
        final EditText surname=(EditText)findViewById(R.id.editText);
        final EditText city=(EditText)findViewById(R.id.editText4);
        final EditText sum=(EditText)findViewById(R.id.editText5);
        final EditText email=(EditText)findViewById(R.id.editText3);
        circularImageView = (ImageView) findViewById(R.id.imageViewUser);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(AddUserDatas.this, LoginActivity.class);
            startActivity(intent);
        } else {

            mUserId = mFirebaseUser.getUid();
            String name = nameEditText.getText().toString().trim();
            //
            //StorageMetadata metadata=new StorageMetadata.Builder().setContentType("image/*").build();
            //if both imagebuttons are clicked
            if(clicked_img) {
                StorageMetadata metadata=new StorageMetadata.Builder().setContentType("image/*").build();
                if(TextUtils.isEmpty(name)  || selectedImage==null){
                    Toast.makeText(getApplicationContext(), "'Нэр' эсвэл  'Зураг' гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
                }else{
                    mProgress.setMessage("Уншиж байна...");
                    mProgress.show();
                    StorageReference filepath= mStorage.child("users").child(mUserId).child("UserData").child(selectedImage.getLastPathSegment());
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
                            String key = mDatabase.child("users").child(mUserId).getKey();

                            User user=new User();
                            user.setUserkey(key_user);
                            user.setFirst_name(nameEditText.getText().toString());
                            user.setLast_name(surname.getText().toString());
                            user.setCity(city.getText().toString());
                            user.setDegree(sum.getText().toString());
                            user.setEmail(email.getText().toString());
                            user.setProfile_img(downloadUrl.toString());
                            final Map<String, Object> childupdates = new HashMap<>();
                            childupdates.put(key, user.UserToFirebase());

                            mDatabase.child("users").child(mUserId).child("UserData").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        finish();

                                    }
                                }
                            });
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddUserDatas.this, UserProfileActivity.class);
                            startActivity(intent);
                            AddUserDatas.this.finish();
                        }
                    });

                }
            }else{
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(), "'Нэр'  гэсэн талбарыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
                }else{
                    mProgress.setMessage("Уншиж байна...");
                    mProgress.show();
                    final String downloadUrl=img;
                    String key = mDatabase.child("users").child(mUserId).getKey();

                    User user=new User();
                    user.setUserkey(key_user);
                    user.setFirst_name(nameEditText.getText().toString());
                    user.setLast_name(surname.getText().toString());
                    user.setCity(city.getText().toString());
                    user.setDegree(sum.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setProfile_img(downloadUrl);
                    final Map<String, Object> childupdates = new HashMap<>();
                    childupdates.put(key, user.UserToFirebase());
                    Query applesQuery = mDatabase.child("users").child(mUserId).child("UserData").orderByChild("id").equalTo(key_user);
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
                    mDatabase.child("users").child(mUserId).child("UserData").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                finish();

                            }
                        }
                    });
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "Амжилттай өөрчлөгдлөө!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddUserDatas.this, UserProfileActivity.class);
                    startActivity(intent);
                    AddUserDatas.this.finish();
                }

                }




        }


    }
    private void Save_user(){
        //save to Firebase
        final EditText nameEditText=(EditText)findViewById(R.id.editText2);
        final EditText surname=(EditText)findViewById(R.id.editText);
        final EditText city=(EditText)findViewById(R.id.editText4);
        final EditText sum=(EditText)findViewById(R.id.editText5);
        final EditText email=(EditText)findViewById(R.id.editText3);
        circularImageView = (ImageView) findViewById(R.id.imageViewUser);
        //img_marking = (ImageButton) findViewById(R.id.imageButtonMark_female);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(AddUserDatas.this, LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            String name=nameEditText.getText().toString().trim();
            StorageMetadata metadata=new StorageMetadata.Builder().setContentType("image/*").build();
            if(TextUtils.isEmpty(name)  || selectedImage==null){
                Toast.makeText(getApplicationContext(), "'Нэр' эсвэл 'Зураг' гэсэн талбаруудыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
            }else{
                mProgress.setMessage("Уншиж байна...");
                mProgress.show();
                StorageReference filepath= mStorage.child("users").child(mUserId).child("UserData").child(selectedImage.getLastPathSegment());
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
                        String key = mDatabase.child("users").child(mUserId).getKey();

                        User user=new User();
                        user.setUserkey(key);
                        user.setFirst_name(nameEditText.getText().toString());
                        user.setLast_name(surname.getText().toString());
                        user.setCity(city.getText().toString());
                        user.setDegree(sum.getText().toString());
                        user.setEmail(email.getText().toString());
                        user.setProfile_img(downloadUrl.toString());
                        final Map<String, Object> childupdates = new HashMap<>();
                        childupdates.put(key, user.UserToFirebase());

                        mDatabase.child("users").child(mUserId).child("UserData").updateChildren(childupdates, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    finish();

                                }
                            }
                        });
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(), "Амжилттай нэвтэрлээ!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddUserDatas.this, MainActivity.class);
                        startActivity(intent);
                        AddUserDatas.this.finish();
                            }
                        });

                    }
                }

            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==GALLERY_REQUEST && resultCode== Activity.RESULT_OK ){
            selectedImage=data.getData();
            if(null!=selectedImage){
                String image_url=selectedImage.toString();
                Glide.with(getApplicationContext()).load(selectedImage).transform(new CircleTransform(AddUserDatas.this)).into(circularImageView);
            }




        }
    }


}
