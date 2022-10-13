package com.example.plantapp;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddFragment extends Fragment {
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    private EditText et_name;
    private Button buttonSave;
    private String water_cycle, birth_date;
    private DatePicker datePicker;
    // Firebase
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Plant plantacoount;
    private Uri selectedImageUri;
    private String imageUrl;
    private int year,month,day,tyear,tmonth,tday;
    private int dyear=1,dmonth=1,dday=1;
    private int ONE_DAY= 24 * 60 * 60 * 1000;
    // 현재 날짜를 알기 위해 사용
    private Calendar mCalendar;
    private String Dday;
    private long d,t,r;
    private int resultNumber=0;
    static final int DATE_DIALOG_ID=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        imageview = (ImageView) view.findViewById(R.id.imageView);
        Spinner sp_water=(Spinner) view.findViewById(R.id.sp_water);
        buttonSave=(Button)view.findViewById(R.id.buttonSave);
        datePicker=(DatePicker) view.findViewById(R.id.datePicker);
        Locale.setDefault(Locale.KOREAN);
        et_name=(EditText) view.findViewById(R.id.et_name);


        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();



        ArrayAdapter water_Adapter = ArrayAdapter.createFromResource(getActivity(), R.array.물주기, android.R.layout.simple_spinner_dropdown_item);
        water_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_water.setAdapter(water_Adapter);

        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        sp_water.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                water_cycle=Integer.toString(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear+=1;
                birth_date=year+"-"+monthOfYear+"-"+dayOfMonth;

                Calendar calendar=Calendar.getInstance();
                tyear=calendar.get(Calendar.YEAR);
                tmonth=calendar.get(Calendar.MONTH);
                tday=calendar.get(Calendar.DAY_OF_MONTH);

                Calendar dcalender=Calendar.getInstance();
                monthOfYear-=1;
                dcalender.set(year,monthOfYear,dayOfMonth);
                t=calendar.getTimeInMillis();
                d=dcalender.getTimeInMillis();
                r=(t-d)/ONE_DAY;
                resultNumber=(int)r+1;
                if(resultNumber<0){
                    Dday=String.format("D%d",resultNumber);
                }
                else{
                    Dday=String.format("D+%d",resultNumber+1);
                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedImageUri != null){
                    imageUrl=Long.toString(System.currentTimeMillis());
                    StorageReference storageRef=storage.getReference()
                            .child("Images")
                            .child(firebaseAuth.getCurrentUser().getUid())
                            .child("plants")
                            .child(imageUrl);
                    storageRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Log.d(TAG,"사진 등록 완료");
                            plantacoount=new Plant();
                            plantacoount.setImageUrl(imageUrl);
                            plantacoount.setWater_cycle(water_cycle);
                            plantacoount.setBirthday(birth_date);
                            plantacoount.setDday(Dday);
                            String name = et_name.getText().toString();
                            plantacoount.setName(name);
                            plantacoount.setWater_lastday("");
                            addplant(plantacoount);
                        }
                    });

//                    Intent intent = ((Activity) getContext()).getIntent();
//                    ((Activity) getContext()).startActivity(intent);
                    ((BottomActivity)getActivity()).replaceplant();
                }
                else {
                    Toast.makeText(getContext(), "사진을 선택해 주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    public void addplant(Plant plantacoount){
        db.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("plants")
                .add(plantacoount)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,"등록 완료");
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);

        }

    }

    private String getToday() {
        // 지정된 format 으로 string 표시
        final String strFormat = getString(R.string.format_today);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat(strFormat);
        return CurDateFormat.format(mCalendar.getTime());
    }

    private String getDday(int a_year, int a_monthOfYear, int a_dayOfMonth) {
        // D-day 설정
        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(a_year, a_monthOfYear, a_dayOfMonth);

        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today 의 차를 구한다.
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dday - today;

        // 출력 시 d-day 에 맞게 표시
        final String strFormat;
        if (result > 0) {
            strFormat = "D-%d";
        } else if (result == 0) {
            strFormat = "D-Day";
        } else {
            result *= -1;
            strFormat = "D+%d";
        }

        final String strCount = (String.format(strFormat, result));
        return strCount;
    }

}

