package com.example.plantapp;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

public class EditDictionaryActivity extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE = 200;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    EditText et_name,et_water,et_temp,et_place,et_tip;
    Spinner sp_dictionary;
    TextView save;
    ImageView iv_profile;
    private Uri mImageUri;
    private StorageTask uploadTask;
    private String plantname,water,temp,place,tip;
    StorageReference storageRef;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdictionary);

        et_name=findViewById(R.id.name);
        et_water=findViewById(R.id.water);
        et_temp=findViewById(R.id.temp);
        et_place=findViewById(R.id.place);
        et_tip=findViewById(R.id.tip);
        save=findViewById(R.id.save);
        iv_profile=findViewById(R.id.iv_profile);
        sp_dictionary=findViewById(R.id.sp_dictionary);
        ArrayAdapter sp_dictionary_Adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.사전식물종류, android.R.layout.simple_spinner_dropdown_item);
        sp_dictionary_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_dictionary.setAdapter(sp_dictionary_Adapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("dictionary");
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void upload(){
        plantname=et_name.getText().toString();
        water = et_water.getText().toString();
        temp = et_temp.getText().toString();
        place = et_place.getText().toString();
        tip = et_tip.getText().toString();
        String sp = sp_dictionary.getSelectedItem().toString();

        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (mImageUri!=null){

            StorageReference filereference=storageRef.child(System.currentTimeMillis() +"."+getFileExtension(mImageUri));

            uploadTask=filereference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        String myUrl=downloadUri.toString();

                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Dictionary");

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("imageurl",""+myUrl);
                        hashMap.put("name", plantname);
                        hashMap.put("water", water);
                        hashMap.put("place", place);
                        hashMap.put("temp", temp);
                        hashMap.put("tip", tip);
                        hashMap.put("sp_dictionary",sp);

                        reference.child(plantname).setValue(hashMap);


                        pd.dismiss();
                        finish();
                    }else{
                        Toast.makeText(EditDictionaryActivity.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditDictionaryActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(EditDictionaryActivity.this,"No image selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            mImageUri = data.getData();
            iv_profile.setImageURI(mImageUri);


        }else{
            Toast.makeText(EditDictionaryActivity.this,"Something gone wrong",Toast.LENGTH_SHORT).show();
        }
    }
}
