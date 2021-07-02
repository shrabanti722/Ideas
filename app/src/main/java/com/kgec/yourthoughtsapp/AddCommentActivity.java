package com.kgec.yourthoughtsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCommentActivity extends AppCompatActivity {

    private TextView editcommenttext;
    private Button btnaddcomment;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        Intent i = getIntent();
        final String tid = i.getStringExtra("tid");
        final String name = i.getStringExtra("name");
        final String uid = i.getStringExtra("uid");

        editcommenttext = findViewById(R.id.editcommenttext);
        btnaddcomment = findViewById(R.id.btnaddcomment);
        btnaddcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map= new HashMap<>();
                map.put("commenttext", editcommenttext.getText().toString());
                map.put("cdate", new Timestamp(new java.util.Date()));
                map.put("Name", name);
                map.put("uid", uid);

                firestore.collection("thoughts").document(tid).collection("comments").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddCommentActivity.this, "Your Comment is added!", Toast.LENGTH_SHORT).show();
                        final Comment comment = new Comment(editcommenttext.getText().toString(),name, new java.util.Date(), uid);
                        final DocumentReference thoughtRef = firestore.collection("thoughts").document(tid);
                        thoughtRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        int commentCount = Integer.parseInt(document.getString("commentcount"));
                                        thoughtRef.update("commentcount", String.valueOf(commentCount+1));
                                        Intent i = new Intent();
                                        i.putExtra("NEWCOMMENT", comment);
                                        setResult(Activity.RESULT_OK,i);
                                        finish();
                                    } else {
                                        Log.d("CurrentUser here", "No such document");
                                    }
                                } else {
                                    Log.d("CurrentUser here too", "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCommentActivity.this, "SOMETHING WENT WRONG!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}