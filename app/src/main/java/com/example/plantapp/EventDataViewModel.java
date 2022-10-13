package com.example.plantapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class EventDataViewModel extends AndroidViewModel {

    private EventDataModel myCardModel;
    private LiveData<ArrayList<EventData>> userInfo;
    private LiveData<List<EventData>> allCards;

    public EventDataViewModel(@NonNull Application application) {
        super(application);
        myCardModel = new EventDataModel(application);
        userInfo = myCardModel.getUserInfo();

    }
    public void userInfo(){
        myCardModel.userInfo();
    }
    public LiveData<ArrayList<EventData>> getUserInfo() {
        return userInfo;
    }

}
