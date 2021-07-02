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
    private String username;
    private String uid;

    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    public ThoughtAdapter(@NonNull android.content.Context context, ArrayList<ThoughtDetails> list, String username, String uid) {
        super(context, R.layout.thought_row,list);
        this.context=context;
        this.list=list;
        this.username = username;
        this.uid = uid;
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
        TextView commentText=convertView.findViewById(R.id.tvcommenttext);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                firestore.collection("thoughts").orderBy("cdate", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot Snapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            Toast.makeText(context, "Some error"+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        else {
                            List<DocumentSnapshot> documents= Snapshots.getDocuments();
                            DocumentSnapshot docc = documents.get(position);
                            Intent cla = new Intent(context, CommentListActivity.class);
                            cla.putExtra("tid", docc.getId());
                            cla.putExtra("name", username);
                            cla.putExtra("uid", uid);
                            context.startActivity(cla);
                        }
                    }

                });
            }
        });

        name.setText(td.getName());
        date.setText(td.getDate().toString());
        thought.setText(td.getThought());
        comments.setText(String.valueOf(td.getCommentCount()));
        commentText.setText("comments");
        return convertView;
    }
}
