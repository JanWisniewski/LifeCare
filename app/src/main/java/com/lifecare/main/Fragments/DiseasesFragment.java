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
import com.lifecare.main.Activities.FillDisease;
import com.lifecare.main.Lists.DiseaseList;
import com.lifecare.main.Models.Disease;
import com.lifecare.main.R;

import java.util.ArrayList;
import java.util.List;

public class DiseasesFragment extends Fragment {
    public static final String DISEASE_ID = null;

    ListView listView;
    List<Disease> diseases;
    DatabaseReference dbDiseases;
    Query query;

    public DiseasesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diseases, container, false);
        getActivity().setTitle(R.string.diseases);

        final Button addDiseaseBtn = view.findViewById(R.id.addDiseaseBtn);
        addDiseaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDiseaseIntent = new Intent(getActivity(), FillDisease.class);
                startActivity(addDiseaseIntent);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        listView = view.findViewById(R.id.listViewDiseases);

        String uid = FirebaseAuth.getInstance().getUid();
        dbDiseases = FirebaseDatabase.getInstance().getReference("Diseases");
        query = dbDiseases.orderByChild("uid").equalTo(uid);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Disease disease = diseases.get(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent openUpdate = new Intent(getActivity(), FillDisease.class);
                        openUpdate.putExtra(DISEASE_ID, disease.getId());

                        startActivity(openUpdate);
                    }
                });
                builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbDiseases.child(disease.getId()).removeValue().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), R.string.deletedDisease, Toast.LENGTH_LONG).show();
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
        diseases = new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                diseases.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Disease disease = postSnapshot.getValue(Disease.class);
                    diseases.add(disease);
                }

                DiseaseList diseaseAdapter = new DiseaseList(getActivity(), diseases);
                listView.setAdapter(diseaseAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
