package com.example.plantapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;
    private User user,del_follow;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private boolean isfollowing=false;

    public UserAdapter(Context mContext,List<User> mUsers){
        this.mContext=mContext;
        this.mUsers=mUsers;
    }
    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        if ((mUsers.get(position).getId()).equals(firebaseUser.getUid())){
            holder.btn_follow.setVisibility(View.GONE);
            user=mUsers.get(position);
            if (user.getImageurl().equals("noprofile")){
                Glide.with(mContext).load(R.drawable.seedling_solid).into(holder.image_profile);

            }else {
                Glide.with(mContext).load(user.getImageurl()).into(holder.image_profile);
            }
        }
        user=mUsers.get(position);
        if (user.getImageurl().equals("noprofile")){
            Glide.with(mContext).load(R.drawable.seedling_solid).into(holder.image_profile);

        }else {
            Glide.with(mContext).load(user.getImageurl()).into(holder.image_profile);
        }
        holder.nickname.setText(mUsers.get(position).getNickname());

        isFollowing(mUsers.get(position).getId(),holder.btn_follow);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mContext,BottomActivity.class);
                intent.putExtra("publisherid",mUsers.get(position).getId());
                mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btn_follow.getText().toString().equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(mUsers.get(position).getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(mUsers.get(position).getId())
                            .child("followers").child(firebaseUser.getUid()).setValue(true);

                    addNotifications(user.getId());

                    String email=mUsers.get(position).getEmail();
                    mUsers.get(position).setEmail(email.replace(">", "."));
                    db.collection("users")
                            .document(firebaseAuth.getCurrentUser().getUid())
                            .collection("following")
                            .add(mUsers.get(position))
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                }
                            });
                    user.setEmail(email.replace(">", "."));
                    db.collection("users")
                            .document(mUsers.get(position).getId())
                            .collection("follower")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                }
                            });
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(mUsers.get(position).getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(mUsers.get(position).getId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();

                    CollectionReference sfColRef = db.collection("users")
                            .document(firebaseUser.getUid())
                            .collection("following");

                    sfColRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                    del_follow=snapshot.toObject(User.class);

                                    if(snapshot.get("id").equals(mUsers.get(position).getId())){
                                        String doc=snapshot.getId();
                                        Task<Void> sfDocRef = db.collection("users")
                                                .document(firebaseUser.getUid())
                                                .collection("following").document(doc).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
                    CollectionReference sfColRef2 = db.collection("users")
                            .document(mUsers.get(position).getId())
                            .collection("follower");

                    sfColRef2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                    del_follow=snapshot.toObject(User.class);

                                    if(snapshot.get("id").equals(firebaseUser.getUid())){
                                        String doc=snapshot.getId();
                                        Task<Void> sfDocRef = db.collection("users")
                                                .document(mUsers.get(position).getId())
                                                .collection("follower").document(doc).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
                }

            }
        });
    }

    private void addNotifications(String userid){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("userid",firebaseUser.getUid());
        hashMap.put("text","started following you");
        hashMap.put("postid","");
        hashMap.put("ispost",false);

        reference.push().setValue(hashMap);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public CircleImageView image_profile;
        public Button btn_follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nickname=itemView.findViewById(R.id.nickname);
            image_profile=itemView.findViewById(R.id.image_profile);
            btn_follow=itemView.findViewById(R.id.btn_follow);
        }
    }

    private void isFollowing(String userid,Button btn){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userid).exists()){
                    btn.setText("following");
                }
                else
                    btn.setText("follow");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
