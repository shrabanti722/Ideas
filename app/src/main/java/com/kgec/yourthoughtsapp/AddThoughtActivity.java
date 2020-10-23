package com.kgec.yourthoughtsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddThoughtActivity extends AppCompatActivity {

    TextView tvcurrentdate;
    EditText etthought;
    Button btnadd;


    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thought);

        tvcurrentdate=findViewById(R.id.tvcurrentdate);
        etthought=findViewById(R.id.etenterthought);
        btnadd=findViewById(R.id.btnaddthought);

        Intent ri = getIntent();
        final String uid = ri.getStringExtra("uid");

        ThoughtDetails t=new ThoughtDetails(uid);
        tvcurrentdate.setText(t.getDate().toString());

        Log.i("TRUTH", "AddThought");


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CollectionReference collectionReference=firestore.collection("thoughtlist");
                Log.i("TRUTH", "onCLick");
                Map<String,Object> map=new HashMap<>();
                map.put("thoughttext",etthought.getText().toString());
                map.put("cdate",tvcurrentdate.getText().toString());

                map.put("uid", uid);
                Log.i("TRUTH", "uid : "+uid);
                Log.i("TRUTH", "SC : "+firestore.collection("users").document(uid));
                //map.put("username",)

                firestore.collection("thoughts").add(map)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.i("TRUTH", "onCLick3");
                                Toast.makeText(AddThoughtActivity.this, "Data Added", Toast.LENGTH_LONG).show();
                                Intent ri=new Intent();

                                ThoughtDetails thought=new ThoughtDetails(String.valueOf(new Timestamp(new java.util.Date())),etthought.getText().toString());
                                tvcurrentdate.setText(thought.getDate());
                                //t.setThought(etthought.getText().toString());
                                ri.putExtra("NEWTHOUGHT", thought);
                                setResult(Activity.RESULT_OK,ri);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddThoughtActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                Log.i("TRUTH", "Error : "+e.getMessage());
                            }
                        });
            }
        }); //end of btnadd


    }
}
