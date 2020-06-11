package Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.List;

import model.Gratitudemodel;

import static android.content.ContentValues.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Gratitudemodel> logList;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("GratitudeLog");

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
    Gratitudemodel gratitude=logList.get(position);
        Log.d(TAG, "onBindViewHolder: "+gratitude);
    String url=gratitude.getImageUrl();
 holder.heading.setText(gratitude.getTitle());
    holder.body.setText(gratitude.getBody());
        Picasso.get().load(url).placeholder(R.drawable.defaultpicture).fit().into(holder.img);
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


            }


        });
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView heading,body,name,date;
        public ImageView img;
        public ImageButton imgbtn;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            img=itemView.findViewById(R.id.log_image);
            heading=itemView.findViewById(R.id.log_header);
            body=itemView.findViewById(R.id.log_body);
            imgbtn=itemView.findViewById(R.id.deletebutton);
        }
    }
}
