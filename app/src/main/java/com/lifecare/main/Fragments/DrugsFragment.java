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
import com.lifecare.main.Activities.FillDrug;
import com.lifecare.main.Lists.DrugList;
import com.lifecare.main.Models.Drug;
import com.lifecare.main.R;

import java.util.ArrayList;
import java.util.List;

public class DrugsFragment extends Fragment {
    public static final String DRUG_ID = null;

    ListView listView;
    List<Drug> drugs;
    DatabaseReference dbDrugs;
    Query query;

    public DrugsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drugs, container, false);
        getActivity().setTitle(R.string.drugs);

        final Button btn = view.findViewById(R.id.addMedicineBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDrugIntent = new Intent(getActivity(), FillDrug.class);
                startActivity(addDrugIntent);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        listView = view.findViewById(R.id.listViewDrugs);

        String uid = FirebaseAuth.getInstance().getUid();
        dbDrugs = FirebaseDatabase.getInstance().getReference("Drugs");
        query = dbDrugs.orderByChild("uid").equalTo(uid);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Drug drug = drugs.get(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent openUpdate = new Intent(getActivity(), FillDrug.class);
                        openUpdate.putExtra(DRUG_ID, drug.getId());

                        startActivity(openUpdate);
                    }
                });
                builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbDrugs.child(drug.getId()).removeValue().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), R.string.deletedDrug, Toast.LENGTH_LONG).show();
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
