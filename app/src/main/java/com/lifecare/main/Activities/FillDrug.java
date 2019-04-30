package com.lifecare.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
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
import com.lifecare.main.Fragments.DrugsFragment;
import com.lifecare.main.Models.Drug;
import com.lifecare.main.R;

public class FillDrug extends AppCompatActivity {

    EditText nameET;
    EditText conflictsET;
    Spinner spinnerDosage;
    String id;

    DatabaseReference dbDrugs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_drug);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.drug);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();

        nameET = findViewById(R.id.nameDrugET);
        conflictsET = findViewById(R.id.conflictsET);
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
                    nameET.setText(dataSnapshot.child("name").getValue(String.class));
                    conflictsET.setText(dataSnapshot.child("conflicts").getValue(String.class));
                    spinnerDosage.setSelection(dataSnapshot.child("dosageID").getValue(Integer.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void onClickSave() {
        if (id != null) {
            updateDrug(id);
        } else {
            addDrug();
        }
    }

    private void updateDrug(String id) {
        String name = nameET.getText().toString();
        String conflicts = conflictsET.getText().toString();
        Integer dosage = spinnerDosage.getSelectedItemPosition();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Drug drug = new Drug(id, name, dosage, conflicts, uid);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Drugs").child(id);

        databaseReference.setValue(drug).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.updatedDrug, Toast.LENGTH_LONG).show();
                    backToList();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void backToList() {
        Intent drugsFragment = new Intent(getApplicationContext(), Main.class);
        drugsFragment.putExtra("fragmentName", "drugs");
        startActivity(drugsFragment);
        finish();
    }

    private void addDrug() {
        String name = nameET.getText().toString();
        String conflicts = conflictsET.getText().toString();
        Integer dosage = spinnerDosage.getSelectedItemPosition();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String id = dbDrugs.push().getKey();

        Drug drug = new Drug(id, name, dosage, conflicts, uid);

        dbDrugs.child(id).setValue(drug).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.addDrug, Toast.LENGTH_LONG).show();
                    backToList();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            onClickSave();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
