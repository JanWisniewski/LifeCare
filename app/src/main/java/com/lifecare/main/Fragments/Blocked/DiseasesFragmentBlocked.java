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
import com.lifecare.main.Activities.ReadDisease;
import com.lifecare.main.Lists.DiseaseList;
import com.lifecare.main.Models.Disease;
import com.lifecare.main.R;

import java.util.ArrayList;
import java.util.List;

public class DiseasesFragmentBlocked extends Fragment {
    public static final String DISEASE_ID = null;

    ListView listView;
    List<Disease> diseases;
    DatabaseReference dbDiseases;
    Query query;

    public DiseasesFragmentBlocked() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diseases_blocked, container, false);
        getActivity().setTitle(R.string.diseases);

        listView = view.findViewById(R.id.listViewDiseases);

        String uid = FirebaseAuth.getInstance().getUid();
        dbDiseases = FirebaseDatabase.getInstance().getReference("Diseases");
        query = dbDiseases.orderByChild("uid").equalTo(uid);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Disease disease = diseases.get(i);

                Intent openUpdate = new Intent(getActivity(), ReadDisease.class);
                openUpdate.putExtra(DISEASE_ID, disease.getId());

                startActivity(openUpdate);
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
