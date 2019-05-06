package com.lifecare.main.Fragments.Blocked;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lifecare.main.Activities.ReadDoctor;
import com.lifecare.main.Lists.DoctorList;
import com.lifecare.main.Models.Doctor;
import com.lifecare.main.R;

import java.util.ArrayList;
import java.util.List;

public class DoctorsFragmentBlocked extends Fragment {
    public static final String DOCTORS_ID = null;

    ListView listView;
    List<Doctor> doctors;
    DatabaseReference dbDoctors;
    Query query;

    public DoctorsFragmentBlocked() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctors_blocked, container, false);
        getActivity().setTitle(R.string.doctors);

        listView = view.findViewById(R.id.listViewDoctors);

        String uid = FirebaseAuth.getInstance().getUid();
        dbDoctors = FirebaseDatabase.getInstance().getReference("Doctors");
        query = dbDoctors.orderByChild("uid").equalTo(uid);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Doctor doctor = doctors.get(i);

                Intent openUpdate = new Intent(getActivity(), ReadDoctor.class);
                openUpdate.putExtra(DOCTORS_ID, doctor.getId());

                startActivity(openUpdate);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        doctors = new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                doctors.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Doctor doctor = postSnapshot.getValue(Doctor.class);
                    doctors.add(doctor);
                }

                DoctorList doctorAdapter = new DoctorList(getActivity(), doctors);
                listView.setAdapter(doctorAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
