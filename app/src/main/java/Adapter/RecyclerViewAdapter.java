package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nabadeep.gratitudelog.ListLogs;
import com.nabadeep.gratitudelog.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Gratitudemodel;

import static android.content.ContentValues.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Gratitudemodel> logList;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("GratitudeLog");

  private AlertDialog alertDialog;
  private AlertDialog.Builder builder;
  private LayoutInflater layoutInflater;

    public RecyclerViewAdapter(Context context, List<Gratitudemodel> logList) {
        this.context = context;
        this.logList = logList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.log_item,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, final int position) {
    final Gratitudemodel gratitude=logList.get(position);
        Log.d(TAG, "onBindViewHolder: "+gratitude);

 holder.heading.setText(gratitude.getTitle());
    holder.body.setText(gratitude.getBody());

        String date= (String) DateFormat.format("hh:mm a dd-mm-yyyy",gratitude.getCreatedAt().getSeconds()*1000);
        Log.d(TAG, "onBindViewHolder: "+date);
        holder.imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
  collectionReference.whereEqualTo("createdAt",logList.get(position).getCreatedAt()).addSnapshotListener(new EventListener<QuerySnapshot>() {
      @Override
      public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
          for (QueryDocumentSnapshot snap:queryDocumentSnapshots) {
              snap.getReference().delete();

          }
      }
  });
                notifyDataSetChanged();
            }

        });
      //
      holder.edit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
           updatelog(gratitude);

              notifyDataSetChanged();
          }
      });

    }

    private void updatelog(final Gratitudemodel gratitude) {
        builder=new AlertDialog.Builder(context);
        layoutInflater=LayoutInflater.from(context);
        View view1=layoutInflater.inflate(R.layout.edit_popup,null);
        Button updatebuttton;

        final EditText heading,description;
        updatebuttton=view1.findViewById(R.id.UpdateButton);
        heading=view1.findViewById(R.id.updateHeading);
        description=view1.findViewById(R.id.updateDescription);

        heading.setText(String.valueOf(gratitude.getTitle()));
        description.setText(String.valueOf(gratitude.getBody()));
        builder.setView(view1);
        alertDialog=builder.create();
        alertDialog.show();
        //start
        updatebuttton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                gratitude.setTitle(heading.getText().toString());
                gratitude.setBody(description.getText().toString());




                collectionReference.whereEqualTo("createdAt",gratitude.getCreatedAt()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (QueryDocumentSnapshot snap:queryDocumentSnapshots) {
                            //TODO
                            snap.getReference().update("title",gratitude.getTitle(),"body",gratitude.getBody()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(context,"Updated Sucessfully..",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                });
                alertDialog.dismiss();

            }
        });
        //end
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView heading,body,name,date;
        public ImageButton edit;
        public ImageButton imgbtn;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;

            heading=itemView.findViewById(R.id.log_header);
            body=itemView.findViewById(R.id.log_body);
            imgbtn=itemView.findViewById(R.id.deletebutton);
            edit=itemView.findViewById(R.id.editbutton);
        }
    }
}
