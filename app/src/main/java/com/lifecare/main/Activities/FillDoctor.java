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
import com.lifecare.main.Fragments.DoctorsFragment;
import com.lifecare.main.Models.Doctor;
import com.lifecare.main.R;

public class FillDoctor extends AppCompatActivity {

    EditText nameET;
    EditText phoneET;
    Spinner spinnerSpecialization;

    DatabaseReference dbDoctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_doctor);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();

        nameET = findViewById(R.id.nameDoctorET);
        phoneET = findViewById(R.id.phoneDoctorET);
        spinnerSpecialization = findViewById(R.id.spinnerSpecialization);
        String[] specializationArray = getResources().getStringArray(R.array.specializationArray);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, specializationArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecialization.setAdapter(adapter);
        Button addDoctorBtn = findViewById(R.id.addDoctorBtn);

        final String id = intent.getStringExtra(DoctorsFragment.DOCTORS_ID);

        dbDoctors = FirebaseDatabase.getInstance().getReference("Doctors");

        if (id != null) {
            DatabaseReference reference = dbDoctors.child(id);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    nameET.setText(dataSnapshot.child("name").getValue(String.class));
                    phoneET.setText(dataSnapshot.child("phoneNumber").getValue(String.class));
                    spinnerSpecialization.setSelection(dataSnapshot.child("specializationID").getValue(Integer.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            addDoctorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateDoctor(id);
                }
            });
        } else {
            addDoctorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addDoctor();
                }
            });
        }
    }

    private void addDoctor() {
        String name = nameET.getText().toString();
        String phone = phoneET.getText().toString();
        Integer specialization = spinnerSpecialization.getSelectedItemPosition();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String id = dbDoctors.push().getKey();

        Doctor doctor = new Doctor(id, name, phone, specialization, uid);

        dbDoctors.child(id).setValue(doctor).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.addedDoctor, Toast.LENGTH_LONG).show();
                    backToList();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateDoctor(String id) {
        String name = nameET.getText().toString();
        String phone = phoneET.getText().toString();
        Integer specialization = spinnerSpecialization.getSelectedItemPosition();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Doctor doctor = new Doctor(id, name, phone, specialization, uid);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Doctors").child(id);

        databaseReference.setValue(doctor).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.updatedDoctor, Toast.LENGTH_LONG).show();
                    backToList();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void backToList() {
        Intent doctorsFragment = new Intent(getApplicationContext(), Main.class);
        doctorsFragment.putExtra("fragmentName", 3);
        startActivity(doctorsFragment);
        finish();
    }
}
