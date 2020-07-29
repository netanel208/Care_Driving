package com.example.caredriving.ui.navigationBar.home;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caredriving.R;
import com.example.caredriving.firebase.model.FirebaseDBEntity;
import com.example.caredriving.firebase.model.FirebaseDBUser;
import com.example.caredriving.firebase.model.dataObject.StudentObj;
import com.example.caredriving.firebase.model.dataObject.UserObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewAdapterAllDates extends RecyclerView.Adapter<RecyclerViewAdapterAllDates.ViewHolder> {

    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<ArrayList<String>> lessonsId = new ArrayList<>();
    private String userType;
    private Context context;

    public RecyclerViewAdapterAllDates(Context context, ArrayList<String> dates, String userType, ArrayList<ArrayList<String>> lessonsId){
        this.dates = dates;
        this.lessonsId = lessonsId;
        this.userType = userType;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_lessons, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.tvDate.setText(dates.get(position));
        ArrayList<String> lessonId = new ArrayList<>(lessonsId.get(position));
        RecyclerViewAdapterLessonsToday adapter = new RecyclerViewAdapterLessonsToday(context, lessonId, userType);
        holder.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        holder.imgArrowRight.setVisibility(View.VISIBLE);
        holder.imgArrowLeft.setVisibility(View.VISIBLE);
        if(position == dates.size()-1){
            holder.imgArrowRight.setVisibility(View.INVISIBLE);
        }
        if(position == 0){
            holder.imgArrowLeft.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvDate;
        ImageView imgArrowRight;
        ImageView imgArrowLeft;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgArrowRight = itemView.findViewById(R.id.imgArrowRight);
            imgArrowLeft  = itemView.findViewById(R.id.imgArrowLeft);
            recyclerView = itemView.findViewById(R.id.recyclerviewLessonsToday);
        }
    }


}
