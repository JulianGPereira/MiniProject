package com.example.notesandpasswordmanager.ManagePasswords;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.notesandpasswordmanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddPassword extends AppCompatActivity {
    EditText mcreatetitleofpassword,mcreatecontentofpassword,memailofaddpassword;
    RelativeLayout mgenerate;
    FloatingActionButton msavepassword;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseFirestore fStore;
    ProgressBar mprogressbarofcreatepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);
        msavepassword = findViewById(R.id.savepassword);
        mcreatetitleofpassword = findViewById(R.id.createtitleofaddpassword);
        memailofaddpassword=findViewById(R.id.emailofaddpassword);
        mcreatecontentofpassword = findViewById(R.id.passwordofaddpassword);
        mgenerate=findViewById(R.id.generate);
        mprogressbarofcreatepassword=findViewById(R.id.progressbarofaddpassword);

        Toolbar toolbar = findViewById(R.id.toolbarofcreatepassword);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        mgenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordGenerator gpsswd =new PasswordGenerator();
                String gpassword=gpsswd.generatePassword(8);
                mcreatecontentofpassword.setText(gpassword);

            }
        });
        msavepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EncryptDecrypt e= new EncryptDecrypt();
                String title=mcreatetitleofpassword.getText().toString().trim();
                String password=mcreatecontentofpassword.getText().toString().trim();
                String email=memailofaddpassword.getText().toString().trim();
                String encrpyttitle=e.encrypt(title);
                String encryptpassword=e.encrypt(password);
                String encryptemail=e.encrypt(email);
                if(title.isEmpty() || password.isEmpty() || email.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"All Fields Are Required",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    mprogressbarofcreatepassword.setVisibility(View.VISIBLE);

                    DocumentReference docRef=fStore.collection("PASSWORDS").document(fUser.getUid()).collection("MyPasswords").document();
                    Map<String ,Object> passwd= new HashMap<>();
                    passwd.put("title",encrpyttitle);
                    passwd.put("email",encryptemail);
                    passwd.put("password",encryptpassword);

                    docRef.set(passwd).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Password Added Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddPassword.this,PasswordMain.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed To Add Password",Toast.LENGTH_SHORT).show();
                            mprogressbarofcreatepassword.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}