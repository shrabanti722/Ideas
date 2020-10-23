package com.kgec.yourthoughtsapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ThoughtAdapter extends ArrayAdapter<ThoughtDetails> {

    private Context context;
    private ArrayList<ThoughtDetails> list;
    CheckBox cb;

    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    public ThoughtAdapter(@NonNull android.content.Context context, ArrayList<ThoughtDetails> list) {
        super(context, R.layout.thought_row,list);
        this.context=context;
        this.list=list;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.thought_row, parent,false);
        }
        final ThoughtDetails td=list.get(position);

        TextView name=convertView.findViewById(R.id.tvname);
        TextView date=convertView.findViewById(R.id.tvdate);
        TextView thought=convertView.findViewById(R.id.tvthought);
        TextView comments=convertView.findViewById(R.id.tvcommentcount);
        ImageView dp = convertView.findViewById(R.id.dp);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //oast.makeText(context, "CLicked.", Toast.LENGTH_SHORT).show();
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                //view.
                //firestore.collection("thoughts").document()

                firestore.collection("thoughts").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot Snapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            Log.i("TRUTH", "inside null");
                            Toast.makeText(context, "Some error"+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        else {
                            //ThoughtDetails td = new ThoughtDetails();
                            //DocumentSnapshot doc =
                            List<DocumentSnapshot> documents= Snapshots.getDocuments();
                            DocumentSnapshot docc = documents.get(position);
                            Intent cla = new Intent(context, CommentListActivity.class);
                            cla.putExtra("tid", docc.getId());
                            context.startActivity(cla);
                            //docc.getId();
                            //Toast.makeText(context, ""+docc.getId(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });




        /*if(cb.isChecked())
        {
            cb.setChecked(true);
            x = Integer.parseInt(likes.getText().toString());
            x = x+1;

        }*/
        //final ThoughtDetails td = new ThoughtDetails();


        /*firestore.collection("users").orderBy("cdate", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot Snapshots, @Nullable FirebaseFirestoreException e) {


                List<DocumentSnapshot> documents = Snapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    Log.i("ERR", "c1");
                    td.setName(document.getString("Name"));
                    td.setPhoto(Integer.parseInt(document.getString("dp")));

                    firestore.collection("users").document(document.getId()).collection("thoughts").orderBy("cdate").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot Snapshots, @Nullable FirebaseFirestoreException e) {
                            Log.i("ERR", "c2");
                            List<DocumentSnapshot> documents = Snapshots.getDocuments();
                            for (DocumentSnapshot document : documents) {
                                td.setName(document.getString("Name"));
                                td.setThought(document.getString("thoughttext"));
                                td.setDate(document.getString("cdate"));

                            }
                        }
                    });

                }
            }
        });*/

        Log.i("ERR","c3");
        name.setText(td.getName());
        date.setText(td.getDate().toString());
        thought.setText(td.getThought());
        //likes.setText(String.valueOf(x));
        comments.setText(String.valueOf(td.getComments()));
        return convertView;
    }
}
