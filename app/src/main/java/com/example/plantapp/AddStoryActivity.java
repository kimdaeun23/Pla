package com.example.plantapp;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

public class AddStoryActivity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;

    private Uri mImageUri;
    String myUrl="";
    private StorageTask storageTask;
    StorageReference storageReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        storageReference= FirebaseStorage.getInstance().getReference("story");

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);

    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void publishStory(){
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Posting");
        pd.show();

        if (mImageUri!=null){
            StorageReference imageReference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));

            storageTask=imageReference.putFile(mImageUri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloaddUri=task.getResult();
                        myUrl=downloaddUri.toString();

                        String myid= FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Story")
                                .child(myid);

                        String storyid=reference.push().getKey();
                        long timeend=System.currentTimeMillis()+86400000;

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("imageurl",myUrl);
                        hashMap.put("timestart", ServerValue.TIMESTAMP);
                        hashMap.put("timeend",timeend);
                        hashMap.put("storyid",storyid);
                        hashMap.put("userid",myid);

                        reference.child(storyid).setValue(hashMap);
                        pd.dismiss();
                        finish();
                    }else{
                        Toast.makeText(AddStoryActivity.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddStoryActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(AddStoryActivity.this,"NO image selected",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            mImageUri = data.getData();
            publishStory();

        }
        else {
            Toast.makeText(this, "something gone wrong", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddStoryActivity.this,BottomActivity.class));
            finish();
        }
    }
}
