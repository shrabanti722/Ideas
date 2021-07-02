package com.kgec.yourthoughtsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etemail, etpass, etname, cnfpass;
    private Button btnregister;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etemail = findViewById(R.id.etemail);
        etpass = findViewById(R.id.etpass);
        etname = findViewById(R.id.etname);
        btnregister = findViewById(R.id.btnregister);
        cnfpass = findViewById(R.id.etcnfpass);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(etpass.getText().toString().equals(cnfpass.getText().toString()))){
                    Toast.makeText(RegisterActivity.this, "PASSWORDS DO NOT MATCH", Toast.LENGTH_SHORT).show();
                    Log.i("TRUTH", "PASSWORDS DO NOT MATCH");
                    etpass.setText("");
                    cnfpass.setText("");
                }
                String email = etemail.getText().toString();
                String password = etpass.getText().toString();
                auth.createUserWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user!=null){
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("Name", etname.getText().toString());
                                    map.put("Email", etemail.getText().toString());

                                    firestore.collection("users").document(user.getUid()).set(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.i("TRUTH", "DATA ADDED");
                                                    Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_LONG).show();
                                                    Intent i = new Intent(RegisterActivity.this, ThoughtActivity.class);
                                                    i.putExtra("uid", user.getUid());
                                                    i.putExtra("name", etname.getText().toString());
                                                    startActivity(i);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.i("TRUTH", "ERROR : "+e.getMessage());
                                                    Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
                        Log.i("WRONG", e.getMessage());
                    }
                });
            }
        });
    }
}
