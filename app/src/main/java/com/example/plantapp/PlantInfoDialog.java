package com.example.plantapp;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class PlantInfoDialog extends Fragment {

    private String TAG = "CardDialogTAG";
    private Context context;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseStorage db = FirebaseStorage.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private StorageReference storageRef = db.getReference();

    // 커스텀 다이얼로그의 각 위젯들을 정의한다.
    private MaterialToolbar cardView_toolbar;
    private ImageView iv_profile;
    private TextView tv_plantname, tv_plantday, tv_info_dday, tv_info_watercycle, tv_info_waterlast,
            info_watericon1, info_watericon2, info_watericon3, info_watericon4, info_watericon5,
            info_lighticon1, info_lighticon2, info_lighticon3, info_lighticon4, info_lighticon5;
    private Button btn_water, btn_light;

    public PlantInfoDialog(Context context) {
        this.context = context;
    }

    ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> arr;
    PlantViewModel plantViewModel;
    private int tyear, tmonth, tday;
    private int ONE_DAY = 24 * 60 * 60 * 1000;
    // 현재 날짜를 알기 위해 사용
    private String Dday;
    private long d, t, r;
    private int resultNumber, i = 0;

    private String Toname, Tobirth, lastday, cycle;
    private int cycles = 0;
    private PlantModel plantModel;

    public String today() {
        Calendar calendar = Calendar.getInstance();
        tyear = calendar.get(Calendar.YEAR);
        tmonth = calendar.get(Calendar.MONTH) + 1;
        tday = calendar.get(Calendar.DAY_OF_MONTH);

        String today = tyear + "-" + tmonth + "-" + tday;
        Log.d("today::::::::", today);

        return today;

    }

    public int setwater(String lastday) {
        Calendar calendar = Calendar.getInstance();
        tyear = calendar.get(Calendar.YEAR);
        tmonth = calendar.get(Calendar.MONTH) + 1;
        tday = calendar.get(Calendar.DAY_OF_MONTH) + 1;

        int birth_year = Integer.parseInt(lastday.substring(0, 4));
        int birth_month = Integer.parseInt(lastday.substring(5, 6));
        int birth_day = Integer.parseInt(lastday.substring(7));

        Calendar dcalender = Calendar.getInstance();
        birth_month -= 1;
        dcalender.set(birth_year, birth_month, birth_day);
        t = calendar.getTimeInMillis();
        d = dcalender.getTimeInMillis();
        r = (t - d) / ONE_DAY;
        resultNumber = (int) r;

        return resultNumber;

    }


    public void callFunction(Plant cardInfo) {
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
        tv_plantname = dlg.findViewById(R.id.tv_plantname);
        tv_plantday = dlg.findViewById(R.id.tv_plantday);
        tv_info_dday = dlg.findViewById(R.id.tv_info_dday);
        tv_info_watercycle = dlg.findViewById(R.id.tv_info_watercycle);
        tv_info_waterlast = dlg.findViewById(R.id.tv_info_waterlast);

        info_watericon1 = dlg.findViewById(R.id.info_watericon1);
        info_watericon2 = dlg.findViewById(R.id.info_watericon2);
        info_watericon3 = dlg.findViewById(R.id.info_watericon3);
        info_watericon4 = dlg.findViewById(R.id.info_watericon4);
        info_watericon5 = dlg.findViewById(R.id.info_watericon5);
        info_lighticon1 = dlg.findViewById(R.id.info_lighticon1);
        info_lighticon2 = dlg.findViewById(R.id.info_lighticon2);
        info_lighticon3 = dlg.findViewById(R.id.info_lighticon3);
        info_lighticon4 = dlg.findViewById(R.id.info_lighticon4);
        info_lighticon5 = dlg.findViewById(R.id.info_lighticon5);

        btn_water = dlg.findViewById(R.id.btn_water);
        btn_light = dlg.findViewById(R.id.btn_light);

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

        Toname = cardInfo.getName();
        Tobirth = cardInfo.getBirthday();
        cycle = cardInfo.getWater_cycle();
        cycles = Integer.parseInt(cycle);

        if (cardInfo.getWater_lastday() != "") {
            lastday = cardInfo.getWater_lastday();
            int interval = setwater(lastday);
            int half_cycles = (int) cycles / 2;

            if (interval > cycles) {
                i = 1;
                info_watericon1.setVisibility(View.VISIBLE);
                info_watericon2.setVisibility(View.GONE);
                info_watericon3.setVisibility(View.GONE);
                info_watericon4.setVisibility(View.GONE);
                info_watericon5.setVisibility(View.GONE);
            } else if (interval > half_cycles) {
                i = 3;
                info_watericon1.setVisibility(View.VISIBLE);
                info_watericon2.setVisibility(View.VISIBLE);
                info_watericon3.setVisibility(View.VISIBLE);
                info_watericon4.setVisibility(View.GONE);
                info_watericon5.setVisibility(View.GONE);
            } else {
                i = 5;
                info_watericon1.setVisibility(View.VISIBLE);
                info_watericon2.setVisibility(View.VISIBLE);
                info_watericon3.setVisibility(View.VISIBLE);
                info_watericon4.setVisibility(View.VISIBLE);
                info_watericon5.setVisibility(View.VISIBLE);
            }
            firestore.collection("users")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .collection("plants")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                if (Tobirth.equals(snapshot.get("birthday"))) {
                                    if (Toname.equals(snapshot.get("name"))) {
                                        String doc = snapshot.getId();
                                        DocumentReference sfDocRef = firestore.collection("users")
                                                .document(firebaseAuth.getCurrentUser().getUid())
                                                .collection("plants")
                                                .document(doc);

                                        firestore.runTransaction(new Transaction.Function<Void>() {

                                            @Nullable
                                            @Override
                                            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                                transaction.update(sfDocRef, "flower_water", i);
                                                return null;
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "Transaction success!");

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Transaction failure.", e);
                                            }
                                        });

                                    } else {
                                        Log.d("실패", String.valueOf(snapshot.get("birthday")));
                                    }
                                }
                            }
                        }
                    });



            tv_plantday.setText(cardInfo.getBirthday());
            tv_plantname.setText(cardInfo.getName());
            tv_info_watercycle.setText(cardInfo.getWater_cycle());
            tv_info_dday.setText(cardInfo.getDday());
            tv_info_waterlast.setText(cardInfo.getWater_lastday());


            cardView_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();
                }
            });


            btn_water.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String now = today();

                    firestore.collection("users")
                            .document(firebaseAuth.getCurrentUser().getUid())
                            .collection("plants")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        if (Tobirth.equals(snapshot.get("birthday"))) {
                                            if (Toname.equals(snapshot.get("name"))) {
                                                String doc = snapshot.getId();
                                                DocumentReference sfDocRef = firestore.collection("users")
                                                        .document(firebaseAuth.getCurrentUser().getUid())
                                                        .collection("plants")
                                                        .document(doc);

                                                firestore.runTransaction(new Transaction.Function<Void>() {

                                                    @Nullable
                                                    @Override
                                                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                                        transaction.update(sfDocRef, "water_lastday", now);
                                                        return null;
                                                    }
                                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d(TAG, "Transaction success!");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Transaction failure.", e);
                                                    }
                                                });

                                            } else {
                                                Log.d("실패", String.valueOf(snapshot.get("birthday")));
                                            }
                                        }
                                    }
                                }
                            });
                    info_watericon1.setVisibility(View.VISIBLE);
                    info_watericon2.setVisibility(View.VISIBLE);
                    info_watericon3.setVisibility(View.VISIBLE);
                    info_watericon4.setVisibility(View.VISIBLE);
                    info_watericon5.setVisibility(View.VISIBLE);
                    tv_info_waterlast.setText(now);
                    context.startActivity(new Intent(context, BluetoothActivity.class));

                }
            });

            btn_light.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        }
    }
}
