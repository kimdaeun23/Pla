package com.example.plantapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
    private CommunityFragment fragmentcommunity=new CommunityFragment();
    private ProfileFragment fragmentprofile=new ProfileFragment();
    private DictionaryFragment fragmentdictionary=new DictionaryFragment();
    private LikeFragment fragmentlike=new LikeFragment();
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

        Bundle intent=getIntent().getExtras();
        if (intent != null){
            String publisher = intent.getString("publisherid");
            SharedPreferences.Editor editor= getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("profileid",publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new OtherprofileFragment()).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new HomeFragment()).commit();
        }

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
                case R.id.communityFragment:
                    transaction.replace(R.id.nav_host_fragment, fragmentcommunity).commitAllowingStateLoss();
                    break;
                case R.id.likeFragment:
                    transaction.replace(R.id.nav_host_fragment, fragmentlike).commitAllowingStateLoss();
                    break;

            }

            return true;
        }
    }
    public void replaceprofile(){
        FragmentTransaction transactionfrag = fragmentManager.beginTransaction();
        transactionfrag.replace(R.id.nav_host_fragment, fragmentprofile).commit();
    }
    public void replacedictionary(){
        FragmentTransaction transactionfrag = fragmentManager.beginTransaction();
        transactionfrag.replace(R.id.nav_host_fragment, fragmentdictionary).commit();
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
    public void replacebluetooth(){
        startActivity(new Intent(this, BluetoothActivity.class));
    }

}
