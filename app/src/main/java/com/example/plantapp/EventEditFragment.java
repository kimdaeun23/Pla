package com.example.plantapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.LocalTime;

public class EventEditFragment extends Fragment {
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private Button btn_save;

    private LocalTime time;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_edit, container, false);
        initWidgets(view);
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = eventNameET.getText().toString();
                Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
                Event.eventsList.add(newEvent);

                Intent intent = ((BottomActivity) getContext()).getIntent();
                ((Activity) getContext()).startActivity(intent);
            }
        });
        return view;
    }
    private void initWidgets(View view)
    {
        eventNameET = view.findViewById(R.id.eventNameET);
        eventDateTV = view.findViewById(R.id.eventDateTV);
        eventTimeTV = view.findViewById(R.id.eventTimeTV);
        btn_save = view.findViewById(R.id.btn_save);
    }
}
