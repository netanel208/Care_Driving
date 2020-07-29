package com.example.caredriving.ui.navigationBar.students;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.caredriving.R;
import com.example.caredriving.firebase.model.FirebaseDBEntity;
import com.example.caredriving.firebase.model.dataObject.StudentObj;
import com.example.caredriving.firebase.model.dataObject.UserObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyStudentsFragment extends Fragment {

    RecyclerView recyclerView;
    TextView txtNoTeacher;
    private RecyclerViewAdapterStudents adapter;
    private static ArrayList<StudentObj> students = new ArrayList<>();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private UserObj user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_students, container, false);
        recyclerView = root.findViewById(R.id.recyclerviewStudents);
        txtNoTeacher =root.findViewById(R.id.txtNoTeacher);
        students.clear();

        //recyclerView.setVisibility(View.INVISIBLE);
        txtNoTeacher.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        downloadInfoFromDatabase(root);

        return root;
    }

    private void downloadInfoFromDatabase(final View root){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot fb_entity : dataSnapshot.getChildren()){
                    FirebaseDBEntity entity = fb_entity.getValue(FirebaseDBEntity.class);
                    user = entity.getUserObj();
                    if(user instanceof StudentObj && currentUser.getUid().equals(((StudentObj)user).getTeacherId())) {
                        students.add((StudentObj) user);
                    }
                }
                if(students.isEmpty()){
                    txtNoTeacher.setVisibility(View.VISIBLE);
                }
                adapter = new RecyclerViewAdapterStudents(getActivity(), students);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(root.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}