package com.lifecare.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifecare.main.Fragments.DiseasesFragment;
import com.lifecare.main.Models.Disease;
import com.lifecare.main.R;

public class FillDisease extends AppCompatActivity {

    Spinner spinnerDisease;
    EditText descriptionET;
    Spinner spinnerState;

    DatabaseReference dbDiseases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_disease);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();

        descriptionET = findViewById(R.id.descriptionET);

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

        Button addDiseaseBtn = findViewById(R.id.addDiseaseBtn);

        final String id = intent.getStringExtra(DiseasesFragment.DISEASE_ID);

        dbDiseases = FirebaseDatabase.getInstance().getReference("Diseases");

        if (id != null) {
            DatabaseReference reference = dbDiseases.child(id);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    spinnerDisease.setSelection(dataSnapshot.child("diseaseID").getValue(Integer.class));
                    descriptionET.setText(dataSnapshot.child("description").getValue(String.class));
                    spinnerState.setSelection(dataSnapshot.child("stateID").getValue(Integer.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            addDiseaseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateDisease(id);
                }
            });
        } else {
            addDiseaseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addDisease();
                }
            });
        }
    }

    private void addDisease() {
        Integer diseaseID = spinnerDisease.getSelectedItemPosition();
        String description = descriptionET.getText().toString();
        Integer stateID = spinnerState.getSelectedItemPosition();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String id = dbDiseases.push().getKey();

        Disease disease = new Disease(id, diseaseID, description, stateID, uid);

        dbDiseases.child(id).setValue(disease).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.addedDisease, Toast.LENGTH_LONG).show();
                    backToList();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateDisease(String id) {
        Integer diseaseID = spinnerDisease.getSelectedItemPosition();
        String description = descriptionET.getText().toString();
        Integer stateID = spinnerState.getSelectedItemPosition();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Disease disease = new Disease(id, diseaseID, description, stateID, uid);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Diseases").child(id);

        databaseReference.setValue(disease).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.updatedDisease, Toast.LENGTH_LONG).show();
                    backToList();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void backToList() {
        Intent diseaseFragment = new Intent(getApplicationContext(), Main.class);
        diseaseFragment.putExtra("fragmentName", 2);
        startActivity(diseaseFragment);
        finish();
    }
}
