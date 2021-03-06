package com.example.plantapp;

import static java.security.AccessController.getContext;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EventDataModel {
    private String TAG="PlantModelTAG";

    private Application application;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<ArrayList<EventData>> userInfo;
    private ArrayList<EventData> arrCard = new ArrayList<>();
    private MutableLiveData<ArrayList<EventData>> cardMutableLiveData;
    private EventData card;
    private WeekViewFragment weekViewFragment;


    public EventDataModel(Application application){
        this.application=application;
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        arrCard = new ArrayList<>();
        userInfo = new MutableLiveData<>();
        cardMutableLiveData=new MutableLiveData<>();

    }

    public void userInfo(){
//        db.collection("users")
//                .document(firebaseAuth.getCurrentUser().getUid())
//                .collection("events")
//                .document("2022-06-07")
//                .collection("plans")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                            if(value.isEmpty()){
//                                Toast.makeText(application, "?????? ????????????.", Toast.LENGTH_SHORT).show();
//                            }
//                            for (DocumentChange dc : value.getDocumentChanges()) {
//
//                                switch (dc.getType()) {
//                                    case ADDED:
//                                        card = dc.getDocument().toObject(EventData.class);
//                                        arrCard.add(card);
//                                        cardMutableLiveData.postValue(arrCard);
//
//                                        break;
//                                    case MODIFIED:
////                                    arrCard.remove(dc.getOldIndex());
////                                    card = dc.getDocument().toObject(EventData.class);
////                                    arrCard.add(card);
////                                    Log.d(TAG, "Modified city: " + dc.getOldIndex());
//                                        break;
//                                    case REMOVED:
//                                        Log.d(TAG, "Removed city: " + dc.getDocument().getData());
//                                        cardMutableLiveData.postValue(arrCard);
//                                        break;
//                                }
//                            }
//                    }
//                });


    }

    public LiveData<ArrayList<EventData>> getUserInfo() {
        return cardMutableLiveData;
    }


}
