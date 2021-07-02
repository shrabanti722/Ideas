package com.kgec.yourthoughtsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kgec.yourthoughtsapp.R;
import com.kgec.yourthoughtsapp.ThoughtAdapter;
import com.kgec.yourthoughtsapp.ThoughtDetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThoughtActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private ListView lvthoughtdisp;

    private ArrayList<ThoughtDetails> list=new ArrayList<>();
    private ThoughtAdapter ta=null;

    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    private String name;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thought);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent ri = getIntent();
        uid = ri.getStringExtra("uid");
        final String name = ri.getStringExtra("name");

        lvthoughtdisp=findViewById(R.id.lvthoughtdisp);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=(new Intent(ThoughtActivity.this,AddThoughtActivity.class));
                i.putExtra("uid", uid);
                i.putExtra("name", name);
                startActivityForResult(i,900);
            }
        });

        firestore.collection("thoughts").orderBy("cdate", Query.Direction.DESCENDING).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot Snapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(ThoughtActivity.this, "Some error "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    list.clear();
                    List<DocumentSnapshot> documents= Snapshots.getDocuments();
                    for(DocumentSnapshot document:documents){
                        String tt = document.getString("thoughttext");
                        Date ccdate = document.getDate("cdate");
                        String username = document.get("username").toString();
                        int commentCount = Integer.parseInt(document.getString("commentcount"));
                        ThoughtDetails obj = new ThoughtDetails(username,ccdate,tt, commentCount);
                        list.add(obj);
                        ta.notifyDataSetChanged();
                    }
                }
                Log.i("TRUTH", "onEvent");
            }
        });
        ta=new ThoughtAdapter(this,list, name, uid);
        lvthoughtdisp.setAdapter(ta);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        firebaseAuth.signOut();

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==900){
            if(resultCode== Activity.RESULT_OK){
                ThoughtDetails t=(ThoughtDetails) data.getSerializableExtra("NEWTHOUGHT");
                String username = data.getStringExtra("name");
                list.add(t);
                firestore.collection("thoughts").orderBy("cdate", Query.Direction.DESCENDING).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot Snapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            Toast.makeText(ThoughtActivity.this, "Some error "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        else{
                            list.clear();
                            List<DocumentSnapshot> documents= Snapshots.getDocuments();
                            for(DocumentSnapshot document:documents){
                                String tt = document.getString("thoughttext");
                                Date ccdate = document.getDate("cdate");
                                String username = document.get("username").toString();
                                int commentCount = Integer.parseInt(document.getString("commentcount"));
                                ThoughtDetails obj = new ThoughtDetails(username,ccdate,tt, commentCount);
                                list.add(obj);
                                ta.notifyDataSetChanged();
                            }
                        }
                        Log.i("TRUTH", "onEvent");
                    }
                });
                ta=new ThoughtAdapter(this,list, username, uid);
                lvthoughtdisp.setAdapter(ta);
                ta.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            firebaseAuth.signOut();
            Intent ri = new Intent(ThoughtActivity.this, MainActivity.class);
            ri.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ri);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
