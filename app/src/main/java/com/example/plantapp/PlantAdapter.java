package com.example.plantapp;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {
    private ArrayList<Plant> cardModel;
    private LayoutInflater layoutInflater;
    private Context context;
    private ViewPager2 viewPager2;
    private AlertDialog.Builder builder;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private StorageReference storageRef=storage.getReference();
    private String birth,name,predday;
    private int tyear,tmonth,tday;
    private int ONE_DAY= 24 * 60 * 60 * 1000;
    // 현재 날짜를 알기 위해 사용
    private String Dday;
    private long d,t,r;
    private int resultNumber=0;

    private int cnt=0;

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
        birth=cardModel.get(i).getBirthday();
        Log.d("birth",birth);
        name=cardModel.get(i).getName();
        Log.d("name",name);
        predday=cardModel.get(i).getDday();
        cnt=cardModel.get(i).getFlower_water();

        switch (cnt){
            case 5:
                holder.info_watericon1.setVisibility(View.VISIBLE);
                holder.info_watericon2.setVisibility(View.VISIBLE);
                holder.info_watericon3.setVisibility(View.VISIBLE);
                holder.info_watericon4.setVisibility(View.VISIBLE);
                holder.info_watericon5.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.info_watericon1.setVisibility(View.VISIBLE);
                holder.info_watericon2.setVisibility(View.VISIBLE);
                holder.info_watericon3.setVisibility(View.VISIBLE);
                holder.info_watericon4.setVisibility(View.GONE);
                holder.info_watericon5.setVisibility(View.GONE);
                break;
            case 1:
                holder.info_watericon1.setVisibility(View.VISIBLE);
                holder.info_watericon2.setVisibility(View.GONE);
                holder.info_watericon3.setVisibility(View.GONE);
                holder.info_watericon4.setVisibility(View.GONE);
                holder.info_watericon5.setVisibility(View.GONE);
                break;
            default:
                holder.info_watericon1.setVisibility(View.VISIBLE);
                holder.info_watericon2.setVisibility(View.GONE);
                holder.info_watericon3.setVisibility(View.GONE);
                holder.info_watericon4.setVisibility(View.GONE);
                holder.info_watericon5.setVisibility(View.GONE);

        }



        storageRef.child("Images")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("plants")
                .child(cardModel.get(i).getImageUrl())
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

        db.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("plants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            if(birth.equals(snapshot.get("birthday"))) {
                                if (name.equals(snapshot.get("name"))) {
                                    String doc = snapshot.getId();
                                    DocumentReference sfDocRef = db.collection("users")
                                            .document(firebaseAuth.getCurrentUser().getUid())
                                            .collection("plants")
                                            .document(doc);

                                    db.runTransaction(new Transaction.Function<Void>() {

                                        @Nullable
                                        @Override
                                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                            String result = dday(birth);
                                            if(predday.equals(result)){
                                                Log.d(TAG, "달라질 디데이 없음");
                                            }
                                            else {
                                                transaction.update(sfDocRef, "dday", result);
                                                Log.d("저장될이름:::::::::::", String.valueOf(snapshot.get("name")));
                                                Log.d("스냅샷날짜:::::::::::", String.valueOf(snapshot.get("birthday")));
                                                Log.d("비교할날짜:::::::::::", birth);
                                                Log.d("저장될디데이:::::::::::", result);

                                            }
                                            return null;

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "Transaction success!");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Transaction failure.", e);
                                        }
                                    });

                                } else {
                                    Log.d("실패", String.valueOf(snapshot.get("birthday")));
                                }
                            }
                        }
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlantInfoDialog cardDialog = new PlantInfoDialog(view.getContext());
                cardDialog.callFunction(cardModel.get(i));
            }
        });

//        holder.tv_plantname.setText(cardModel.get(i).getName());
//        holder.tv_plantday.setText(cardModel.get(i).getDday());
//
//        storageRef.child("Images")
//                .child(firebaseAuth.getCurrentUser().getUid())
//                .child("plants")
//                .child(cardModel.get(position).getImageUrl())
//                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(holder.itemView.getContext()).load(uri).into(holder.iv_profile);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(viewPager2.getContext(), "이미지로드실패", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PlantInfoDialog cardDialog = new PlantInfoDialog(view.getContext());
//                cardDialog.callFunction(cardModel.get(i));
//            }
//        });
    }
    public void reset() {
        Intent intent = ((Activity) context).getIntent();
        ((Activity) context).finish(); //현재 액티비티 종료 실시
        ((Activity) context).overridePendingTransition(0, 0); //효과 없애기
        ((Activity) context).startActivity(intent); //현재 액티비티 재실행 실시
        ((Activity) context).overridePendingTransition(0, 0); //효과 없애기
    }
    public String dday(String birth){
        int i=getItemCount();
            Calendar calendar=Calendar.getInstance();
            tyear=calendar.get(Calendar.YEAR);
            tmonth=calendar.get(Calendar.MONTH)+1;
            tday=calendar.get(Calendar.DAY_OF_MONTH)+1;

            int birth_year=Integer.parseInt(birth.substring(0,4));
            int birth_month=Integer.parseInt(birth.substring(5,6));
            int birth_day=Integer.parseInt(birth.substring(7));

            Calendar dcalender=Calendar.getInstance();
            birth_month-=1;
            dcalender.set(birth_year,birth_month,birth_day);
            t=calendar.getTimeInMillis();
            d=dcalender.getTimeInMillis();
            r=(t-d)/ONE_DAY;
            resultNumber=(int)r;
            if(resultNumber<0){
                Dday=String.format("D%d",resultNumber);
            }
            else{

                Dday=String.format("D+%d",resultNumber+1);
            }
            Log.d("dday함수결과:::::", String.valueOf(Dday));

        return Dday;

    }



    @Override
    public int getItemCount() {
        return cardModel.size();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        TextView tv_plantname,tv_plantday,
                info_watericon1,info_watericon2,info_watericon3,info_watericon4,info_watericon5,
                info_lighticon1,info_lighticon2,info_lighticon3,info_lighticon4,info_lighticon5;;

        public PlantViewHolder(@NonNull View view) {
            super(view);
            iv_profile = view.findViewById(R.id.iv_profile);
            tv_plantname=view.findViewById(R.id.tv_plantname);
            tv_plantday=view.findViewById(R.id.tv_plantday);
            info_watericon1= view.findViewById(R.id.info_watericon1);
            info_watericon2= view.findViewById(R.id.info_watericon2);
            info_watericon3= view.findViewById(R.id.info_watericon3);
            info_watericon4= view.findViewById(R.id.info_watericon4);
            info_watericon5= view.findViewById(R.id.info_watericon5);
        }
    }
}
