package com.example.plantapp;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
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


    public MutableLiveData<ArrayList<Plant>> getUserInfo() {
        return userInfo;
    }

    public MutableLiveData<String> getCardId() {
        return cardId;
    }

}
