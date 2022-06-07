package com.example.plantapp;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class PlantInfoDialog extends Fragment {

    private String TAG = "CardDialogTAG";
    private Context context;
    private String water_cycle, birth_date;
    private int tyear,tmonth,tday;
    private int ONE_DAY= 24 * 60 * 60 * 1000;
    // 현재 날짜를 알기 위해 사용
    private String Dday;
    private long d,t,r;
    private int resultNumber=0;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseStorage db=FirebaseStorage.getInstance();
    StorageReference storageRef=db.getReference();

    // 커스텀 다이얼로그의 각 위젯들을 정의한다.
    private MaterialToolbar cardView_toolbar;
    private ImageView iv_profile;
    private TextView tv_plantname, tv_plantday,tv_info_dday,tv_info_watercycle,tv_info_waterlast,
            info_watericon1,info_watericon2,info_watericon3,info_watericon4,info_watericon5,
            info_lighticon1,info_lighticon2,info_lighticon3,info_lighticon4,info_lighticon5;
    private ToggleButton tg_wateron,tg_lighton;

    public PlantInfoDialog(Context context) {
        this.context = context;
    }

    ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> arr;
    PlantViewModel plantViewModel;

//    public void dday(){
//        Calendar calendar=Calendar.getInstance();
//        tyear=calendar.get(Calendar.YEAR);
//        tmonth=calendar.get(Calendar.MONTH);
//        tday=calendar.get(Calendar.DAY_OF_MONTH);
//
//        int birth_year=Integer.parseInt(birth_date.substring(0,4));
//        int birth_month=Integer.parseInt(birth_date.substring(5,6));
//        int birth_day=Integer.parseInt(birth_date.substring(7,8));
//
//        Calendar dcalender=Calendar.getInstance();
//        birth_month-=1;
//        dcalender.set(birth_year,birth_month,birth_day);
//        t=calendar.getTimeInMillis();
//        d=dcalender.getTimeInMillis();
//        r=(d-t)/ONE_DAY;
//        resultNumber=(int)r;
//        if(resultNumber>=0){
//            Dday=String.format("D-%d",resultNumber= Math.abs(resultNumber));
//        }
//        else{
//            Dday=String.format("D+%d",resultNumber= Math.abs(resultNumber)+1);
//        }
//    }

    public void callFunction(Plant cardInfo){
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        dlg.setCanceledOnTouchOutside(false);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_plantinfo);

        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes((WindowManager.LayoutParams) params);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        cardView_toolbar = dlg.findViewById(R.id.cardView_toolbar);

        iv_profile = dlg.findViewById(R.id.iv_profile);
        tv_plantname= dlg.findViewById(R.id.tv_plantname);
        tv_plantday= dlg.findViewById(R.id.tv_plantday);
        tv_info_dday= dlg.findViewById(R.id.tv_info_dday);
        tv_info_watercycle= dlg.findViewById(R.id.tv_info_watercycle);
        tv_info_waterlast= dlg.findViewById(R.id.tv_info_waterlast);

        info_watericon1= dlg.findViewById(R.id.info_watericon1);
        info_watericon2= dlg.findViewById(R.id.info_watericon2);
        info_watericon3= dlg.findViewById(R.id.info_watericon3);
        info_watericon4= dlg.findViewById(R.id.info_watericon4);
        info_watericon5= dlg.findViewById(R.id.info_watericon5);
        info_lighticon1= dlg.findViewById(R.id.info_lighticon1);
        info_lighticon2= dlg.findViewById(R.id.info_lighticon2);
        info_lighticon3= dlg.findViewById(R.id.info_lighticon3);
        info_lighticon4= dlg.findViewById(R.id.info_lighticon4);
        info_lighticon5= dlg.findViewById(R.id.info_lighticon5);

        tg_wateron=dlg.findViewById(R.id.tg_wateron);
        tg_lighton=dlg.findViewById(R.id.tg_lighton);

        storageRef.child("Images")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("plants")
                .child(cardInfo.getImageUrl())
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(iv_profile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        tv_plantday.setText(cardInfo.getBirthday());
        birth_date=cardInfo.getBirthday();
        tv_plantname.setText(cardInfo.getName());
        tv_info_watercycle.setText(cardInfo.getWater_cycle());

//        dday();
//        cardInfo.setDday(Dday);
        tv_info_dday.setText(cardInfo.getDday());



        cardView_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });



        tg_wateron.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(context,"물주기활성화",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context,"물주기비활성화",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tg_lighton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(context,"광합성활성화",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,"광합성비활성화",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
