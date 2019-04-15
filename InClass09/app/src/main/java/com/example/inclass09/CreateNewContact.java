package com.example.inclass09;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class CreateNewContact extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView photo;
    private Button btnSubmit;
    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewPhone;
    private Bitmap imageBitmap;

    DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference myRef = myRootRef.child("contacts");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageRef = firebaseStorage.getReference();

    ArrayList<Contact> contactLst;
    FirebaseAuth firebaseAuth;

    private Uri filePath;
    StorageReference imageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        contactLst = new ArrayList<>();
        final String id = UUID.randomUUID().toString();

        photo = findViewById(R.id.imageView_ContactPhoto);
        btnSubmit = findViewById(R.id.button_Submit);
        textViewName = findViewById(R.id.editText_Name);
        textViewEmail = findViewById(R.id.editText_Email);
        textViewPhone = findViewById(R.id.editText_PhNum);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Demo", "ImageClick");
                /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);*/
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String contactName = textViewName.getText().toString();
                String email = textViewEmail.getText().toString();
                String phoneNum = textViewPhone.getText().toString();

                if(contactName.length() < 1){
                    textViewName.setError("Please Enter Name");
                }else if(email.length() < 1){
                    textViewEmail.setError("Please Enter Email");
                }else if(phoneNum.length() < 1){
                    textViewPhone.setError("Please Enter Phone Number");
                }else{

                    if(imageBitmap != null){
                        Log.d("Demo", "You Have Image");

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        final byte[] byteData = baos.toByteArray();

                        imageRef = storageRef.child("images/" + imageBitmap.toString());

                        UploadTask uploadTask = imageRef.putBytes(byteData);

                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Log.d("demo", "onFailure: " + byteData);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                Log.d("demo", "onSuccess: " + imageRef.getDownloadUrl());
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        Log.d("demo", "onSuccess: " + uri.toString());
                                        Picasso.get().load(uri.toString()).placeholder(R.drawable.loading).into(photo);
                                    }
                                });
                            }
                        });
                    }

                    final Contact contact = new Contact(textViewName.getText().toString(), textViewEmail.getText().toString(), textViewPhone.getText().toString(), imageRef.toString());
                    Log.d("demo: ",contact.toString());
                    writeNewContact(firebaseAuth.getUid(), textViewName.getText().toString(), textViewEmail.getText().toString(), textViewPhone.getText().toString(), imageRef.toString(), id);

                    myRootRef.child("contacts").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                Contact contact = new Contact();
                                contact.setName(ds.child("name").getValue(String.class));
                                contact.setPhone(ds.child("phone").getValue(String.class));

                                contact.setImageBitmap(ds.child("image").getValue(String.class));

                                //int image = ds.child("imageResId").getValue(Integer.class);
                                //contact.setImageBitmap(imageBitmap);

                                contact.setEmail(ds.child("email").getValue(String.class));
                                contactLst.add(contact);
                            }
                            Intent intent = new Intent();
                            intent.putExtra(ContactListAct.VALUE_KEY, contactLst);
                            startActivity(new Intent(CreateNewContact.this, ContactListAct.class));
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("Demo", "onActivity result");
            Bundle extras = data.getExtras();
            filePath = data.getData();
            imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);
        }
    }

    private void writeNewContact(String userId, String name, String email, String phone, String imageRef, String id) {
        Contact contact = new Contact(name, email, phone, imageRef);
        myRef.child(userId).child(phone).setValue(contact);
    }
}
