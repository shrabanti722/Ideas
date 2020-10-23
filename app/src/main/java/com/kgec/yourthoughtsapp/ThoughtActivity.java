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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thought);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();

        firestore.setFirestoreSettings(settings);

        Intent ri = getIntent();
        final String uid = ri.getStringExtra("uid");

        lvthoughtdisp=findViewById(R.id.lvthoughtdisp);
        ta=new ThoughtAdapter(this,list);
        lvthoughtdisp.setAdapter(ta);

        //lvthoughtdisp

        /*lvthoughtdisp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ThoughtActivity.this, "Clicked Once : ", Toast.LENGTH_SHORT).show();
                firestore.collection("thoughts").document().collection("comments");
            }
        });*/

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=(new Intent(ThoughtActivity.this,AddThoughtActivity.class));
                Log.i("TRUTH", "uidTA : "+uid);
                i.putExtra("uid", uid);
                startActivityForResult(i,900);
                //startActivity(i);
            }
        }); //end of listener

        Log.i("TRUTH", "Hi.");

        lvthoughtdisp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //ThoughtDetails t=new ThoughtDetails();
                //t.setId((int)id);
                //TextView tvtid=view.findViewById(R.id.thoughtid);
                //String tid=tvtid.getText().toString();
                String tid = "HELLO";
                Toast.makeText(ThoughtActivity.this, "You clicked on "+tid, Toast.LENGTH_SHORT).show();


            }
        });

        //registerForContextMenu(lvthoughtdisp);

        //.document(uid).collection("thoughts").

        firestore.collection("thoughts").orderBy("cdate", Query.Direction.DESCENDING).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot Snapshots, @Nullable FirebaseFirestoreException e) {
                Log.i("TRUTH", "inside first");

                if(e!=null){
                    Log.i("TRUTH", "inside null");
                    Toast.makeText(ThoughtActivity.this, "Some error "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    ThoughtDetails td = new ThoughtDetails();
                    List<DocumentSnapshot> documents= Snapshots.getDocuments();
                    for(DocumentSnapshot document:documents){
                        Log.i("TRUTH", "inside outer");

                        //String uidr = document.getString("uid");
                        //Log.i("TRUTH", "uidr : "+document.getString("uid"));

                        /*DocumentReference docc = firestore.collection("users").document(uidr);

                        docc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        Log.i("TRUTH", "DocumentSnapshot data: " + document.getString("name"));
                                    } else {
                                        Log.d("TRUTH", "No such document");
                                    }
                                } else {
                                    Log.d("TRUTH", "get failed with ", task.getException());
                                }
                            }
                        });*/

                        //td.setName(document.getString("Name"));
                        //td.setPhoto(Integer.parseInt(document.getString("dp")));
                        list.clear();
                        Log.i("TRUTH", "getData : "+document.getData());
                        Log.i("TRUTH", "Name after getData : "+name);
                        String tt = document.getString("thoughttext");
                        String ccdate = document.get("cdate").toString();
                        //Date cdate = (Date)ccdate;
                        ThoughtDetails obj = new ThoughtDetails(name,ccdate,tt);
                        list.add(obj);
                        ta.notifyDataSetChanged();


                        /*firestore.collection("users").addSnapshotListener(ThoughtActivity.this, new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot Snapshots, @Nullable FirebaseFirestoreException e) {

                                List<DocumentSnapshot> documents= Snapshots.getDocuments();
                                for(DocumentSnapshot document:documents) {
                                    Log.i("ERR", "inside inner");
                                    td.setName(document.getString("Name"));
                                    td.setPhoto(Integer.parseInt(document.getString("dp")));
                                    td.setThought(document.getString("thoughttext"));
                                    td.setDate(document.getString("cdate"));
                                    list.add(td);
                                    ta.notifyDataSetChanged();
                                    Log.i("ERR", "lol");


                                }
                            }
                        });*/
                    }



                }

                Log.i("TRUTH", "onEvent");

            }
        });
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
                Log.i("TRUTH", "onActivityeResult");
                ThoughtDetails t=(ThoughtDetails) data.getSerializableExtra("NEWTHOUGHT");
                list.add(t);
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

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
