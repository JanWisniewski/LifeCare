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
import com.lifecare.main.Activities.ReadDrug;
import com.lifecare.main.Lists.DrugList;
import com.lifecare.main.Models.Drug;
import com.lifecare.main.R;

import java.util.ArrayList;
import java.util.List;

public class DrugsFragmentBlocked extends Fragment {
    public static final String DRUG_ID = null;

    ListView listView;
    List<Drug> drugs;
    DatabaseReference dbDrugs;
    Query query;

    public DrugsFragmentBlocked() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drugs_blocked, container, false);
        getActivity().setTitle(R.string.drugs);

        listView = view.findViewById(R.id.listViewDrugs);

        String uid = FirebaseAuth.getInstance().getUid();
        dbDrugs = FirebaseDatabase.getInstance().getReference("Drugs");
        query = dbDrugs.orderByChild("uid").equalTo(uid);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Drug drug = drugs.get(i);

                Intent openUpdate = new Intent(getActivity(), ReadDrug.class);
                openUpdate.putExtra(DRUG_ID, drug.getId());

                startActivity(openUpdate);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        drugs = new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                drugs.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Drug drug = postSnapshot.getValue(Drug.class);
                    drugs.add(drug);
                }

                DrugList drugAdapter = new DrugList(getActivity(), drugs);
                listView.setAdapter(drugAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
