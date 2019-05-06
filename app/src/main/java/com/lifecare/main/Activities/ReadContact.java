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
import com.lifecare.main.Fragments.ContactsFragment;
import com.lifecare.main.R;

public class ReadContact extends AppCompatActivity {

    TextView nameTV;
    TextView phoneTV;
    TextView addressTV;
    Spinner relationSpinner;
    String id;

    DatabaseReference dbContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_contact);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.contact);
        }

        Intent intent = getIntent();

        nameTV = findViewById(R.id.name);
        phoneTV = findViewById(R.id.phone);
        addressTV = findViewById(R.id.address);
        relationSpinner = findViewById(R.id.relation);
        String[] relationsArray = getResources().getStringArray(R.array.relationsArray);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, relationsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relationSpinner.setAdapter(adapter);

        id = intent.getStringExtra(ContactsFragment.CONTACT_ID);

        dbContacts = FirebaseDatabase.getInstance().getReference("Contacts");

        if (id != null) {
            DatabaseReference reference = dbContacts.child(id);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nameTV.setText(dataSnapshot.child("name").getValue(String.class));
                    phoneTV.setText(dataSnapshot.child("phone").getValue(String.class));
                    addressTV.setText(dataSnapshot.child("address").getValue(String.class));
                    relationSpinner.setSelection(dataSnapshot.child("relation").getValue(Integer.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
