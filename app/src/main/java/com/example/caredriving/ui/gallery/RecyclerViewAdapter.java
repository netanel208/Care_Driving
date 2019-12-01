package com.example.caredriving.ui.gallery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.caredriving.R;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> teachersNames = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<String> teachersNames) {
        this.teachersNames = teachersNames;
        this.context = context;
    }

    // Recycling the ViewHolder - put things into position
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // Called every single time when a new item is added to the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.tvTeachersName.setText(teachersNames.get(position));

        //OnClickListener to each item
        holder.layoutTeachersDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked." + teachersNames.get(position));
                Toast.makeText(context, teachersNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Tells how many items are on the list
    @Override
    public int getItemCount() {
        return teachersNames.size();
    }

    // Holds the information in memory of each individual entry
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTeachersName;
        RelativeLayout layoutTeachersDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeachersName = itemView.findViewById(R.id.tvTeachersName);
            layoutTeachersDetails = itemView.findViewById(R.id.layoutTeachersDetails);
        }
    }

}
