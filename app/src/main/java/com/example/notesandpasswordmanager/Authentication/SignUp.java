package com.example.notesandpasswordmanager.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesandpasswordmanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    private EditText msignupemail,msignuppassword,mconfirmsignuppassword;
    private RelativeLayout msignup;
    private TextView mgotologin;
    private FirebaseAuth fAuth;
    ProgressBar mprogressbarofsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        msignupemail=findViewById(R.id.signupemail);
        msignuppassword=findViewById(R.id.signuppassword);
        mconfirmsignuppassword=findViewById(R.id.confirmsignuppassword);
        msignup=findViewById(R.id.signup);
        mgotologin=findViewById(R.id.gotologin);
        mprogressbarofsignup=findViewById(R.id.progressbarofsignup);
        fAuth=FirebaseAuth.getInstance();
        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = msignupemail.getText().toString().trim();
                String password = msignuppassword.getText().toString().trim();
                String cpassword = mconfirmsignuppassword.getText().toString().trim();
                if (email.isEmpty()) {
                    msignupemail.setError("Email Is Required");
                }
                else if(password.isEmpty()) {
                    msignuppassword.setError("Password Is Required");
                }
                else if (password.length() < 8) {
                    msignuppassword.setError("Password Must Be Atleast 8 Characters");
                } else if (!cpassword.equals(password)) {
                    mconfirmsignuppassword.setError("Passwords Don't Match");
                } else {
                    mprogressbarofsignup.setVisibility(View.VISIBLE);
                    fAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getApplicationContext(),"Registration Successful.",Toast.LENGTH_SHORT).show();
                            sendEmailVerification();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mprogressbarofsignup.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Failed To Register User",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,MainActivity.class));
            }
        });
    }
    private void sendEmailVerification()
    {
        FirebaseUser fUser=fAuth.getCurrentUser();
        if(fUser!=null) {
            fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(),"Verification Email Sent.Verify and Log In Again",Toast.LENGTH_SHORT).show();
                    fAuth.signOut();
                    finish();
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mprogressbarofsignup.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Failed To Send Verification Email",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}