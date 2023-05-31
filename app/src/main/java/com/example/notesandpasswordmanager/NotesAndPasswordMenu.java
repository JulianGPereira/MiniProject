package com.example.notesandpasswordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.notesandpasswordmanager.Authentication.MainActivity;
import com.example.notesandpasswordmanager.ManageNotes.NotesMain;
import com.example.notesandpasswordmanager.ManagePasswords.PasswordMain;
import com.google.firebase.auth.FirebaseAuth;

public class NotesAndPasswordMenu extends AppCompatActivity {
    private RelativeLayout mNotes,mPassword,mLogout;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_and_password_menu);

        mNotes=findViewById(R.id.notes);
        mPassword=findViewById(R.id.password);
        mLogout=findViewById(R.id.menuLogout);

        mNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotesAndPasswordMenu.this, NotesMain.class));
            }
        });

        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotesAndPasswordMenu.this, PasswordMain.class));
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(NotesAndPasswordMenu.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NotesAndPasswordMenu.this, MainActivity.class));
                finish();
            }
        });
    }
}