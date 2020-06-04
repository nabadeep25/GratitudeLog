package Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nabadeep.gratitudelog.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import model.Gratitudemodel;

import static android.content.ContentValues.TAG;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.ViewHolder> {
    private Context context;
    private List<Gratitudemodel> logList;

    public RecylerViewAdapter(Context context, List<Gratitudemodel> logList) {
        this.context = context;
        this.logList = logList;
    }

    @NonNull
    @Override
    public RecylerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.log_item,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerViewAdapter.ViewHolder holder, int position) {
    Gratitudemodel gratitude=logList.get(position);
    String url=gratitude.getImageUrl();
    holder.heading.setText(gratitude.getTitle());
    holder.body.setText(gratitude.getBody());
        Picasso.get().load(url).placeholder(R.drawable.defaultpicture).fit().into(holder.img);
        String date= (String) DateFormat.format("hh:mm a dd-mm-yyyy",gratitude.getCreatedAt().getSeconds()*1000);
        Log.d(TAG, "onBindViewHolder: "+date);
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView heading,body,name,date;
        public ImageView img;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            img=itemView.findViewById(R.id.log_image);
            heading=itemView.findViewById(R.id.logHeading);
            body=itemView.findViewById(R.id.log_body);
        }
    }
}
