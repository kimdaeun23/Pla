package com.example.plantapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder>{
    private Context mContext;
    private List<Dictionary> mUsers;
    private Dictionary user;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    public DictionaryAdapter(Context mContext,List<Dictionary> mUsers){
        this.mContext=mContext;
        this.mUsers=mUsers;
    }
    @NonNull
    @Override
    public DictionaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dictionary, parent, false);

        return new DictionaryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DictionaryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        user=mUsers.get(position);

        Glide.with(mContext).load(user.getImageurl()).into(holder.image_profile);

        holder.nickname.setText(mUsers.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,Dictionary_InfoActivity.class);
                intent.putExtra("name",mUsers.get(position).getName());
                Log.d("nameinput", mUsers.get(position).getName());
                mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public CircleImageView image_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nickname=itemView.findViewById(R.id.nickname);
            image_profile=itemView.findViewById(R.id.image_profile);
        }
    }
}
