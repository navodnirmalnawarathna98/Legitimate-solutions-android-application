package com.example.milestone06;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddPostActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    DatabaseReference userDbRef;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    private static final int IMAGE_PICK_CAMARA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;

    //permition array
    String[] camaraPermissions;
    String[] storagePermitions;


    EditText titleEt, descriptionEt;
    ImageView imageIv;
    Button uploadBtn;


    Uri image_rui = null;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        //init permition array
        camaraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermitions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();


        userDbRef = FirebaseDatabase.getInstance().getReference("Users");
//
//        Query query = userDbRef.orderByChild("email").equalTo(email);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        titleEt = findViewById(R.id.pTitleEt);
        descriptionEt = findViewById(R.id.pDescriptionEt);
        imageIv = findViewById(R.id.pImageIv);
        uploadBtn = findViewById(R.id.pUploadBtn);

        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = titleEt.getText().toString().trim();
                String description = descriptionEt.getText().toString().trim();
                if(TextUtils.isEmpty(title)){
                    Toast.makeText(AddPostActivity.this, "Enter Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(description)){
                    Toast.makeText(AddPostActivity.this, "Enter description..", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(image_rui == null){
                    uploadData(title, description, "noImage");
                }else{
                    uploadData(title, description, String.valueOf(image_rui));
                }

            }
        });
    }

    private void uploadData(String title, String description, String uri) {
        pd.setMessage("Publishing post...");
        pd.show();

        String timeStamp = String.valueOf(System.currentTimeMillis());

        String filePathAndName = "Posts/" + "Post_" + timeStamp;

        if(!uri.equals("noImage")){
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());

                            String downloadUri = uriTask.getResult().toString();

                            if(uriTask.isSuccessful()){
                                HashMap<Object, String> hashMap = new HashMap<>();

//                                hashMap.put("uid", uid);
//                                hashMap.put("uName", name);
//                                hashMap.put("uEmail", email);
//                                hashMap.put("uDp", dp);
                                hashMap.put("pId", timeStamp);
                                hashMap.put("pDescr", description);
                                hashMap.put("pImage", downloadUri);
                                hashMap.put("pTime", timeStamp);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

                                ref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                pd.dismiss();
                                                Toast.makeText(AddPostActivity.this, "Post published", Toast.LENGTH_SHORT).show();
                                                titleEt.setText("");
                                                descriptionEt.setText("");
                                                imageIv.setImageURI(null);
                                                image_rui = null;
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


                                            }
                                        });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }else{
            //post with out image
            HashMap<Object, String> hashMap = new HashMap<>();

//                                hashMap.put("uid", uid);
//                                hashMap.put("uName", name);
//                                hashMap.put("uEmail", email);
//                                hashMap.put("uDp", dp);
            hashMap.put("pId", timeStamp);
            hashMap.put("pDescr", description);
            hashMap.put("pImage", "noImage");
            hashMap.put("pTime", timeStamp);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

            ref.child(timeStamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            pd.dismiss();
                            Toast.makeText(AddPostActivity.this, "Post published", Toast.LENGTH_SHORT).show();

                            titleEt.setText("");
                            descriptionEt.setText("");
                            imageIv.setImageURI(null);
                            image_rui = null;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    });

        }
    }

    private void showImagePickDialog() {
        String[] options = {"Camera","Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(which==0){
                    //camara clicked
                    if(!checkCameraPermition()){
                        requestCameraPermition();
                    }else{
                        pickFromCamara();
                    }

                }
                if(which==1){
                    //gallery clicked
                    if(!checkCameraPermition()){
                        requestCameraPermition();
                    }else{
                        pickFromGallery();
                    }

                }
            }
        });
        //create and dialog
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamara() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Pick");
        image_rui = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui);
        startActivityForResult(intent,IMAGE_PICK_CAMARA_CODE);
    }

    private boolean checkStrongePermition(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStrongePermition(){
        ActivityCompat.requestPermissions(this, storagePermitions, STORAGE_REQUEST_CODE);

    }

    private boolean checkCameraPermition(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermition(){
        ActivityCompat.requestPermissions(this, camaraPermissions, CAMERA_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickFromCamara();
                    }else{
                        Toast.makeText(this, "CAMARA & STORAGE BOTH PERMISSION ARE NECCESSARY", Toast.LENGTH_SHORT).show();
                    }

                }
                else{

                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, " STORAGE  PERMISSION  NECCESSARY", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                image_rui = data.getData();

                imageIv.setImageURI(image_rui);
            }else if (requestCode == IMAGE_PICK_CAMARA_CODE){
                imageIv.setImageURI(image_rui);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}