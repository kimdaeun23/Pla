package com.example.plantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class PlantFragment extends Fragment {
    private TextView tv_plantday,tv_plantname;
    private ImageView iv_plant;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage=FirebaseStorage.getInstance();
    private Plant plantaccount= new Plant();
    private Intent intent;

    private ArrayList<Plant> arrCard = new ArrayList<>();
    ViewPager2 viewPager2;
    PlantAdapter adapter;
    private PlantViewModel plantViewModel;
    private TextView profile,search;
    SwipeRefreshLayout swipeLayout;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        plantViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(PlantViewModel.class);
        plantViewModel.getUserInfo().observe(this, new Observer<ArrayList<Plant>>() {
            @Override
            public void onChanged(ArrayList<Plant> cards) {
                arrCard = cards;
                adapter = new PlantAdapter(cards, viewPager2);
                viewPager2.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant, container, false);
//        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout);
//
//
//        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//
//            public void onRefresh() {
//                adapter.notifyDataSetChanged();
//
//                swipeLayout.setRefreshing(false);
//
//            }
//
//        });
        viewPager2 = view.findViewById(R.id.my_card_viewpager);
        tv_plantname=view.findViewById(R.id.tv_plantname);
        tv_plantday=view.findViewById(R.id.tv_plantday);
        plantViewModel.userInfo();
        profile=view.findViewById(R.id.profile);
        search=view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BottomActivity)getActivity()).replaceprofile();
            }
        });


        return view;
    }

    private int arrSize(ArrayList<Plant> arrCard) {
        return arrCard.size();
    }


}
