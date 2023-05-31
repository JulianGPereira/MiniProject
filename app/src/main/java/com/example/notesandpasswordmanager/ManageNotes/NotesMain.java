package com.example.notesandpasswordmanager.ManageNotes;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.notesandpasswordmanager.Authentication.MainActivity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesMain extends AppCompatActivity {
    FloatingActionButton maddnotefab;
    FirebaseAuth fAuth;
    RecyclerView mrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser fUser;
    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter<NoteModel,NoteViewHolder> noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);

        maddnotefab=findViewById(R.id.addnotefab);
        fAuth=FirebaseAuth.getInstance();
        fUser=FirebaseAuth.getInstance().getCurrentUser();
        fStore=FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("Notes Manager");

        maddnotefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotesMain.this,AddNote.class));
            }
        });
        Query query=fStore.collection("NOTES").document(fUser.getUid()).collection("MyNotes").orderBy("title",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<NoteModel> allnotes= new FirestoreRecyclerOptions.Builder<NoteModel>().setQuery(query,NoteModel.class).build();
        noteAdapter=new FirestoreRecyclerAdapter<NoteModel, NoteViewHolder>(allnotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull NoteModel model) {
                int colourcode=getRandomColor();
                holder.mnote.setBackgroundColor(holder.itemView.getResources().getColor(colourcode,null));
                holder.notetitle.setText(model.getTitle());
                holder.notecontent.setText(model.getContent());
                String docId=noteAdapter.getSnapshots().getSnapshot(position).getId();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(view.getContext(),NoteDetails.class);
                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("content",model.getContent());
                        intent.putExtra("noteId",docId);
                        view.getContext().startActivity(intent);
                    }
                });
                ImageView popupbutton=holder.itemView.findViewById(R.id.menupopbutton);
                popupbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu=new PopupMenu(view.getContext(),view);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Intent intent=new Intent(view.getContext(),EditNote.class);
                                intent.putExtra("title",model.getTitle());
                                intent.putExtra("content",model.getContent());
                                intent.putExtra("noteId",docId);
                                view.getContext().startActivity(intent);
                                return false;
                            }
                        });
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                DocumentReference docRef=fStore.collection("NOTES").document(fUser.getUid()).collection("MyNotes").document(docId);
                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(view.getContext(),"Note Deleted",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(view.getContext(),"Failed To Delete Note",Toast.LENGTH_SHORT).show();
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
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };
        mrecyclerview=findViewById(R.id.recyclerview);
        mrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerview.setAdapter(noteAdapter);
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder
    {
        private TextView notetitle;
        private TextView notecontent;
        LinearLayout mnote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle=itemView.findViewById(R.id.notetitle);
            notecontent=itemView.findViewById(R.id.notecontent);
            mnote=itemView.findViewById(R.id.note);
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
                startActivity(new Intent(NotesMain.this, MainActivity.class));
                break;

            case R.id.home:
                startActivity(new Intent(NotesMain.this, NotesAndPasswordMenu.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null)
        {
            noteAdapter.stopListening();
        }
    }
    public int getRandomColor()
    {
        List<Integer> colorcode=new ArrayList<>();
        colorcode.add(R.color.purple_200);
        colorcode.add(R.color.teal_200);
        colorcode.add(R.color.white);
        colorcode.add(R.color.yellow);
        colorcode.add(R.color.lightGreen);
        colorcode.add(R.color.pink);
        colorcode.add(R.color.lightPurple);
        colorcode.add(R.color.skyblue);
        colorcode.add(R.color.gray);
        colorcode.add(R.color.red);
        colorcode.add(R.color.blue);
        colorcode.add(R.color.greenlight);
        Random random=new Random();
        int number=random.nextInt(colorcode.size());
        return colorcode.get(number);
    }
}