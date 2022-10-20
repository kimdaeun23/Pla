package com.example.plantapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecoActivity extends AppCompatActivity {
    LinearLayout layout;
    ImageView iv_reco,close;
    TextView tv_reco;

    private String sp;
    private Dictionary dictionary;
    private ImageView reco1,reco2,reco3,reco4;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco);

        Intent intent = getIntent();
        String reco = intent.getStringExtra("reco");

        layout=findViewById(R.id.banner);
        iv_reco=findViewById(R.id.iv_reco);
        tv_reco=findViewById(R.id.tv_reco);
        close=findViewById(R.id.close);

        if(reco.equals("reco1")){
            layout.setBackgroundResource(R.color.reco1);
            iv_reco.setImageResource(R.drawable.reco1);
            tv_reco.setText("초보 식물집사도 키우기 쉬운 식물");
            sp="초보집사";
        }else if(reco.equals("reco2")){
            layout.setBackgroundResource(R.color.reco2);
            iv_reco.setImageResource(R.drawable.reco2);
            tv_reco.setText("공기청정효과가 뛰어난 식물추천");
            sp="공기정화";
        }else if(reco.equals("reco3")){
            layout.setBackgroundResource(R.color.reco3);
            iv_reco.setImageResource(R.drawable.reco3);
            tv_reco.setText("게으른 식물집사 추천 물주기가 긴 식물");
            sp="게으른";
        }else if(reco.equals("reco4")){
            layout.setBackgroundResource(R.color.reco4);
            iv_reco.setImageResource(R.drawable.reco4);
            tv_reco.setText("인테리어 효과가 뛰어난 식물추천");
            sp="집꾸미기";
        }

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mDic=new ArrayList<>();
        dicAdapter=new DictionaryAdapter(getApplicationContext(),mDic);
        recyclerView.setAdapter(dicAdapter);

        readUsers();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecoActivity.this,DictionaryActivity.class));
                finish();
            }
        });

    }
    private void readUsers(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Dictionary");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mDic.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Dictionary user=snapshot.getValue(Dictionary.class);
                        if(user.getSp_dictionary().equals(sp)){
                            mDic.add(user);
                        }
                    }
                    dicAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
