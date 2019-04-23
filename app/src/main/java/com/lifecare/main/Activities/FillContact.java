package com.lifecare.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
import com.lifecare.main.Fragments.ContactsFragment;
import com.lifecare.main.Models.Contact;
import com.lifecare.main.R;

public class FillContact extends AppCompatActivity {

    EditText nameET;
    EditText phoneET;
    EditText addressET;
    Spinner relationSpinner;

    DatabaseReference dbContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_contact);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();

        nameET = findViewById(R.id.name);
        phoneET = findViewById(R.id.phone);
        addressET = findViewById(R.id.address);
        relationSpinner = findViewById(R.id.relation);
        String[] relationsArray = getResources().getStringArray(R.array.relationsArray);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, relationsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relationSpinner.setAdapter(adapter);
        Button addContactBtn = findViewById(R.id.addContactBtn);

        final String id = intent.getStringExtra(ContactsFragment.CONTACT_ID);

        dbContacts = FirebaseDatabase.getInstance().getReference("Contacts");

        if (id != null) {
            DatabaseReference reference = dbContacts.child(id);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nameET.setText(dataSnapshot.child("name").getValue(String.class));
                    phoneET.setText(dataSnapshot.child("phone").getValue(String.class));
                    addressET.setText(dataSnapshot.child("address").getValue(String.class));
                    relationSpinner.setSelection(dataSnapshot.child("relation").getValue(Integer.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            addContactBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateContact(id);
                }
            });

        } else {
            addContactBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addContact();
                }
            });
        }
    }

    private void updateContact(String id) {
        String name = nameET.getText().toString();
        String phone = phoneET.getText().toString();
        String address = addressET.getText().toString();
        Integer relation = relationSpinner.getSelectedItemPosition();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Contact contact = new Contact(id, name, relation, address, phone, uid);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Contacts").child(id);

        databaseReference.setValue(contact).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.updatedContact, Toast.LENGTH_LONG).show();
                    backToList();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addContact() {
        String name = nameET.getText().toString();
        String phone = phoneET.getText().toString();
        String address = addressET.getText().toString();
        Integer relation = relationSpinner.getSelectedItemPosition();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String id = dbContacts.push().getKey();

        Contact contact = new Contact(id, name, relation, address, phone, uid);

        dbContacts.child(id).setValue(contact).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.addedContact, Toast.LENGTH_LONG).show();
                    backToList();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void backToList() {
        Intent contactsFragment = new Intent(getApplicationContext(), Main.class);
        contactsFragment.putExtra("fragmentName", 0);
        startActivity(contactsFragment);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        backToList();
        return true;
    }
}
