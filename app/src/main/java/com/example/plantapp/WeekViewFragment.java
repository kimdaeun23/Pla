package com.example.plantapp;

import static android.content.ContentValues.TAG;
import static com.example.plantapp.CalendarUtils.daysInWeekArray;
import static com.example.plantapp.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private TextView monthYearText, eventCellplant;
    private RecyclerView calendarRecyclerView,eventdataRecyclerView;
    private ListView eventListView;
    private Button btn_pre,btn_nx,btn_newevent;
    EventDataAdapter madapter;
    private String initdate="";
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private String exists="";
    private TextView profile,search;

    private EventDataViewModel eventDataViewModel;
    private ArrayList<EventData> arrCard;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_view, container, false);

        initWidgets(view);

//        eventDataViewModel.userInfo();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setWeekView();
//        setlist();
        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });

        btn_nx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
                setWeekView();
            }
        });

        btn_newevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BottomActivity)getActivity()).replaceeventedit();
            }
        });
        search=view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        profile=view.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BottomActivity)getActivity()).replaceprofile();
            }
        });

        return view;
    }
    private void initWidgets(View view)
    {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
//        eventListView = view.findViewById(R.id.eventListView);
        eventdataRecyclerView=view.findViewById(R.id.eventdataRecyclerView);
        btn_newevent = view.findViewById(R.id.btn_newevent);
        btn_nx = view.findViewById(R.id.btn_nx);
        btn_pre = view.findViewById(R.id.btn_pre);
        eventCellplant=view.findViewById(R.id.eventCellplant);
    }

    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
//        setEventAdpater();
    }


    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        initdate=date.toString();
        setlist();
        setWeekView();
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    public void setlist(){
        db.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("events")
                .document(initdate)
                .collection("plans")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot querySnapshot= task.getResult();
                            if(querySnapshot.isEmpty()){
                                Log.d(TAG, "Document not exists!");
                                eventdataRecyclerView.setVisibility(View.GONE);
                            }
                            else{
                                Log.d(TAG, "Document exists!");
                                eventdataRecyclerView.setVisibility(View.VISIBLE);
                                arrCard = new ArrayList<>();
                                Log.d("date",initdate);
                                db.collection("users")
                                        .document(firebaseAuth.getCurrentUser().getUid())
                                        .collection("events")
                                        .document(initdate)
                                        .collection("plans")
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot item: queryDocumentSnapshots){
                                                    RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
                                                    String str_name = item.get("name").toString();
                                                    String str_eventCellplant = item.get("eventCellplant").toString();
                                                    String str_event_type = item.get("name").toString();
                                                    String str_date = item.get("name").toString();
                                                    arrCard.add(new EventData(str_name,str_date,str_eventCellplant,str_event_type));
                                                    madapter= new EventDataAdapter(arrCard);
                                                    eventdataRecyclerView.setLayoutManager(layoutManager);
                                                    madapter.notifyDataSetChanged();
                                                    eventdataRecyclerView.setAdapter(madapter);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

    }


}
