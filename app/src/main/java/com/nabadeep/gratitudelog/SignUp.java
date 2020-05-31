package com.nabadeep.gratitudelog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
  private   FirebaseAuth firebaseAuth;
 private  FirebaseAuth.AuthStateListener authStateListener;
 private FirebaseUser currentuser;
 private FirebaseFirestore db=FirebaseFirestore.getInstance();
 private CollectionReference collectionReference=db.collection("Users");

 private ProgressBar progressBar;
 private Button sigunup;
 private Button login;
 private AutoCompleteTextView email;
 private EditText password;
 private EditText Username;

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth=FirebaseAuth.getInstance();


        progressBar=findViewById(R.id.sprogressBar);
        sigunup=findViewById(R.id.SignUp);
        login=findViewById(R.id.loginlink);
        email=findViewById(R.id.semail);
        Username=findViewById(R.id.user);
        password=findViewById(R.id.spassword);

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            currentuser=firebaseAuth.getCurrentUser();
            if(currentuser!=null){
               //log
            }else{
                //reg
            }
            }
        };
        sigunup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String femail = email.getText().toString().trim();
                    String fpass = password.getText().toString().trim();
                    String fuser = Username.getText().toString().trim();
                if(!TextUtils.isEmpty(femail) && !TextUtils.isEmpty(fpass) &&
                        !TextUtils.isEmpty(fuser)) {
                    createUserAccount(femail, fpass, fuser);
                }
            }
        });

    }

    private void createUserAccount(String email, String password, final String username) {
     if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) &&
             !TextUtils.isEmpty(username)){
         progressBar.setVisibility(View.VISIBLE);
       firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
            currentuser=firebaseAuth.getCurrentUser();
            String UserId=currentuser.getUid();
                   Map<String,String> userObj=new HashMap<>();
                   userObj.put("User Id",UserId);
                   userObj.put("user name",username);
                   collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                       @Override
                       public void onSuccess(DocumentReference documentReference) {
//comlete



                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {

                       }
                   });

               }else{
                   //
               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

           }
       });
     }else{

     }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
