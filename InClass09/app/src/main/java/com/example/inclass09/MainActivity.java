package com.example.inclass09;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText email;
    private EditText pass;
    private Button btnLogin;
    private Button btnSignUp;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Login");

        email = findViewById(R.id.editText_Email);
        pass = findViewById(R.id.editText_Password);
        btnLogin = findViewById(R.id.button_Login);
        btnSignUp = findViewById(R.id.btn_SignUp);
        firebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( !email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty() ) {

                    Log.d("demo", email.getText().toString() + " " + pass.getText().toString());

                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener() {

                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (!task.isSuccessful()) {
                                        Log.d("demo", "Login failed");
                                        Toast.makeText(MainActivity.this, "Login failed! Invalid email or password,\nPlease try again.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Log.d("demo", "Login failed");
                                        startActivity(new Intent(MainActivity.this, ContactListAct.class));
                                        finish();
                                    }
                                }
                            });
                } else{
                    if (email.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                    } else if (pass.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, UserSignUp.class);
                startActivity(in);
            }
        });

//        if(firebaseAuth.getCurrentUser()!=null){
//            //firebaseAuth.getCurrentUser().getEmail();
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }
    }





    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
