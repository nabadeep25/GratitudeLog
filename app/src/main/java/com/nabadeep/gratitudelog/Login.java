package com.nabadeep.gratitudelog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import Utility.LogApi;

public class Login extends AppCompatActivity {
private Button SignupLink;
private Button LogIn;
private AutoCompleteTextView Email;
private EditText Password;

    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        firebaseAuth=FirebaseAuth.getInstance();

        LogIn=findViewById(R.id.login);
        Email=findViewById(R.id.email);
        Password=findViewById(R.id.password);
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=Email.getText().toString().trim();
                String pass=Password.getText().toString().trim();
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
                loginToFirebase(email,pass);
                }else{
                    Toast.makeText(Login.this,"Empty Field ",Toast.LENGTH_SHORT).show();
                }
            }
        });
        SignupLink=findViewById(R.id.signuplink);
        SignupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,SignUp.class));
            }
        });
    }

    private void loginToFirebase(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {



               if(task.isSuccessful()){
                   FirebaseUser user=firebaseAuth.getCurrentUser();
                   String currentUserId=user.getUid();
                   collectionReference.whereEqualTo("UserId",currentUserId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                       @Override
                       public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                           if(!queryDocumentSnapshots.isEmpty()){
                               LogApi logApi=LogApi.getInstance();
                               for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                                   logApi.setUserName(snapshot.getString("username"));
                                   logApi.setUserId(snapshot.getString("UserId"));
                                   startActivity(new Intent(Login.this,ListLogs.class));
                                   //todo

                                   finish();
                               }
                           }
                       }
                   });
               }else{
                   Toast.makeText(Login.this,"Failed to login",Toast.LENGTH_SHORT).show();
               }


            }
        });
    }
}
