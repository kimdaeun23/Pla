package com.example.plantapp;

import android.app.Application;
import android.app.FragmentManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;

public class PlantModel {
    private String TAG="PlantModelTAG";

    private Application application;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private MutableLiveData<ArrayList<Plant>> userInfo;
    private ArrayList<Plant> arrCard = new ArrayList<>();
    private MutableLiveData<String> cardId;
    private String aaa,result;
    private String water_cycle, birth_date;
    private int tyear,tmonth,tday;
    private int ONE_DAY= 24 * 60 * 60 * 1000;
    // 현재 날짜를 알기 위해 사용
    private String Dday;
    private long d,t,r;
    private int resultNumber=0;
    private int i=0;
    private int size=0;


    private PlantFragment plantFragment;

    private Plant card;

    public PlantModel(Application application){
        this.application=application;

        userInfo = new MutableLiveData<>();
        cardId = new MutableLiveData<>();

    }

    public void userInfo(){

        db.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("plants")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentChange dc : value.getDocumentChanges()){
                            switch (dc.getType()){
                                case ADDED:
                                    card = dc.getDocument().toObject(Plant.class);
                                    arrCard.add(card);
                                    Log.d("arrcardsize:::::::;", String.valueOf(arrCard.size()));
//                                    Log.d("arrcardone:::::::;", arrCard.get(i).getBirthday());
                                    size=arrCard.size();
//                                    changdday(size);
                                    userInfo.postValue(arrCard);
                                    break;
                                case MODIFIED:
                                    arrCard.remove(dc.getOldIndex());
                                    card = dc.getDocument().toObject(Plant.class);
                                    arrCard.add(card);
                                    Log.d(TAG, "Modified city: " + dc.getOldIndex());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });
    }

    public void changdday(int size){
        for(i=0;i<size;i++) {
            db.collection("users")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .collection("plants")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("changddayiiiii", String.valueOf(i));
                                aaa = arrCard.get(i).getBirthday();
                                if (aaa.equals(snapshot.get("birthday"))) {
                                    String doc = snapshot.getId();
                                    DocumentReference sfDocRef = db.collection("users")
                                            .document(firebaseAuth.getCurrentUser().getUid())
                                            .collection("plants")
                                            .document(doc);

                                    db.runTransaction(new Transaction.Function<Void>() {

                                        @Nullable
                                        @Override
                                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                             result = dday(aaa);

                                            transaction.update(sfDocRef, "dday", result);
                                            Log.d("저장될이름:::::::::::", String.valueOf(snapshot.get("name")));
                                            Log.d("스냅샷날짜:::::::::::", String.valueOf(snapshot.get("birthday")));
                                            Log.d("비교할날짜:::::::::::", aaa);
                                            Log.d("저장될디데이:::::::::::", result);
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
                                    arrCard.get(i).setDday(result);
                                } else {
                                    Log.d("실패", String.valueOf(snapshot.get("birthday")));
                                }
                            }
                        }
                    });
        }
    }

    public String dday(String birth){
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
        resultNumber=(int)r+1;
        if(resultNumber<0){
            Dday=String.format("D%d",resultNumber);
        }
        else{

            Dday=String.format("D+%d",resultNumber+1);
        }
        Log.d("dday함수결과:::::", String.valueOf(Dday));

        return Dday;

    }


    public MutableLiveData<ArrayList<Plant>> getUserInfo() {
        return userInfo;
    }

    public MutableLiveData<String> getCardId() {
        return cardId;
    }

}
