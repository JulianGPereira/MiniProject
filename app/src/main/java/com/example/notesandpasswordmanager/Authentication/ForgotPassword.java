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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText mforgotpassword;
    private RelativeLayout mpasswordrecoverbutton;
    private TextView mgobacktologin;
    ProgressBar mprogressbarofforgotpassword;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();
        mforgotpassword=findViewById(R.id.forgotpassword);
        mpasswordrecoverbutton=findViewById(R.id.passwordrecoverbutton);
        mgobacktologin=findViewById(R.id.gobacktologin);
        mprogressbarofforgotpassword=findViewById(R.id.progressbarofforgotpassword);
        fAuth=FirebaseAuth.getInstance();
        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mforgotpassword.getText().toString().trim();
                if(email.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Email", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mprogressbarofforgotpassword.setVisibility(View.VISIBLE);
                    fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ForgotPassword.this, "Reset Link Sent Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgotPassword.this, "Error! Reset Link Not Sent "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            mprogressbarofforgotpassword.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });

        mgobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPassword.this,MainActivity.class));
            }
        });
    }
}