package com.example.plantapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class PlantViewModel extends AndroidViewModel {

    private PlantModel myCardModel;
    private MutableLiveData<ArrayList<Plant>> userInfo;
    private MutableLiveData<String> cardId;
    private LiveData<List<Plant>> allCards;

    public PlantViewModel(@NonNull Application application) {
        super(application);
        myCardModel = new PlantModel(application);
        userInfo = myCardModel.getUserInfo();
        cardId = myCardModel.getCardId();

    }
    public void userInfo(){
        myCardModel.userInfo();
        myCardModel.getCardId();
    }
    public MutableLiveData<ArrayList<Plant>> getUserInfo() {
        return userInfo;
    }

    public MutableLiveData<String> getCardId() {
        return cardId;
    }
}
