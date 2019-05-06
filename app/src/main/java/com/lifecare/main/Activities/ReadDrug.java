package com.lifecare.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifecare.main.Fragments.DrugsFragment;
import com.lifecare.main.R;

public class ReadDrug extends AppCompatActivity {

    TextView nameTV;
    TextView conflictsTV;
    Spinner spinnerDosage;
    String id;

    DatabaseReference dbDrugs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_drug);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.drug);
        }

        Intent intent = getIntent();

        nameTV = findViewById(R.id.nameDrugTV);
        conflictsTV = findViewById(R.id.conflictsTV);
        spinnerDosage = findViewById(R.id.spinnerDosage);
        String[] dosageArray = getResources().getStringArray(R.array.dosageArray);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, dosageArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDosage.setAdapter(adapter);

        id = intent.getStringExtra(DrugsFragment.DRUG_ID);

        dbDrugs = FirebaseDatabase.getInstance().getReference("Drugs");

        if (id != null) {
            DatabaseReference reference = dbDrugs.child(id);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nameTV.setText(dataSnapshot.child("name").getValue(String.class));
                    conflictsTV.setText(dataSnapshot.child("conflicts").getValue(String.class));
                    spinnerDosage.setSelection(dataSnapshot.child("dosageID").getValue(Integer.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
