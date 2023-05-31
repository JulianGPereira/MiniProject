package com.example.notesandpasswordmanager.ManagePasswords;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.example.notesandpasswordmanager.Authentication.MainActivity;
import com.example.notesandpasswordmanager.ManageNotes.NotesMain;
import com.example.notesandpasswordmanager.NotesAndPasswordMenu;
import com.example.notesandpasswordmanager.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PasswordMain extends AppCompatActivity {
    FloatingActionButton maddpasswordfab;
    FirebaseAuth fAuth;
    RecyclerView mrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser fUser;
    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter<PasswordModel,PasswordViewHolder> passwordAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_main);

        maddpasswordfab=findViewById(R.id.addpasswordfab);
        fAuth=FirebaseAuth.getInstance();
        fUser=FirebaseAuth.getInstance().getCurrentUser();
        fStore=FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("Password Manager");

        maddpasswordfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PasswordMain.this,AddPassword.class));
            }
        });
        Query query=fStore.collection("PASSWORDS").document(fUser.getUid()).collection("MyPasswords").orderBy("title",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<PasswordModel> allpasswords= new FirestoreRecyclerOptions.Builder<PasswordModel>().setQuery(query,PasswordModel.class).build();
        passwordAdapter=new FirestoreRecyclerAdapter<PasswordModel, PasswordViewHolder>(allpasswords) {
            @Override
            protected void onBindViewHolder(@NonNull PasswordViewHolder holder, int position, @NonNull PasswordModel model) {
                ImageView popupbutton,ecopybutton,pcopybutton;
                popupbutton=holder.itemView.findViewById(R.id.menupopbutton);
                ecopybutton=holder.itemView.findViewById(R.id.copybutton1);
                pcopybutton=holder.itemView.findViewById(R.id.copybutton2);
                holder.passwordtitle.setText(model.getTitle());
                holder.passwordemail.setText(model.getEmail());
                holder.passwordcontent.setText(model.getPassword());
                String docId=passwordAdapter.getSnapshots().getSnapshot(position).getId();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(view.getContext(),EditPassword.class);
                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("password",model.getPassword());
                        intent.putExtra("email",model.getEmail());
                        intent.putExtra("passwordId",docId);
                        view.getContext().startActivity(intent);
                    }
                });
                ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ecopybutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipData clipData = ClipData.newPlainText("text", model.getEmail());
                        Toast.makeText(getApplicationContext(),"Email Copied",Toast.LENGTH_SHORT).show();
                        manager.setPrimaryClip(clipData);
                    }
                });
                pcopybutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipData clipData=ClipData.newPlainText("password",model.getPassword());
                        Toast.makeText(getApplicationContext(),"Password Copied",Toast.LENGTH_SHORT).show();
                        manager.setPrimaryClip(clipData);
                    }
                });
                popupbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu=new PopupMenu(view.getContext(),view);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                DocumentReference docRef=fStore.collection("PASSWORDS").document(fUser.getUid()).collection("MyPasswords").document(docId);
                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(view.getContext(),"Password Deleted",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(view.getContext(),"Failed To Delete Password",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }
            @NonNull
            @Override
            public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.password_layout,parent,false);
                return new PasswordViewHolder(view);
            }
        };
        mrecyclerview=findViewById(R.id.recyclerview);
        mrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerview.setAdapter(passwordAdapter);
    }
    public class PasswordViewHolder extends RecyclerView.ViewHolder
    {
        private TextView passwordtitle;
        private TextView passwordcontent;
        private TextView passwordemail;
        LinearLayout mpassword;

        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            passwordtitle=itemView.findViewById(R.id.passwordtitle);
            passwordcontent=itemView.findViewById(R.id.passwordcontent);
            passwordemail=itemView.findViewById(R.id.emailcontent);
            mpassword=itemView.findViewById(R.id.password);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                fAuth.signOut();
                finish();
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PasswordMain.this,MainActivity.class));
                break;

            case R.id.home:
                startActivity(new Intent(PasswordMain.this, NotesAndPasswordMenu.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        passwordAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(passwordAdapter!=null)
        {
            passwordAdapter.stopListening();
        }
    }
}