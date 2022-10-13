package com.example.plantapp;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventDataAdapter extends RecyclerView.Adapter<EventDataAdapter.EventDataViewHolder> {
    private ArrayList<EventData> cardModel=new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Context context;
    private AlertDialog.Builder builder;
    private ArrayList<EventData> initList = new ArrayList<>();

    public EventDataAdapter(ArrayList<EventData> cardModel) {
        this.cardModel = cardModel;
        initList = new ArrayList<>();
        initList.addAll(cardModel);

    }

    @NonNull
    @Override
    public EventDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventDataAdapter.EventDataViewHolder(layoutInflater.from(parent.getContext()).inflate(R.layout.event_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventDataViewHolder holder, int position) {
        int i = position;

        EventData eventData=initList.get(i);
        holder.setItem(eventData);

    }

    @Override
    public int getItemCount() {
        Log.d("size",Integer.toString(initList.size()));
        return initList.size();
    }

    public class EventDataViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_eventtype;
        TextView eventCellTV,eventCellplant;

        public EventDataViewHolder(@NonNull View view) {
            super(view);
             eventCellTV = view.findViewById(R.id.eventCellTV);
             eventCellplant=view.findViewById(R.id.eventCellplant);
             iv_eventtype=view.findViewById(R.id.iv_eventtype);
        }
        public void setItem(EventData eventData){
            eventCellTV.setText(eventData.getName());
            eventCellplant.setText(eventData.getEventCellplant());

            if((eventData.getEvent_type()).equals("물주기")){
                iv_eventtype.setBackgroundResource(R.drawable.drop);
            }
            else if((eventData.getEvent_type()).equals("광합성")){
                iv_eventtype.setBackgroundResource(R.drawable.sun);
            }
            else{
                iv_eventtype.setBackgroundResource(R.drawable.sprout);
            }
        }
    }
}
