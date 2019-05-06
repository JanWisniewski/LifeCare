package com.lifecare.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifecare.main.Fragments.DoctorsFragment;
import com.lifecare.main.R;

public class ReadDoctor extends AppCompatActivity {

    TextView nameTV;
    TextView phoneTV;
    Spinner spinnerSpecialization;
    String id;

    DatabaseReference dbDoctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_doctor);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.doctor);
        }

        Intent intent = getIntent();

        nameTV = findViewById(R.id.nameDoctorTV);
        phoneTV = findViewById(R.id.phoneDoctorTV);
        spinnerSpecialization = findViewById(R.id.spinnerSpecialization);
        String[] specializationArray = getResources().getStringArray(R.array.specializationArray);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, specializationArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecialization.setAdapter(adapter);

        id = intent.getStringExtra(DoctorsFragment.DOCTORS_ID);

        dbDoctors = FirebaseDatabase.getInstance().getReference("Doctors");

        if (id != null) {
            DatabaseReference reference = dbDoctors.child(id);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    nameTV.setText(dataSnapshot.child("name").getValue(String.class));
                    phoneTV.setText(dataSnapshot.child("phoneNumber").getValue(String.class));
                    spinnerSpecialization.setSelection(dataSnapshot.child("specializationID").getValue(Integer.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
