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
import com.lifecare.main.Fragments.DiseasesFragment;
import com.lifecare.main.R;

public class ReadDisease extends AppCompatActivity {

    Spinner spinnerDisease;
    TextView descriptionTV;
    Spinner spinnerState;
    String id;

    DatabaseReference dbDiseases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_disease);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.disease);
        }

        Intent intent = getIntent();

        descriptionTV = findViewById(R.id.descriptionTV);

        spinnerDisease = findViewById(R.id.spinnerDisease);
        String[] diseaseArray = getResources().getStringArray(R.array.diseasesArray);
        final ArrayAdapter<String> diseaseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, diseaseArray);
        diseaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDisease.setAdapter(diseaseAdapter);

        spinnerState = findViewById(R.id.spinnerState);
        String[] stateArray = getResources().getStringArray(R.array.stateArray);
        final ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateArray);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(stateAdapter);

        id = intent.getStringExtra(DiseasesFragment.DISEASE_ID);

        dbDiseases = FirebaseDatabase.getInstance().getReference("Diseases");

        if (id != null) {
            DatabaseReference reference = dbDiseases.child(id);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    spinnerDisease.setSelection(dataSnapshot.child("diseaseID").getValue(Integer.class));
                    descriptionTV.setText(dataSnapshot.child("description").getValue(String.class));
                    spinnerState.setSelection(dataSnapshot.child("stateID").getValue(Integer.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
