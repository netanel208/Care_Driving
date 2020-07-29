package com.example.caredriving.ui.navigationBar.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.caredriving.R;
import com.example.caredriving.firebase.model.FirebaseDBEntity;
import com.example.caredriving.firebase.model.dataObject.UserObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerViewAdapterLessonsToday extends RecyclerView.Adapter<RecyclerViewAdapterLessonsToday.ViewHolder> {

    private ArrayList<String> lessonsId = new ArrayList<>();
    private Context context;
    private String userType;


    public RecyclerViewAdapterLessonsToday(Context context, ArrayList<String> lessonsId, String userType){
        this.lessonsId = lessonsId;
        this.context = context;
        this.userType = userType;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterLessonsToday.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_lessons_today, parent, false);
        return new RecyclerViewAdapterLessonsToday.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterLessonsToday.ViewHolder holder, int position) {
        holder.tvFirstName.setVisibility(View.INVISIBLE);
        holder.tvPhone.setVisibility(View.INVISIBLE);
        holder.tvLastName.setVisibility(View.INVISIBLE);
        downloadInfoFromDatabase(holder, position);
    }

    @Override
    public int getItemCount() {
        return lessonsId.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvHour;
        TextView tvFirstName;
        TextView tvLastName;
        TextView tvPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHour = itemView.findViewById(R.id.tvHour);
            tvFirstName = itemView.findViewById(R.id.tvFirstName);
            tvLastName  = itemView.findViewById(R.id.tvLastName);
            tvPhone  = itemView.findViewById(R.id.tvPhone);
        }
    }

    private void downloadInfoFromDatabase(final ViewHolder holder, final int position){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("lessons");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotl : dataSnapshot.getChildren()){
                   if (lessonsId.get(position).equals(dataSnapshotl.getKey())){
                       holder.tvHour.setText(dataSnapshotl.child("lessonTime").getValue().toString());
                       if (userType.equals("students")) {
                           String id = dataSnapshotl.child("teacherId").getValue().toString();
                           downloadContactInfo(id, holder);
                       } else if (userType.equals("teachers")) {
                           String id = dataSnapshotl.child("studentId").getValue().toString();
                           downloadContactInfo(id, holder);
                       }
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void downloadContactInfo(String userId, final ViewHolder holder){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseDBEntity entity = dataSnapshot.getValue(FirebaseDBEntity.class);
                UserObj user = entity.getUserObj();
                holder.tvFirstName.setVisibility(View.VISIBLE);
                holder.tvPhone.setVisibility(View.VISIBLE);
                holder.tvLastName.setVisibility(View.VISIBLE);
                holder.tvFirstName.setText( user.getFirstName());
                holder.tvLastName.setText( user.getLastName());
                holder.tvPhone.setText( user.getPhoneNumber());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
