package com.lifecare.main.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lifecare.main.Activities.FillDoctor;
import com.lifecare.main.Lists.DoctorList;
import com.lifecare.main.Models.Doctor;
import com.lifecare.main.R;

import java.util.ArrayList;
import java.util.List;

public class DoctorsFragment extends Fragment {
    public static final String DOCTORS_ID = null;

    ListView listView;
    List<Doctor> doctors;
    DatabaseReference dbDoctors;
    Query query;

    public DoctorsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctors, container, false);
        getActivity().setTitle(R.string.doctors);

        final Button btn = view.findViewById(R.id.addDoctorBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDoctorIntent = new Intent(getActivity(), FillDoctor.class);
                startActivity(addDoctorIntent);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        listView = view.findViewById(R.id.listViewDoctors);

        String uid = FirebaseAuth.getInstance().getUid();
        dbDoctors = FirebaseDatabase.getInstance().getReference("Doctors");
        query = dbDoctors.orderByChild("uid").equalTo(uid);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Doctor doctor = doctors.get(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent openUpdate = new Intent(getActivity(), FillDoctor.class);
                        openUpdate.putExtra(DOCTORS_ID, doctor.getId());

                        startActivity(openUpdate);
                    }
                });
                builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbDoctors.child(doctor.getId()).removeValue().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), R.string.deletedDoctor, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                builder.show();
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
