package com.lifecare.main.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifecare.main.R;

public class ReadUserData extends AppCompatActivity {

    TextView nameTV;
    TextView surnameTV;
    Spinner spinnerSex;
    String id;

    DatabaseReference dbUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_user_data);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.userData);
        }

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        nameTV = findViewById(R.id.nameTV);
        surnameTV = findViewById(R.id.surnameTV);
        spinnerSex = findViewById(R.id.spinnerSex);
        String[] sexArray = getResources().getStringArray(R.array.sexArray);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, sexArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(adapter);

        dbUserData = FirebaseDatabase.getInstance().getReference("UserData");

        DatabaseReference reference = dbUserData.child(id);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("sexID")) {
                    nameTV.setText(dataSnapshot.child("name").getValue(String.class));
                    surnameTV.setText(dataSnapshot.child("surname").getValue(String.class));
                    spinnerSex.setSelection(dataSnapshot.child("sexID").getValue(Integer.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
