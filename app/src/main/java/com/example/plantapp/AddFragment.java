package com.example.plantapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddFragment extends Fragment {
    private TextView tv;
    FragmentManager fragmentManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        tv=view.findViewById(R.id.tv);
        fragmentManager = getActivity().getSupportFragmentManager();

        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigation.setVisibility(View.GONE);

        return view;
    }
}
