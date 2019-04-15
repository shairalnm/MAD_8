package com.example.inclass09;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserSignUp extends AppCompatActivity {

    private Button btnCancel;
    private Button btnSignUp;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText pass;
    private EditText confirmPass;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Sign Up");

        btnCancel = findViewById(R.id.button_Cancel);
        btnSignUp = findViewById(R.id.button_SignUp);
        firstName = findViewById(R.id.editText_FirstName);
        lastName = findViewById(R.id.editText_LastName);
        email = findViewById(R.id.editText_Email);
        pass = findViewById(R.id.editText_Password);
        confirmPass = findViewById(R.id.editText_CnfPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailID = email.getText().toString();
                String password = pass.getText().toString();
                String cnfPass = confirmPass.getText().toString();

                //Validate for empty fields
                if (TextUtils.isEmpty(emailID)) {

                    Toast.makeText(getApplicationContext(), "Please fill in the 'Email' field.", Toast.LENGTH_SHORT).show();
                    return;

                } else if (TextUtils.isEmpty(password)) {

                    Toast.makeText(getApplicationContext(), "Please fill in the 'Password' field.", Toast.LENGTH_SHORT).show();
                    return;

                } else if (TextUtils.isEmpty(cnfPass)) {

                    Toast.makeText(getApplicationContext(), "Please fill in the 'Confirm Password' field.", Toast.LENGTH_SHORT).show();
                    return;

                } else if (pass.length() < 6) {

                    Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;

                } else if (!password.equals(cnfPass)) {

                    Toast.makeText(getApplicationContext(), "Passwords don't match.", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    Log.d("demo", "I am here");

                    firebaseAuth.createUserWithEmailAndPassword(emailID, password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("demo", "tas success");
                                        Toast.makeText(getApplicationContext(), "User Signed Up", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(UserSignUp.this, MainActivity.class);
                                        startActivity(i);

                                    } else {
                                        Log.d("demo", "onComplete: Failed=" + task.getException().getMessage());
                                        Toast.makeText(getApplicationContext(), "E-mail or password is wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
                }
            }
        });

//        if(firebaseAuth.getCurrentUser()!= null){
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }
    }
}
