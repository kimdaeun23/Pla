package com.example.plantapp;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
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
    private String aaa;

    private String water_cycle, birth_date;
    private int tyear,tmonth,tday;
    private int ONE_DAY= 24 * 60 * 60 * 1000;
    // 현재 날짜를 알기 위해 사용
    private String Dday;
    private long d,t,r;
    private int resultNumber=0;

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

//        db.collection("users")
//                .document(firebaseAuth.getCurrentUser().getUid())
//                .collection("plants")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for(QueryDocumentSnapshot snapshot : task.getResult()){
//                            aaa=cardModel.get(i).getBirthday();
//                            if(aaa.equals(snapshot.get("birthday"))){
//                                String doc=snapshot.getId();
//                                DocumentReference sfDocRef = db.collection("users")
//                                        .document(firebaseAuth.getCurrentUser().getUid())
//                                        .collection("plants")
//                                        .document(doc);
//
//                                db.runTransaction(new Transaction.Function<Void>() {
//
//                                    @Nullable
//                                    @Override
//                                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                                        String result=dday(aaa);
//
//                                        transaction.update(sfDocRef, "dday", result);
//                                        Log.d("저장될이름:::::::::::",String.valueOf(snapshot.get("name")));
//                                        Log.d("스냅샷날짜:::::::::::", String.valueOf(snapshot.get("birthday")));
//                                        Log.d("비교할날짜:::::::::::", aaa);
//                                        Log.d("저장될디데이:::::::::::",result);
//                                        return null;
//
//                                    }
//                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Log.d(TAG, "Transaction success!");
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.w(TAG, "Transaction failure.", e);
//                                    }
//                                });
//                                holder.tv_plantname.setText(cardModel.get(i).getName());
//                                holder.tv_plantday.setText(cardModel.get(i).getDday());
//                                storageRef.child("Images")
//                                        .child(firebaseAuth.getCurrentUser().getUid())
//                                        .child("plants")
//                                        .child(cardModel.get(i).getImageUrl())
//                                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        Glide.with(holder.itemView.getContext()).load(uri).into(holder.iv_profile);
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(viewPager2.getContext(), "이미지로드실패", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//                                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        PlantInfoDialog cardDialog = new PlantInfoDialog(view.getContext());
//                                        cardDialog.callFunction(cardModel.get(i));
//                                    }
//                                });
//                            }
//                            else {
//                                Log.d("실패",String.valueOf(snapshot.get("birthday")));
//                            }
//                        }
//                    }
//                });
        holder.tv_plantname.setText(cardModel.get(i).getName());
        holder.tv_plantday.setText(cardModel.get(i).getDday());

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

//    public String dday(String birth){
//        int i=getItemCount();
//            Calendar calendar=Calendar.getInstance();
//            tyear=calendar.get(Calendar.YEAR);
//            tmonth=calendar.get(Calendar.MONTH)+1;
//            tday=calendar.get(Calendar.DAY_OF_MONTH)+1;
//
//            int birth_year=Integer.parseInt(birth.substring(0,4));
//            int birth_month=Integer.parseInt(birth.substring(5,6));
//            int birth_day=Integer.parseInt(birth.substring(7));
//
//            Calendar dcalender=Calendar.getInstance();
//            birth_month-=1;
//            dcalender.set(birth_year,birth_month,birth_day);
//            t=calendar.getTimeInMillis();
//            d=dcalender.getTimeInMillis();
//            r=(t-d)/ONE_DAY;
//            resultNumber=(int)r+1;
//            if(resultNumber<0){
//                Dday=String.format("D%d",resultNumber);
//            }
//            else{
//
//                Dday=String.format("D+%d",resultNumber+1);
//            }
//            Log.d("dday함수결과:::::", String.valueOf(Dday));
//
//        return Dday;
//
//    }



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
