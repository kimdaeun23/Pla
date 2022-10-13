package com.example.plantapp;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


public class EventEditFragment extends Fragment {
    private EditText eventNameET;
    private TextView eventDateTV,tv_event_title;
    private Button btn_save;
    private String eventName;
    private String eventType;
    private String eventPlant;
    private Event eventaccount;
    // Firebase
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_edit, container, false);
        initWidgets(view);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();

        Spinner sp_event=(Spinner) view.findViewById(R.id.sp_event);
        ArrayAdapter event_Adapter = ArrayAdapter.createFromResource(getActivity(), R.array.이벤트, android.R.layout.simple_spinner_dropdown_item);
        event_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_event.setAdapter(event_Adapter);

        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventType.equals("기타")){
                    eventName = eventNameET.getText().toString();
                    eventPlant="";
                }
                if(eventType.equals("물주기")){
                    eventPlant=eventNameET.getText().toString();
                }
                if(eventType.equals("광합성")){
                    eventPlant=eventNameET.getText().toString();
                }

                String eventDate=CalendarUtils.selectedDate.toString();
                EventData newEventdata = new EventData(eventName, eventDate,eventPlant,eventType);
                Event newEvent = new Event(eventName, CalendarUtils.selectedDate,eventPlant,eventType);
                Event.eventsList.add(newEvent);
                addevent(newEventdata);
                Intent intent = ((BottomActivity) getContext()).getIntent();
                ((Activity) getContext()).startActivity(intent);
            }
        });

        sp_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    tv_event_title.setText("Plant Name");
                    eventName="물주기";
                    eventType="물주기";
                }
                else if(i==1){
                    tv_event_title.setText("Plant Name");
                    eventName="광합성";
                    eventType="광합성";
                }
                else if(i==2){
                    tv_event_title.setText("Title");
                    eventType="기타";

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
    private void initWidgets(View view)
    {
        eventNameET = view.findViewById(R.id.eventNameET);
        eventDateTV = view.findViewById(R.id.eventDateTV);
        btn_save = view.findViewById(R.id.btn_save);
        tv_event_title=view.findViewById(R.id.tv_event_title);
    }

    public void addevent(EventData eventaccount){
        db.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("events")
                .document(eventaccount.getDate())
                .collection("plans")
                .add(eventaccount)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,"등록 완료");
                    }
                });
    }
}
