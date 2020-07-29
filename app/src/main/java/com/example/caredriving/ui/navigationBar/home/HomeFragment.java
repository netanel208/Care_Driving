package com.example.caredriving.ui.navigationBar.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caredriving.MainActivity;
import com.example.caredriving.R;
import com.example.caredriving.firebase.model.FirebaseDBUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {


    private ArrayList<String> lessonsId = new ArrayList<>();
    private ArrayList<ArrayList<String>> Check = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private static RecyclerView recyclerView;
    private RecyclerViewAdapterAllDates adapter;
    private static String userUid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        dates.clear();
        lessonsId.clear();
        Check.clear();
        downloadInfoFromDatabase(root);

        return root;
    }

    private void downloadInfoFromDatabase(final View root){
        FirebaseDBUser currentUser = new FirebaseDBUser();
        userUid = currentUser.getMyUid();
        DatabaseReference reference = currentUser.getUserRefFromDB();
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("type").getValue().equals("student")){
                    downloadLessonsId(root, "students");
                }
                else if (dataSnapshot.child("type").getValue().equals("teacher")){
                    downloadLessonsId(root, "teachers");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(root.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void downloadLessonsId(final View root, final String userType){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("lessonsPerUser").child(userType).child(userUid);
        final Date currentDay = new Date();
        final Calendar calendar = Calendar.getInstance();
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotl : dataSnapshot.getChildren()) {
                    String year = "";
                    year = year + dataSnapshotl.getKey();
                    for (DataSnapshot dataSnapshotlchild : dataSnapshotl.getChildren()) {
                        String month = "";
                        month =  month + dataSnapshotlchild.getKey();
                        if (month.length()==1){
                            month = "0" + month;
                        }
                        for (DataSnapshot dataSnapshotlgrandsaon :dataSnapshotlchild.getChildren()) {
                            String day = "";
                            day = day + dataSnapshotlgrandsaon.getKey();
                            if (day.length()==1){
                                day = "0" + day;
                            }
                            calendar.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
                            Date validDate = calendar.getTime();
                            if (currentDay.equals(validDate) || currentDay.before(validDate)) {
                                for (DataSnapshot data :dataSnapshotlgrandsaon.getChildren()) {
                                    String date = day + "/" + month + "/" + year;
                                    if (!dates.contains(date)) {
                                        dates.add(date);
                                    }
                                    lessonsId.add(data.getValue().toString());
                                }
                                ArrayList<String> testsSentOrig = new ArrayList<String>(lessonsId);
                                Check.add(testsSentOrig);
                                lessonsId.clear();
                            }
                        }
                    }
                }

                recyclerView = root.findViewById(R.id.recyclerviewAllLessons);
                LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(horizontalLayoutManagaer);
                adapter = new RecyclerViewAdapterAllDates(root.getContext(), dates, userType, Check);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(root.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }



//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        ((MainActivity)getActivity()).clearBackStackInclusive("tag"); // tag (addToBackStack tag) should be the same which was used while transacting the F2 fragment
//    }


}