package com.lifecare.main.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lifecare.main.Fragments.ContactsFragment;
import com.lifecare.main.Models.Contact;
import com.lifecare.main.R;

public class FillContact extends AppCompatActivity {

    EditText nameET;
    EditText phoneET;

    DatabaseReference dbContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_contact);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        nameET = findViewById(R.id.name);
        phoneET = findViewById(R.id.phone);
        Button addContactBtn = findViewById(R.id.addContactBtn);

        dbContacts = FirebaseDatabase.getInstance().getReference("Contacts");
        
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact();
            }
        });
    }

    private void addContact() {
        String name = nameET.getText().toString();
        String phone = phoneET.getText().toString();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String id = dbContacts.push().getKey();

        Contact contact = new Contact(id, name , phone, uid);

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
