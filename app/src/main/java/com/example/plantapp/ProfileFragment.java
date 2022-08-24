package com.example.plantapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "ProfileActivity";

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout,buttonPost;
    private TextView textivewDelete;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //initializing views
        textViewUserEmail = (TextView) view.findViewById(R.id.textviewUserEmail);
        buttonLogout = (Button) view.findViewById(R.id.buttonLogout);
        buttonPost=(Button) view.findViewById(R.id.buttonPost);
        textivewDelete = (TextView) view.findViewById(R.id.textviewDelete);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
        //textViewUserEmail의 내용을 변경해 준다.
        textViewUserEmail.setText(user.getEmail());



        //logout button event
        buttonPost.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View view){
            if (view == buttonLogout) {
                firebaseAuth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
            if (view==buttonPost){
                startActivity(new Intent(getContext(), PostActivity.class));
            }
            //회원탈퇴를 클릭하면 회원정보를 삭제한다. 삭제전에 컨펌창을 하나 띄워야 겠다.
            if (view == textivewDelete) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
                alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getContext(), "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
//                                            finish();
                                                startActivity(new Intent(getContext(), MainActivity.class));
                                            }
                                        });
                            }
                        }
                );
                alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "취소", Toast.LENGTH_LONG).show();
                    }
                });
                alert_confirm.show();

            }
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}