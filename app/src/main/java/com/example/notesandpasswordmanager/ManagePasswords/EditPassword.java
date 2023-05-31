package com.example.notesandpasswordmanager.ManagePasswords;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class EditPassword extends AppCompatActivity {

    EditText mtitleofeditpassword,mcontentofeditpassword,memailofeditpassword;
    FloatingActionButton msavepassword;
    RelativeLayout mgenerate;
    Intent data;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        mtitleofeditpassword=findViewById(R.id.createtitleofeditpassword);
        mcontentofeditpassword=findViewById(R.id.passwordofeditpassword);
        memailofeditpassword=findViewById(R.id.emailofeditpassword);
        msavepassword=findViewById(R.id.saveeditpassword);
        mgenerate=findViewById(R.id.generate);
        Toolbar toolbar=findViewById(R.id.toolbarofeditpassword);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        data=getIntent();
        fUser=FirebaseAuth.getInstance().getCurrentUser();
        fStore=FirebaseFirestore.getInstance();
        mgenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordGenerator gpsswd =new PasswordGenerator();
                String gpassword=gpsswd.generatePassword(8);
                mcontentofeditpassword.setText(gpassword);

            }
        });
        msavepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EncryptDecrypt e= new EncryptDecrypt();
                String newtitle=mtitleofeditpassword.getText().toString();
                String newcontent=mcontentofeditpassword.getText().toString();
                String newemail=memailofeditpassword.getText().toString();
                String encryptnewtitle=e.encrypt(newtitle);
                String encryptnewcontent=e.encrypt(newcontent);
                String encryptnewemail=e.encrypt(newemail);
                if(newtitle.isEmpty()||newcontent.isEmpty() || newemail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Field(s) Cannot Be Empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    DocumentReference documentReference=fStore.collection("PASSWORDS").document(fUser.getUid()).collection("MyPasswords").document(data.getStringExtra("passwordId"));
                    Map<String,Object> password=new HashMap<>();
                    password.put("title",encryptnewtitle);
                    password.put("email",encryptnewemail);
                    password.put("password",encryptnewcontent);
                    documentReference.set(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Password Updated",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditPassword.this,PasswordMain.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed To Update Password",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        String passwordtitle=data.getStringExtra("title");
        String passwordemail=data.getStringExtra("email");
        String passwordcontent=data.getStringExtra("password");
        mtitleofeditpassword.setText(passwordtitle);
        memailofeditpassword.setText(passwordemail);
        mcontentofeditpassword.setText(passwordcontent);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}