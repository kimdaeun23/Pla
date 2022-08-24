package com.example.plantapp;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //define view objects
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextnickname;
    Button buttonSignup;
    TextView textviewSingin;
    TextView textviewMessage;
    ProgressDialog progressDialog;
    //define firebase object
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(), ProfileFragment.class)); //추가해 줄 ProfileActivity
        }
        //initializing views
        editTextnickname = (EditText) findViewById(R.id.editTextnickname);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textviewSingin= (TextView) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        progressDialog = new ProgressDialog(this);

        //button click event
        buttonSignup.setOnClickListener(this);
        textviewSingin.setOnClickListener(this);
    }

    //Firebse creating a new user
    private void registerUser(){
        //사용자가 입력하는 email, password를 가져온다.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String nickname =editTextnickname.getText().toString().trim();
        //email과 password가 비었는지 아닌지를 체크 한다.
        if(TextUtils.isEmpty(nickname)){
            Toast.makeText(this, "Nickname을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(nickname.length()<2){
            Toast.makeText(this, "Nickname 최소 2글자 이상 ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }

        //email과 password가 제대로 입력되어 있다면 계속 진행된다.
        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String uid = user.getUid();

                            //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                            HashMap<Object, String> hashMap = new HashMap<>();

                            hashMap.put("id",uid);
                            hashMap.put("email", email.replace(".", ">"));
                            hashMap.put("nickname", nickname);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            User usermodel=new User(email,nickname,uid);

                            Map<Object, String> users = new HashMap<>();
                            users.put("email", email.replace(".", ">"));
                            users.put("nickname",nickname);
                            users.put("id",uid);

                            firestore.collection("users").document(uid)
                                    .set(users)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "성공적으로 입력되었습니다");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "오류가 발생하였습니다", e);
                                        }
                                    });

                            startActivity(new Intent(getApplicationContext(), BottomActivity.class));
                        } else {
                            //에러발생시
                            Toast.makeText(MainActivity.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    //button click event
    @Override
    public void onClick(View view) {
        if(view == buttonSignup) {
            //TODO
            registerUser();
        }

        if(view == textviewSingin) {
            //TODO
            startActivity(new Intent(this, LoginActivity.class)); //추가해 줄 로그인 액티비티
        }
    }
}