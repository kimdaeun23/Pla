package com.example.plantapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Dictionary_InfoActivity extends AppCompatActivity {

    EditText et_name,et_water,et_temp,et_place,et_tip;
    TextView save,tv_name,tv_water,tv_temp,tv_place,tv_tip,title;
    Spinner sp_dictionary;
    ImageView iv_profile,close;
    private DictionaryAdapter dicAdapter;
    private List<Dictionary> mDic;
    private String name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdictionary);

        et_name=findViewById(R.id.name);
        et_water=findViewById(R.id.water);
        et_temp=findViewById(R.id.temp);
        et_place=findViewById(R.id.place);
        et_tip=findViewById(R.id.tip);
        save=findViewById(R.id.save);
        tv_name=findViewById(R.id.tv_name);
        tv_water=findViewById(R.id.tv_water);
        tv_temp=findViewById(R.id.tv_temp);
        tv_place=findViewById(R.id.tv_place);
        tv_tip=findViewById(R.id.tv_tip);
        title=findViewById(R.id.title);
        iv_profile=findViewById(R.id.iv_profile);
        close=findViewById(R.id.close);
        sp_dictionary=findViewById(R.id.sp_dictionary);

        save.setVisibility(View.GONE);
        et_name.setVisibility(View.GONE);
        et_water.setVisibility(View.GONE);
        et_temp.setVisibility(View.GONE);
        et_place.setVisibility(View.GONE);
        et_tip.setVisibility(View.GONE);
        sp_dictionary.setVisibility(View.GONE);
        tv_name.setVisibility(View.VISIBLE);
        tv_water.setVisibility(View.VISIBLE);
        tv_temp.setVisibility(View.VISIBLE);
        tv_place.setVisibility(View.VISIBLE);
        tv_tip.setVisibility(View.VISIBLE);
/*
        tv_name.setText("");
        tv_water.setText("");
        tv_temp.setText("");
        tv_place.setText("");
        tv_tip.setText("");
        Glide.with(getApplicationContext()).load(R.drawable.seedling_solid).into(iv_profile);
        title.setText("");*/

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dictionary_InfoActivity.this,DictionaryActivity.class));
                finish();
            }
        });

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        Log.d("name",name);


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Dictionary").child(name);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Dictionary user=dataSnapshot.getValue(Dictionary.class);
                        tv_name.setText(user.getName());
                        tv_water.setText(user.getWater());
                        tv_temp.setText(user.getTemp());
                        tv_place.setText(user.getPlace());
                        tv_tip.setText(user.getTip());
                        Glide.with(getApplicationContext()).load(user.getImageurl()).into(iv_profile);
                        title.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
