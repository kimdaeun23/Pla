package com.example.plantapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event>
{
    private Event event;
    private MutableLiveData<ArrayList<Event>> listInfo= new MutableLiveData<>();;
    public EventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);
        TextView eventCellplant=convertView.findViewById(R.id.eventCellplant);
        ImageView iv_eventtype=convertView.findViewById(R.id.iv_eventtype);


        String eventTitle = event.getName();
        eventCellTV.setText(eventTitle);
        eventCellplant.setText(event.getEventCellplant());

        if((event.getEvent_type()).equals("기타")){
            iv_eventtype.setBackgroundResource(R.drawable.sprout);
        }
        else if((event.getEvent_type()).equals("물주기")){
            iv_eventtype.setBackgroundResource(R.drawable.drop);
        }
        else if((event.getEvent_type()).equals("광합성")){
            iv_eventtype.setBackgroundResource(R.drawable.sun);
        }
        return convertView;
    }

}
