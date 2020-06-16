package com.nabadeep.gratitudelog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Date;

import Utility.LogApi;
import model.Gratitudemodel;

public class AddLog extends AppCompatActivity implements View.OnClickListener {
private Button saveButton;
private EditText heading;
private EditText gratitudeText;
private TextView time;
private TextView userName;





private String userid;
private String username;

private FirebaseAuth firebaseAuth;
private  FirebaseAuth.AuthStateListener authStateListener;
private FirebaseUser user;

private FirebaseFirestore db=FirebaseFirestore.getInstance();
private CollectionReference collectionReference=db.collection("GratitudeLog");

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);
       firebaseAuth=FirebaseAuth.getInstance();

      getSupportActionBar().setElevation(0);

       saveButton=findViewById(R.id.saveLogButton);
       heading=findViewById(R.id.logHeading);
       gratitudeText=findViewById(R.id.logDescription);
       time=findViewById(R.id.showtime);
       userName=findViewById(R.id.showusername);





     gratitudeText.setScroller(new Scroller(AddLog.this));
      gratitudeText.setMaxLines(2);
      gratitudeText.setVerticalScrollBarEnabled(true);
      gratitudeText.setMovementMethod(new ScrollingMovementMethod());




       saveButton.setOnClickListener(this);
       if(LogApi.getInstance()!=null){
           userid=LogApi.getInstance().getUserId();
           username=LogApi.getInstance().getUserName();
           userName.setText(username);
       }
       authStateListener= new FirebaseAuth.AuthStateListener() {
           @Override
           public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
         user=firebaseAuth.getCurrentUser();
         if(user!=null){

         }else{
             //
         }
           }
       };

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){




            case R.id.saveLogButton:
                saveLogs();
                break;
        }
    }

    private void saveLogs() {


      final String gHeading=heading.getText().toString().trim();
      final String gBody=gratitudeText.getText().toString().trim();
      if(!TextUtils.isEmpty(gHeading) && !TextUtils.isEmpty(gBody)){
          savetodb(gHeading,gBody);
      }else{
          Toast.makeText(this,"Empty Fields",Toast.LENGTH_LONG).show();
      }
    }

    private void savetodb(String gHeading, String gBody) {
        Gratitudemodel GM=new Gratitudemodel();
        GM.setTitle(gHeading);
        GM.setBody(gBody);
        GM.setUserId(userid);
        //error with name
        GM.setUseName(username);



        GM.setCreatedAt(new Timestamp(new Date()));

        collectionReference.add(GM).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                startActivity(new Intent(AddLog.this,ListLogs.class));



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ADDLOG", "onFailure: "+e.getMessage());
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        user=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
