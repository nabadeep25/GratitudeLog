package com.nabadeep.gratitudelog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import Adapter.RecyclerViewAdapter;

import Utility.LogApi;
import model.Gratitudemodel;

public class ListLogs extends AppCompatActivity {
    private static final String TAG ="List Log" ;
    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private StorageReference storageReference;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("GratitudeLog");


    private List<Gratitudemodel> list;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private TextView empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_logs);

        getSupportActionBar().setElevation(0);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        empty=findViewById(R.id.EmptyList);
        list=new ArrayList<>();
        Log.d(TAG, "onCreate: ");

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);



    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.whereEqualTo("userId", LogApi.getInstance().getUserId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {


            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "onSuccess: "+queryDocumentSnapshots.isEmpty());

                if(!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot snapshots:queryDocumentSnapshots){
                        Gratitudemodel model=snapshots.toObject(Gratitudemodel.class);
                        list.add(model);
                    }
                    Log.d(TAG, "List: "+list);
                    recyclerViewAdapter=new RecyclerViewAdapter(ListLogs.this,list);


                    Log.d(TAG, "onSuccess2: "+list);
                    recyclerView.setAdapter(recyclerViewAdapter);
                   updatelist();
                    Log.d(TAG, "onSuccess:afer");

                }else{
                    empty.setVisibility(View.VISIBLE);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                if(user!=null && firebaseAuth!=null)
                    startActivity(new Intent(ListLogs.this,AddLog.class));
                //finish();
                break;
            case R.id.action_signout:
                if(user!=null && firebaseAuth!=null)
                    firebaseAuth.signOut();
                startActivity(new Intent(ListLogs.this,MainActivity.class));
                //finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //
    public void updatelist(){
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
