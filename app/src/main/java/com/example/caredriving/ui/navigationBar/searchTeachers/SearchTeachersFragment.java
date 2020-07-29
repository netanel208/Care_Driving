package com.example.caredriving.ui.navigationBar.searchTeachers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.caredriving.R;
import com.example.caredriving.firebase.model.FirebaseDBEntity;
import com.example.caredriving.firebase.model.dataObject.TeacherObj;
import com.example.caredriving.firebase.model.dataObject.UserObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SearchTeachersFragment extends Fragment {

    private static ArrayList<TeacherObj> teachers = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private static RecyclerView recyclerView;
    private UserObj user;

    // Locations Filters
    private String[] listLocations;
    private boolean[] checkedLocations;
    private ArrayList<Integer> usersLocations = new ArrayList<>();
    private static ArrayList<String> newLocations = new ArrayList<>();

    // Car Brands Filters
    private String[] listCarBrands;
    private boolean[] checkedCarBrands;
    private ArrayList<Integer> usersCarBrands = new ArrayList<>();
    private static ArrayList<String> newCarBrands = new ArrayList<>();

    // Gear Types Filters
    private String[] listGears;
    private boolean[] checkedGears;
    private ArrayList<Integer> usersGears = new ArrayList<>();
    private static ArrayList<String> newGears = new ArrayList<>();

    // Price Filters
    private String[] listPriceRange;
    private boolean[] checkedPriceRange;
    private ArrayList<Integer> usersPriceRange = new ArrayList<>();
    private static ArrayList<String> newPrices = new ArrayList<>();

    public static SearchTeachersFragment newInstance() {
        return new SearchTeachersFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search_teachers, container, false);
        recyclerView = root.findViewById(R.id.recyclerviewTeachers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        clearPreviousData();
        downloadInfoFromDatabase(root);

        // Cities Filters
        locationFilterDialog(root);
        // Car Brands Filters
        carTypeFilterDialog(root);
        // Gear Types Filters
        carGearFilterDialog(root);
        // Prices Filters
        priceFilterDialog(root);
        return root;
    }

    /**
     * Make sure that duplicates in the information no exist by cleaning all arrays
     */
    private void clearPreviousData(){
        teachers.clear();
        adapter = new RecyclerViewAdapter(getActivity(), teachers);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        usersLocations.clear();
        newLocations.clear();
        usersCarBrands.clear();
        newCarBrands.clear();
        usersGears.clear();
        newGears.clear();
        usersPriceRange.clear();
        newPrices.clear();
    }

    /**
     * Download the teacher's information from the Database
     * @param root fragment view
     */
    private void downloadInfoFromDatabase(final View root){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teachers.clear();
                for (DataSnapshot dataSnapshotl : dataSnapshot.getChildren()) {
                    FirebaseDBEntity entity = dataSnapshotl.getValue(FirebaseDBEntity.class);
                    user = entity.getUserObj();
                    if (user instanceof TeacherObj) {
                        teachers.add((TeacherObj)user);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(root.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Set the Dialog with the Cities checkbox's
     * @param root fragment view
     */
    private void locationFilterDialog(final View root){
        Button btnLocationsFilter = root.findViewById(R.id.btnLocationsFilter);
        // find all string of locations
        listLocations = getResources().getStringArray(R.array.cities);
        checkedLocations = new boolean[listLocations.length];
        // "Location" clicked
        btnLocationsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create dialog screen above this screen
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(root.getContext());
                mBuilder.setTitle(getString(R.string.title_location_filter));
                mBuilder.setMultiChoiceItems(listLocations, checkedLocations, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!usersLocations.contains(position)) {
                                usersLocations.add(position);
                            }
                        } else if (usersLocations.contains(position)) {
                            usersLocations.remove((Object) position);
                        }
                    }
                });
                // Cannot close the alert dialog after it's getting already opened
                mBuilder.setCancelable(false);
                // Filter button - the Recyclerview
                // What happen after click "filter"
                mBuilder.setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newLocations.clear();
                        for (int i = 0; i < usersLocations.size(); i++) {
                            newLocations.add(listLocations[usersLocations.get(i)]);
                        }
                        // Do the filter
                        FilterTeachersList.setNotificationInList(root, teachers, newLocations,
                                newCarBrands,  newGears,  newPrices,  adapter);
                    }
                });
                // Close the Dialog without doing nothing
                mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    /**
     * Set the Dialog with the Car brand's checkbox's
     * @param root fragment view
     */
    private void carTypeFilterDialog(final View root){
        Button btnCarBrandsFilter = root.findViewById(R.id.btnCarBrandsFilter);
        listCarBrands = getResources().getStringArray(R.array.car_brands);
        checkedCarBrands = new boolean[listCarBrands.length];
        btnCarBrandsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(root.getContext());
                mBuilder.setTitle(getString(R.string.title_car_brand_filter));
                mBuilder.setMultiChoiceItems(listCarBrands, checkedCarBrands, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!usersCarBrands.contains(position)) {
                                usersCarBrands.add(position);
                            }
                        } else if (usersCarBrands.contains(position)) {
                            usersCarBrands.remove((Object) position);
                        }
                    }
                });
                // Cannot close the alert dialog after it's getting already opened
                mBuilder.setCancelable(false);
                // Filter the Recyclerview
                mBuilder.setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newCarBrands.clear();
                        for (int i = 0; i < usersCarBrands.size(); i++) {
                            newCarBrands.add(listCarBrands[usersCarBrands.get(i)]);
                        }
                        FilterTeachersList.setNotificationInList(root, teachers, newLocations,
                                newCarBrands,  newGears,  newPrices,  adapter);
                    }
                });
                // Close the Dialog without doing nothing
                mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    /**
     * Set the Dialog with the Gear type's checkbox's
     * @param root fragment view
     */
    private void carGearFilterDialog(final View root){
        Button btnGearTypesFilter = root.findViewById(R.id.btnGearTypesFilter);
        listGears = getResources().getStringArray(R.array.transmission_type);
        checkedGears = new boolean[listGears.length];
        btnGearTypesFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(root.getContext());
                mBuilder.setTitle(getString(R.string.title_gear_filter));
                mBuilder.setMultiChoiceItems(listGears, checkedGears, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!usersGears.contains(position)) {
                                usersGears.add(position);
                            }
                        } else if (usersGears.contains(position)) {
                            usersGears.remove((Object) position);
                        }
                    }
                });
                // Cannot close the alert dialog after it's getting already opened
                mBuilder.setCancelable(false);
                // Filter the Recyclerview
                mBuilder.setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newGears.clear();
                        for (int i = 0; i < usersGears.size(); i++) {
                            newGears.add(listGears[usersGears.get(i)]);
                        }
                        FilterTeachersList.setNotificationInList(root, teachers, newLocations,
                                newCarBrands,  newGears,  newPrices,  adapter);
                    }
                });
                // Close the Dialog without doing nothing
                mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    /**
     * Set the Dialog with the prices checkbox's
     * @param root fragment view
     */
    private void priceFilterDialog(final View root){
        Button btnPriceFilter = root.findViewById(R.id.btnPriceFilter);
        listPriceRange = getResources().getStringArray(R.array.price_range);
        checkedPriceRange = new boolean[listPriceRange.length];
        btnPriceFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(root.getContext());
                mBuilder.setTitle(getString(R.string.title_price_filter));
                mBuilder.setMultiChoiceItems(listPriceRange, checkedPriceRange, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!usersPriceRange.contains(position)) {
                                usersPriceRange.add(position);
                            }
                        } else if (usersPriceRange.contains(position)) {
                            usersPriceRange.remove((Object) position);
                        }
                    }
                });
                // Cannot close the alert dialog after it's getting already opened
                mBuilder.setCancelable(false);
                // Filter the Recyclerview
                mBuilder.setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newPrices.clear();
                        for (int i = 0; i < usersPriceRange.size(); i++) {
                            newPrices.add(listPriceRange[usersPriceRange.get(i)]);
                        }
                        FilterTeachersList.setNotificationInList(root, teachers, newLocations,
                                newCarBrands,  newGears,  newPrices,  adapter);
                    }
                });
                // Close the Dialog without doing nothing
                mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }
}