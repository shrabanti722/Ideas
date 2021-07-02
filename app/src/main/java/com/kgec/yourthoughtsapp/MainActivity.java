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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.auth.User;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText edtEmail, edtPassword;
    private Button btnSignUp, btnlogin;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            String email = currentUser.getEmail();
            DocumentReference docRef = db.collection("users").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("CurrentUser", "DocumentSnapshot data: " + document.getData().get("Name"));
                            Intent i = new Intent(MainActivity.this, ThoughtActivity.class);
                            i.putExtra("uid", currentUser.getUid());
                            i.putExtra("name", document.getData().get("Name").toString());
                            startActivity(i);
                        } else {
                            Log.d("CurrentUser", "No such document");
                        }
                    } else {
                        Log.d("CurrentUser", "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignUp = findViewById(R.id.btnSignup);
        btnlogin = findViewById(R.id.btnlogin);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signUp();

            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });

    }

    private void signIn() {

        mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.i("TRUTH", "Signed In");
                        final FirebaseUser currentUser = authResult.getUser();
                            if(currentUser!=null){
                                String email = currentUser.getEmail();
                                DocumentReference docRef = db.collection("users").document(currentUser.getUid());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d("CurrentUser", "DocumentSnapshot data: " + document.getData().get("Name"));
                                                Intent i = new Intent(MainActivity.this, ThoughtActivity.class);
                                                i.putExtra("uid", currentUser.getUid());
                                                i.putExtra("name", document.getData().get("Name").toString());
                                                startActivity(i);
                                                Toast.makeText(MainActivity.this, "Signed In Successfully!", Toast.LENGTH_LONG).show();
                                            } else {
                                                Log.d("CurrentUser", "No such document");
                                            }
                                        } else {
                                            Log.d("CurrentUser", "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.i("TRUTH", "Signed In Cancelled");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("TRUTH", "Error");
                        Toast.makeText(MainActivity.this, "Email/Password Incorrect.", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void signUp() {
            startActivityForResult(new Intent(MainActivity.this, RegisterActivity.class),100);
    }
}
