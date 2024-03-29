package com.example.plantapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantapp.image.FlowerIdentificationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class DictionaryActivity extends AppCompatActivity {
    private Dictionary dictionary;
    private ImageView dictionary_add,reco1,reco2,reco3,reco4,btn;
    private RecyclerView recyclerView;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DictionaryAdapter dicAdapter;
    private List<Dictionary> mDic;
    private EditText search_bar;
    private List<String> data=new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("Dictionary");
    private static int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.recycler_view);
        dictionary_add=findViewById(R.id.dictionary_add);
        reco1=findViewById(R.id.reco1);
        reco2=findViewById(R.id.reco2);
        reco3=findViewById(R.id.reco3);
        reco4=findViewById(R.id.reco4);
        btn=findViewById(R.id.btn);

        if ((firebaseUser.getUid()).equals("eYA69vvENVQ8NPXXoWjLt33swyb2")){
            dictionary_add.setVisibility(View.VISIBLE);
        }

        dictionary_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditDictionaryActivity.class));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FlowerIdentificationActivity.class));
            }
        });

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        search_bar=findViewById(R.id.search_bar);

        mDic=new ArrayList<>();
        dicAdapter=new DictionaryAdapter(getApplicationContext(),mDic);
        recyclerView.setAdapter(dicAdapter);

        readUsers();
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase(Locale.ROOT));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        reco1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DictionaryActivity.this, RecoActivity.class);
                intent.putExtra("reco", "reco1");
                startActivity(intent);
            }
        });
        reco2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DictionaryActivity.this, RecoActivity.class);
                intent.putExtra("reco", "reco2");
                startActivity(intent);
            }
        });
        reco3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DictionaryActivity.this, RecoActivity.class);
                intent.putExtra("reco", "reco3");
                startActivity(intent);
            }
        });
        reco4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DictionaryActivity.this, RecoActivity.class);
                intent.putExtra("reco", "reco4");
                startActivity(intent);
            }
        });
    }

    private void readUsers(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Dictionary");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(search_bar.getText().toString().equals("")){
                    mDic.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Dictionary user=snapshot.getValue(Dictionary.class);
                        mDic.add(user);
                    }
                    dicAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchUsers(String s){
        Query query= FirebaseDatabase.getInstance().getReference("Dictionary").orderByChild("name")
                .startAfter(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDic.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Dictionary user =snapshot.getValue(Dictionary.class);
                    mDic.add(user);
                }
                dicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}