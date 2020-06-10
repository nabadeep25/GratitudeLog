package com.nabadeep.gratitudelog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import Utility.LogApi;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth=FirebaseAuth.getInstance();

   authStateListener=new FirebaseAuth.AuthStateListener() {
       @Override
       public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
           currentuser= firebaseAuth.getCurrentUser();
           if(currentuser!=null){
               String currentUserid=currentuser.getUid();
               collectionReference.whereEqualTo("UserId",currentUserid).addSnapshotListener(new EventListener<QuerySnapshot>() {
                   @Override
                   public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                       if(e!=null)
                           return;

                       if(!queryDocumentSnapshots.isEmpty()){
                           LogApi logApi=LogApi.getInstance();
                           for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                               logApi.setUserName(snapshot.getString("username"));
                               logApi.setUserId(snapshot.getString("UserId"));

                               startActivity(new Intent(MainActivity.this,ListLogs.class));

                               finish();
                           }
                       }
                   }
               });
           }
       }
   };
        startButton=findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(currentuser!=null)
            firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
