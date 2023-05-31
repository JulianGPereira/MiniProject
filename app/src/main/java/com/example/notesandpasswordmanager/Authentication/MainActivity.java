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

import com.example.notesandpasswordmanager.NotesAndPasswordMenu;
import com.example.notesandpasswordmanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText mloginemail, mloginpassword;
    private RelativeLayout mlogin, mgotosignup;
    private TextView mgotoforgotpassword;
    ProgressBar mprogressbarofmainactivity;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mloginemail = findViewById(R.id.loginemail);
        mloginpassword = findViewById(R.id.loginpassword);
        mlogin = findViewById(R.id.login);
        mgotoforgotpassword = findViewById(R.id.gotoforgotpassword);
        mgotosignup = findViewById(R.id.gotosignup);
        mprogressbarofmainactivity = findViewById(R.id.progressbarofmainactivity);
        fAuth=FirebaseAuth.getInstance();
        FirebaseUser fUser=fAuth.getCurrentUser();
        if(fUser!=null) {
            finish();
            startActivity(new Intent(MainActivity.this, NotesAndPasswordMenu.class));
        }
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mloginemail.getText().toString().trim();
                String password=mloginpassword.getText().toString().trim();
                if(email.isEmpty()|| password.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"All Fields Are Required",Toast.LENGTH_SHORT).show();
                }
                else {
                    mprogressbarofmainactivity.setVisibility(View.VISIBLE);
                    fAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            checkmailverfication();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
        mgotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
            }
        });
        mgotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });
    }
    private void checkmailverfication()
    {
        FirebaseUser fUser=fAuth.getCurrentUser();
        if(fUser.isEmailVerified()==true)
        {
            Toast.makeText(getApplicationContext(),"Logged In Successfully",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,NotesAndPasswordMenu.class));
        }
        else
        {
            mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Verify Your Email First",Toast.LENGTH_SHORT).show();
            fAuth.signOut();
        }
    }
}
