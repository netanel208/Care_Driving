package com.example.caredriving.ui.navigationBar.searchTeachers;

import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.caredriving.firebase.model.FirebaseDBEntity;
import com.example.caredriving.firebase.model.dataObject.TeacherObj;
import com.example.caredriving.firebase.model.dataObject.UserObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FilterTeachersList {
    private static ArrayList<String> ids = new ArrayList<>();
    private static String categoryLocation = "Location";
    private static String categoryTransmission = "Transmission";
    private static String categoryCarType = "Car Type";
    private static String categoryPrice = "Price";

    private static ArrayList <String> locationIds = new ArrayList<>();
    private static ArrayList <String> carTypeIds = new ArrayList<>();
    private static ArrayList <String> gearIds = new ArrayList<>();
    private static ArrayList <String> priceIds = new ArrayList<>();

    /**
     * Search for the relevant teachers information in the Database.
     * Filter the inforamation based on user choice
     * @param root fragment view
     * @param teachers arrayList of TeacherObj, contains all teaches information
     * @param newLocations contains all the locations the user choose to search
     * @param newCarBrands contains all the car brands the user choose to search
     * @param newGears contains all the gear types the user choose to search
     * @param newPrices contains all the prices the user choose to search
     * @param adapter add and set all the teachers in the Recyclerview
     */
    protected static void setNotificationInList(final View root, final ArrayList<TeacherObj> teachers, final ArrayList<String> newLocations,
                                             final ArrayList<String> newCarBrands, final ArrayList<String> newGears, final ArrayList<String> newPrices, final RecyclerViewAdapter adapter) {
        teachers.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Search Teachers");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locationIds.clear();
                carTypeIds.clear();
                gearIds.clear();
                priceIds.clear();

                // Get all the relevant teachers id form Search Teachers tree
                if (!newLocations.isEmpty()) {
                    for (DataSnapshot dataSnapshotl : dataSnapshot.child(categoryLocation).getChildren()) {
                        for (String object : newLocations) {
                            if (dataSnapshotl.getKey().equals(object)) {
                                for (DataSnapshot dataSnapshot1child : dataSnapshotl.getChildren()) {
                                    if (!locationIds.contains(dataSnapshot1child.getKey())) {
                                        locationIds.add(dataSnapshot1child.getKey());
                                    }
                                }
                            }
                        }
                    }
                }
                if (!newCarBrands.isEmpty()) {
                    for (DataSnapshot dataSnapshotl : dataSnapshot.child(categoryCarType).getChildren()) {
                        for (String object : newCarBrands) {
                            if (dataSnapshotl.getKey().equals(object)) {
                                for (DataSnapshot dataSnapshot1child : dataSnapshotl.getChildren()) {
                                    if (!carTypeIds.contains(dataSnapshot1child.getKey())) {
                                        carTypeIds.add(dataSnapshot1child.getKey());
                                    }
                                }
                            }
                        }
                    }
                }
                if (!newGears.isEmpty()) {
                    for (DataSnapshot dataSnapshotl : dataSnapshot.child(categoryTransmission).getChildren()) {
                        for (String object : newGears) {
                            if (dataSnapshotl.getKey().equals(object)) {
                                for (DataSnapshot dataSnapshot1child : dataSnapshotl.getChildren()) {
                                    if (!gearIds.contains(dataSnapshot1child.getKey())) {
                                        gearIds.add(dataSnapshot1child.getKey());
                                    }
                                }
                            }
                        }
                    }
                }
                if (!newPrices.isEmpty()) {
                    for (DataSnapshot dataSnapshotl : dataSnapshot.child(categoryPrice).getChildren()) {
                        for (String object : newPrices) {
                            String[] parts = object.split("\\–");
                            int lower = Integer.parseInt(parts[0]);
                            int upper = Integer.parseInt(parts[1]);
                            if (lower <= Integer.parseInt(dataSnapshotl.getKey()) && upper >= Integer.parseInt(dataSnapshotl.getKey())) {
                                for (DataSnapshot dataSnapshot1child : dataSnapshotl.getChildren()) {
                                    if (!priceIds.contains(dataSnapshot1child.getKey())) {
                                        priceIds.add(dataSnapshot1child.getKey());
                                    }
                                }
                            }
                        }
                    }
                }

                // filter the information based on user choice
                filterNotification(root, newLocations, newCarBrands, newGears, newPrices, dataSnapshot);

                // download all the information from the users tree
                downloadFilteredInfo(root, teachers, adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(root.getContext(), "Oops... something wrong happened", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Contains all 16 option of filter - based on user choice
     * @param root fragment view
     * @param newLocations contains all the locations the user choose to search
     * @param newCarBrands contains all the car brands the user choose to search
     * @param newGears contains all the gear types the user choose to search
     * @param newPrices contains all the prices the user choose to search
     * @param dataSnapshot the location of the information in the Database
     */
    private static void filterNotification(final View root, final ArrayList<String> newLocations, final ArrayList<String> newCarBrands,
                                           final ArrayList<String> newGears, final ArrayList<String> newPrices, DataSnapshot dataSnapshot){
        // 0 0 0 0
        if (newLocations.isEmpty() && newCarBrands.isEmpty() && newGears.isEmpty() && newPrices.isEmpty()) {
            for (DataSnapshot dataSnapshotl : dataSnapshot.child(categoryLocation).getChildren()) {
                for (DataSnapshot dataSnapshot1child : dataSnapshotl.getChildren()) {
                    if (!ids.contains(dataSnapshot1child.getKey())) {
                        ids.add(dataSnapshot1child.getKey());
                    }
                }
            }
        }
        // 0 0 0 1
        else if (newLocations.isEmpty() && newCarBrands.isEmpty() && newGears.isEmpty() && !newPrices.isEmpty()) {
            ids.addAll(priceIds);
        }
        // 0 0 1 0
        else if (newLocations.isEmpty() && newCarBrands.isEmpty() && !newGears.isEmpty() && newPrices.isEmpty()) {
            ids.addAll(gearIds);
        }
        // 0 0 1 1
        else if (newLocations.isEmpty() && newCarBrands.isEmpty() && !newGears.isEmpty() && !newPrices.isEmpty()) {
            for (String id: gearIds){
                if (priceIds.contains(id)){
                    ids.add(id);
                }
            }
        }
        // 0 1 0 0
        else if (newLocations.isEmpty() && !newCarBrands.isEmpty() && newGears.isEmpty() && newPrices.isEmpty()) {
            ids.addAll(carTypeIds);
        }
        // 0 1 0 1
        else if (newLocations.isEmpty() && !newCarBrands.isEmpty() && newGears.isEmpty() && !newPrices.isEmpty()) {
            for (String id: carTypeIds){
                if (priceIds.contains(id)){
                    ids.add(id);
                }
            }
        }
        // 0 1 1 0
        else if (newLocations.isEmpty() && !newCarBrands.isEmpty() && !newGears.isEmpty() && newPrices.isEmpty()) {
            for (String id: carTypeIds){
                if (gearIds.contains(id)){
                    ids.add(id);
                }
            }
        }
        // 0 1 1 1
        else if (newLocations.isEmpty() && !newCarBrands.isEmpty() && !newGears.isEmpty() && !newPrices.isEmpty()) {
            for (String id: carTypeIds){
                if (gearIds.contains(id) && priceIds.contains(id)){
                    ids.add(id);
                }
            }
        }
        // 1 0 0 0
        else if (!newLocations.isEmpty() && newCarBrands.isEmpty() && newGears.isEmpty() && newPrices.isEmpty()) {
            ids.addAll(locationIds);
        }
        // 1 0 0 1
        else if (!newLocations.isEmpty() && newCarBrands.isEmpty() && newGears.isEmpty() && !newPrices.isEmpty()) {
            for (String id: locationIds){
                if (priceIds.contains(id)){
                    ids.add(id);
                }
            }
        }
        // 1 0 1 0
        else if (!newLocations.isEmpty() && newCarBrands.isEmpty() && !newGears.isEmpty() && newPrices.isEmpty()) {
            for (String id: locationIds){
                if (gearIds.contains(id)){
                    ids.add(id);
                }
            }
        }
        // 1 0 1 1
        else if (!newLocations.isEmpty() && newCarBrands.isEmpty() && !newGears.isEmpty() && !newPrices.isEmpty()) {
            for (String id: locationIds){
                if (gearIds.contains(id) && priceIds.contains(id)){
                    ids.add(id);
                }
            }
        }
        // 1 1 0 0
        else if (!newLocations.isEmpty() && !newCarBrands.isEmpty() && newGears.isEmpty() && newPrices.isEmpty()) {
            for (String id : locationIds){
                if (carTypeIds.contains(id)){
                    ids.add(id);
                }
            }
        }
        // 1 1 0 1
        else if (!newLocations.isEmpty() && !newCarBrands.isEmpty() && newGears.isEmpty() && !newPrices.isEmpty()) {
            for (String id: locationIds){
                if (carTypeIds.contains(id) && priceIds.contains(id)){
                    ids.add(id);
                }
            }
        }
        // 1 1 1 0
        else if (!newLocations.isEmpty() && !newCarBrands.isEmpty() && !newGears.isEmpty() && newPrices.isEmpty()) {
            for (String id: locationIds){
                if (carTypeIds.contains(id) && gearIds.contains(id)){
                    ids.add(id);
                }
            }
        }
        // 1 1 1 1
        else if (!newLocations.isEmpty() && !newCarBrands.isEmpty() && !newGears.isEmpty() && !newPrices.isEmpty()) {
            for (String id: locationIds){
                if (carTypeIds.contains(id) && gearIds.contains(id) && priceIds.contains(id)){
                    ids.add(id);
                }
            }

        } else {
            Toast.makeText(root.getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Download all the information from the users tree - based on the filter of the function filterNotification
     * @param root fragment view
     * @param teachers arrayList of TeacherObj, contains all teaches information
     * @param adapter add and set all the teachers in the Recyclerview
     */
    private static void downloadFilteredInfo(final View root, final ArrayList<TeacherObj> teachers, final RecyclerViewAdapter adapter){
        teachers.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotl : dataSnapshot.getChildren()) {
                    for (String id : ids) {
                        if (dataSnapshotl.getKey().equals(id)) {
                            FirebaseDBEntity entity = dataSnapshotl.getValue(FirebaseDBEntity.class);
                            UserObj user = entity.getUserObj();
                            if (user instanceof TeacherObj) {
                                teachers.add((TeacherObj) user);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                ids.clear();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(root.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
