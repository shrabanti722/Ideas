package com.kgec.yourthoughtsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentListActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ArrayAdapter<String> aa;
    private CommentAdapter da;
    private ArrayList<Comment> clist = new ArrayList<>();
    private ListView lvcomment;
    private ArrayList<String> list = new ArrayList<>();
    private String name;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        Intent i = getIntent();
        final String tid = i.getStringExtra("tid");
        name = i.getStringExtra("name");
        uid = i.getStringExtra("uid");

        lvcomment = findViewById(R.id.lvcomment);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CommentListActivity.this,AddCommentActivity.class);
                i.putExtra("tid", tid);
                i.putExtra("name", name);
                i.putExtra("uid", uid);
                startActivityForResult(i,400);
                finish();
            }
        });
        firestore.collection("thoughts").document(tid).collection("comments").orderBy("cdate").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(CommentListActivity.this, "Some Error Happened!", Toast.LENGTH_SHORT).show();
                }
                else {
                    clist.clear();
                    List<DocumentSnapshot> documents =  queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot document:documents){
                        String commenttext =  document.getString("commenttext");
                        Date createdate = document.getDate("cdate");
                        String name = document.getString("Name");
                        Comment comment = new Comment(commenttext,name,createdate, uid);
                        clist.add(comment);
                    }
                    da.notifyDataSetChanged();
                }
            }
        });
        da = new CommentAdapter(this,clist);
        lvcomment.setAdapter(da);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent ri = new Intent(CommentListActivity.this, ThoughtActivity.class);
            ri.putExtra("name", name);
            ri.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            setResult(Activity.RESULT_OK,ri);
            startActivity(ri);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==400){
            if(resultCode== Activity.RESULT_OK){
                Comment comment = (Comment) data.getSerializableExtra("NEWCOMMENT");
                clist.add(comment);
                da.notifyDataSetChanged();
                da = new CommentAdapter(this,clist);
                lvcomment.setAdapter(da);
            };
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.my_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            firebaseAuth.signOut();
//            Intent ri = new Intent(CommentListActivity.this, MainActivity.class);
//            ri.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(ri);
//            finish();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
