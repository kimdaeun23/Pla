package com.example.plantapp;

import static android.view.Gravity.CENTER;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BottomActivity extends AppCompatActivity implements View.OnClickListener{
    private BottomAppBar mBottomAppBar;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment fragmenthome = new HomeFragment();
    private PlantFragment fragmentplant = new PlantFragment();
    private AddFragment fragmentadd = new AddFragment();
    private WeekViewFragment fragmentweekView= new WeekViewFragment();
    private EventEditFragment fragmenteventEdit= new EventEditFragment();
    private FloatingActionButton fab_main;
    private FragmentTransaction transaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
        transaction.replace(R.id.nav_host_fragment, fragmenthome).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        fab_main = findViewById(R.id.fab_main);

        fab_main.setOnClickListener(this);



//        BottomNavigationView bottomNavigation = BottomActivity.this.findViewById(R.id.bottomNavigationView);
//        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.plantFragment:
//                        startActivity(new Intent(BottomActivity.this, PlantFragment.class));
//                }
//                return true;
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transactionadd = fragmentManager.beginTransaction();
        transactionadd.replace(R.id.nav_host_fragment, fragmentadd).commitAllowingStateLoss();
    }


    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.homeFragment:
                    transaction.replace(R.id.nav_host_fragment, fragmenthome).commitAllowingStateLoss();
                    break;
                case R.id.plantFragment:
                    transaction.replace(R.id.nav_host_fragment, fragmentplant).commitAllowingStateLoss();
                    break;
//                case R.id.fab_main:
//                    transaction.replace(R.id.nav_host_fragment, fragmentadd).commitAllowingStateLoss();
//                    break;

            }

            return true;
        }
    }
    public void replaceweekview(){
        FragmentTransaction transactionfrag = fragmentManager.beginTransaction();
        transactionfrag.replace(R.id.nav_host_fragment, fragmentweekView).commit();
    }
    public void replaceeventedit(){
        FragmentTransaction transactionfrag = fragmentManager.beginTransaction();
        transactionfrag.replace(R.id.nav_host_fragment, fragmenteventEdit).commit();
    }

    public void replaceplant(){
        FragmentTransaction transactionfrag = fragmentManager.beginTransaction();
        transactionfrag.replace(R.id.nav_host_fragment, fragmentplant).commit();
    }
    public void replaceprofile(){
        startActivity(new Intent(this, ProfileActivity.class));
    }

}
