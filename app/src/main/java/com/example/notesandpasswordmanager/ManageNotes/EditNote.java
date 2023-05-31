package com.example.notesandpasswordmanager.ManageNotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class EditNote extends AppCompatActivity {
    EditText mtitleofeditnote,mcontentofeditnote;
    FloatingActionButton msaveeditnote;
    Intent data;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        mtitleofeditnote=findViewById(R.id.titleofeditnote);
        mcontentofeditnote=findViewById(R.id.contentofeditnote);
        msaveeditnote=findViewById(R.id.saveeditnote);
        Toolbar toolbar=findViewById(R.id.toolbarofeditnote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        data=getIntent();
        fUser=FirebaseAuth.getInstance().getCurrentUser();
        fStore=FirebaseFirestore.getInstance();
        msaveeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newtitle=mtitleofeditnote.getText().toString();
                String newcontent=mcontentofeditnote.getText().toString();
                if(newtitle.isEmpty()||newcontent.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Field(s) Cannot Be Empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    DocumentReference documentReference=fStore.collection("NOTES").document(fUser.getUid()).collection("MyNotes").document(data.getStringExtra("noteId"));
                    Map<String,Object> note=new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Note Updated",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditNote.this,NotesMain.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed To Update Note",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        String notetitle=data.getStringExtra("title");
        String notecontent=data.getStringExtra("content");
        mtitleofeditnote.setText(notetitle);
        mcontentofeditnote.setText(notecontent);



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