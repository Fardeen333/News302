package com.creative.news302.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.news302.R;
import com.creative.news302.models.model_news;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class addData extends AppCompatActivity {

    ProgressBar pb;
    TextView tv;

    private DatabaseReference userDatabaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ImageView rl_photo;
    Uri profileImage = null;
    boolean photouploaded = false;


    EditText edTitle, edBrief, edDescription;
    EditText edTitleH, edBriefH, edDescriptionH;
    String title, brief, description, imgurl;
    String titleH, briefH, descriptionH;
    Button b_add;
    model_news model_newsx;
    Spinner spincat;
    long childcount;
    String selectedcat;
    boolean isDoing = false;
    ValueEventListener eventListener;
    long number = 100000000;
    String[] categories = {"Please Select Category","National", "International", "Sports", "Entertainment", "Technology", "Politics", "Economy", "Editorial", "Current Affairs"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        rl_photo = findViewById(R.id.imvupload);

        pb = findViewById(R.id.pb);
        tv = findViewById(R.id.tv);
        pb.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);

        edTitle = findViewById(R.id.edTitle);
        edBrief = findViewById(R.id.edBrief);
        edDescription = findViewById(R.id.edDescription);

        edTitleH = findViewById(R.id.edTitleh);
        edBriefH = findViewById(R.id.edBriefh);
        edDescriptionH = findViewById(R.id.edDescriptionh);




        b_add = findViewById(R.id.b_add);
        spincat = findViewById(R.id.spinner1);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spincat.setAdapter(dataAdapter);

        spincat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selectedcat = categories[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        userDatabaseReference = FirebaseDatabase.getInstance().getReference();


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv.getText()=="Click Here to Retry"){
                    UploadPic1();
                }
                else if(tv.getText()=="Retrieve URL"){
                    GetDownloadURL1();
                }
            }
        });
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childcount = dataSnapshot.getChildrenCount();
                SelectImage();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photouploaded) {

                    if(!checkfordata()){
                        return;
                    }
                    if(Objects.equals(selectedcat, "Please Select Category")){
                        Toast.makeText(addData.this, "Please Select Category", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    title = edTitle.getText().toString();
                    brief = edBrief.getText().toString();
                    description = edDescription.getText().toString();

                    titleH = edTitleH.getText().toString();
                    briefH = edBriefH.getText().toString();
                    descriptionH = edDescriptionH.getText().toString();
                    //imgurl = edImgurl.getText().toString();


                    isDoing = true;
                    pushvalue();
                    b_add.setText("Adding");
                    b_add.setEnabled(false);

                }
                else {
                    Toast.makeText(addData.this, "Please Upload photo first", Toast.LENGTH_SHORT).show();
                }
            }
        });


        rl_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getchildcount2();

            }
        });
    }

    private boolean checkfordata() {
        boolean isCorrect = true;
        if(edTitle.length()==0){
            edTitle.setError("Please Fill the data");
            edTitle.requestFocus();
            isCorrect = false;
        }
        if(edTitleH.length()==0){
            edTitleH.setError("Please Fill the data");
            edTitleH.requestFocus();
            isCorrect = false;
        }
        if(edDescription.length()==0){
            edDescription.setError("Please Fill the data");
            edDescription.requestFocus();
            isCorrect = false;
        }
        if(edDescriptionH.length()==0){
            edDescriptionH.setError("Please Fill the data");
            edDescriptionH.requestFocus();
            isCorrect = false;
        }
        if(edBrief.length()==0){
            edBrief.setError("Please Fill the data");
            edBrief.requestFocus();
            isCorrect = false;
        }
        if(edBriefH.length()==0){
            edBriefH.setError("Please Fill the data");
            edBriefH.requestFocus();
            isCorrect = false;
        }
        return isCorrect;
    }

    private void SelectImage() {
        userDatabaseReference.removeEventListener(eventListener);
        profileImage = null;
         pb.setVisibility(View.VISIBLE);
         tv.setVisibility(View.VISIBLE);
        CropImage.ActivityBuilder cropImage = CropImage.activity(profileImage);
        cropImage.setAspectRatio(2, 1);
        cropImage.setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);


    }

    private void pushvalue() {

        model_newsx = new model_news(title, brief, description, imgurl, selectedcat, number - childcount,titleH,briefH,descriptionH);
        userDatabaseReference.child("news").child(String.valueOf(number - childcount)).setValue(model_newsx).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    showvalue();

                    userDatabaseReference.child("notification").setValue(edTitle.getText().toString());
                    isDoing = false;
                    b_add.setText("Add");
                    b_add.setEnabled(true);
                    profileImage = null;
                    edTitle.setText("");
                    edTitleH.setText("");

                    edBrief.setText("");
                    edBriefH.setText("");

                    edDescription.setText("");
                    edDescriptionH.setText("");
                    tv.setVisibility(View.GONE);
                    Toast.makeText(addData.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                    rl_photo.setImageDrawable(getResources().getDrawable(R.drawable.button_back_ripple_round_grey));

                } else {
                    isDoing = false;
                    b_add.setText("Retry");
                    b_add.setEnabled(true);
                }


            }
        });
    }

    private void showvalue() {

    }

//    private void getchildcount() {
//        if (isDoing) {
//            userDatabaseReference.child("news").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (isDoing) {
//                        isDoing = false;
//                        childcount = dataSnapshot.getChildrenCount();
//                        pushvalue();
//
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    isDoing = false;
//                    b_add.setText("retry");
//                    b_add.setEnabled(true);
//
//                }
//            });
//
//        }
//
//
//    }

    private void getchildcount2() {
        userDatabaseReference.child("news").addListenerForSingleValueEvent(eventListener);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (result == null) {
                 pb.setVisibility(View.GONE);
                 tv.setVisibility(View.GONE);
                Toast.makeText(this, "Error! retry", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_OK) {
                profileImage = result.getUri();
                UploadPic1();

            }

        }else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
            pb.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            Toast.makeText(this, "Error! retry", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadPic1() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("news").child((number - childcount) + ".jpg");
        tv.setText("UPLOADING...");
        rl_photo.setDrawingCacheEnabled(true);
        rl_photo.buildDrawingCache();
        rl_photo.setImageURI(profileImage);
        //Bitmap bitmap = ((BitmapDrawable) rl_photo.getDrawable()).getBitmap();
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
       // byte[] datai = baos.toByteArray();
        UploadTask uploadTask = storageReference.putFile(profileImage);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
               // double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
                Toast.makeText(addData.this, (int)progress+" % uploaded", Toast.LENGTH_SHORT).show();
            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                pb.setVisibility(View.GONE);
                tv.setText("Click Here to Retry");
                Toast.makeText(addData.this, "error uploading", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //tv1.setText("Uploaded");
                GetDownloadURL1();
                pb.setVisibility(View.GONE);
                tv.setText("Retrieve URL");
                Toast.makeText(addData.this, "uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetDownloadURL1() {
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                imgurl = "https://firebasestorage.googleapis.com" + uri.getEncodedPath() + "?" + uri.getQuery();
                Log.d("imageee", imgurl);
                tv.setText("Done");
                pb.setVisibility(View.GONE);
                photouploaded = true;
                //rl_photo.setImageURI(profileImage);
                //Picasso.get().load(imagelink1).into(imv2);
            }






        });
    }

}


