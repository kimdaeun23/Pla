package com.example.plantapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import org.w3c.dom.Text;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    Uri imageUri;
    String myUri="";
    StorageTask uploadTask;
    StorageReference storageReference;
    ImageView close,image_added;
    TextView post;
    EditText description;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        close=findViewById(R.id.close);
        image_added=findViewById(R.id.image_added);
        post=findViewById(R.id.post);
        description=findViewById(R.id.description);

        storageReference= FirebaseStorage.getInstance().getReference("posts");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this,BottomActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        image_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        if(imageUri!=null){
            StorageReference filerefrence=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

            uploadTask=filerefrence.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filerefrence.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        myUri=downloadUri.toString();

                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts");

                        String postid=reference.push().getKey();

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("postid",postid);
                        hashMap.put("postimage",myUri);
                        hashMap.put("description",description.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(postid).setValue(hashMap);

                        db.collection("users")
                                .document(firebaseAuth.getCurrentUser().getUid())
                                .collection("posts")
                                .add(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                    }
                                });


                        progressDialog.dismiss();
                        startActivity(new Intent(PostActivity.this,BottomActivity.class));
                        finish();

                    }else{
                        Toast.makeText(PostActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{

            Toast.makeText(PostActivity.this, "NO Image Selected", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();
            image_added.setImageURI(imageUri);

        }
        else {
            Toast.makeText(this, "something gone wrong", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this,BottomActivity.class));
            finish();
        }
    }
}
