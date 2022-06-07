package com.example.plantapp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {
    private ArrayList<Plant> cardModel;
    private LayoutInflater layoutInflater;
    private Context context;
    private ViewPager2 viewPager2;
    private AlertDialog.Builder builder;

    public PlantAdapter(ArrayList<Plant> cardModel, ViewPager2 viewPager2) {
        this.cardModel = cardModel;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlantAdapter.PlantViewHolder(layoutInflater.from(parent.getContext()).inflate(R.layout.itme_plant_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        int i = position;

        holder.tv_plantname.setText(cardModel.get(i).getName());
        holder.tv_plantday.setText(cardModel.get(i).getDday());


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageRef=storage.getReference();

        storageRef.child("Images")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("plants")
                .child(cardModel.get(position).getImageUrl())
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.itemView.getContext()).load(uri).into(holder.iv_profile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(viewPager2.getContext(), "이미지로드실패", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlantInfoDialog cardDialog = new PlantInfoDialog(view.getContext());
                cardDialog.callFunction(cardModel.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardModel.size();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        TextView tv_plantname,tv_plantday;

        public PlantViewHolder(@NonNull View view) {
            super(view);
            iv_profile = view.findViewById(R.id.iv_profile);
            tv_plantname=view.findViewById(R.id.tv_plantname);
            tv_plantday=view.findViewById(R.id.tv_plantday);
        }
    }
}
