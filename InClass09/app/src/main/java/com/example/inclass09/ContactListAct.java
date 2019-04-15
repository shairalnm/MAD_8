package com.example.inclass09;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContactListAct extends AppCompatActivity {

    private Button btnCreateNewContact;
    public static final String VALUE_KEY = "value";
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    LinearLayoutManager myLayout;
    ArrayList<Contact> contactLst;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mRef = myRootRef.child("contacts");
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Contacts");
        firebaseAuth = FirebaseAuth.getInstance();

        btnCreateNewContact = findViewById(R.id.button_NewContact);

        myRecyclerView = findViewById(R.id.recyclerID);
        myLayout = new LinearLayoutManager(this);

        myLayout.setOrientation(LinearLayoutManager.VERTICAL);

        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(myLayout);

        contactLst = new ArrayList<>();

        myRootRef.child("contacts").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    contact = new Contact();
                    contact.setName(ds.child("name").getValue(String.class));
                    contact.setPhone(ds.child("phone").getValue(String.class));
                    /*int image = ds.child("imageResId").getValue(Integer.class);
                    contact.setImageResId(image);*/
                    contact.setEmail(ds.child("email").getValue(String.class));
                    contactLst.add(contact);
                }

                myAdapter = new ContactAdapter(contactLst, ContactListAct.this);
                myRecyclerView.setAdapter(myAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        btnCreateNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(ContactListAct.this, CreateNewContact.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 100)
        {
            if(resultCode == RESULT_OK)
            {
                contactLst  = (ArrayList<Contact>) data.getSerializableExtra(VALUE_KEY);
                myAdapter = new ContactAdapter(contactLst, ContactListAct.this);
                myRecyclerView.setAdapter(myAdapter);
            }
            else if(resultCode == RESULT_CANCELED) {

                Log.d("demo","No Value received");
            }
        }
    }
}
