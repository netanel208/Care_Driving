package com.example.caredriving.ui.navigationBar.searchTeachers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.caredriving.R;
import com.example.caredriving.firebase.model.dataObject.TeacherObj;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<TeacherObj> teachers = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<TeacherObj> teachers) {
        this.context = context;
        this.teachers = teachers;
    }

    // Recycling the ViewHolder - put things into position
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_teachers_search, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // Called every single time when a new item is added to the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.tvTeachersFirstName.setText(teachers.get(position).getFirstName());
        holder.tvTeachersLastName.setText(teachers.get(position).getLastName());
        holder.tvLocation.setText(teachers.get(position).getCity());
        holder.tvGearType.setText(teachers.get(position).getTransmission());
        holder.tvPhoneNumber.setText(teachers.get(position).getPhoneNumber());
        holder.tvPrice.setText(teachers.get(position).getLessonPrice());
        holder.tvTeachersFirstName.setText(teachers.get(position).getFirstName());
    }

    // Tells how many items are on the list
    @Override
    public int getItemCount() {
        return teachers.size();
    }


    //////////////////////////////////////////////////////////////
    // Holds the information in memory of each individual entry //
    //////////////////////////////////////////////////////////////
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTeachersFirstName;
        TextView tvTeachersLastName;
        TextView tvLocation;
        TextView tvGearType;
        TextView tvPhoneNumber;
        TextView tvPrice;
        RelativeLayout layoutTeachersDetails;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvTeachersFirstName = itemView.findViewById(R.id.tvTeachersFirstName);
            tvTeachersLastName = itemView.findViewById(R.id.tvTeachersLastName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvGearType = itemView.findViewById(R.id.tvGearType);
            layoutTeachersDetails = itemView.findViewById(R.id.layoutTeachersDetails);

            // Send to the next activity the relevant information
            layoutTeachersDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClickListener: called.");
                    int position = getAdapterPosition();
                    Intent intent = new Intent(itemView.getContext(), ContactTeacherActivity.class);
                    intent.putExtra("TeachersUid", teachers.get(position).getId());
                    intent.putExtra("TeachersFirstName", teachers.get(position).getFirstName());
                    intent.putExtra("TeachersLastName", teachers.get(position).getLastName());
                    intent.putExtra("TeachersExperience", teachers.get(position).getExperience());
                    intent.putExtra("TeachersLocation", teachers.get(position).getCity());
                    intent.putExtra("TeachersCarType", teachers.get(position).getCarType());
                    intent.putExtra("TeachersCarYear", teachers.get(position).getCarYear());
                    intent.putExtra("TeachersGearType", teachers.get(position).getTransmission());
                    intent.putExtra("TeachersLessonPrice", teachers.get(position).getLessonPrice());
                    intent.putExtra("TeachersPhoneNumber", teachers.get(position).getPhoneNumber());
                    intent.putExtra("TeachersEmail", teachers.get(position).getEmail());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
