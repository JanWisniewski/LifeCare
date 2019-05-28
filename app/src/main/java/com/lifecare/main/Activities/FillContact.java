package com.lifecare.main.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
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

    private static final int RESULT_PICK_CONTACT = 1;

    EditText nameET;
    EditText phoneET;
    EditText addressET;
    Spinner relationSpinner;
    String id;

    DatabaseReference dbContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_contact);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.contact);
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

        Button addFromContactListBtn = findViewById(R.id.addFromContactListBtn);

        addFromContactListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });

        id = intent.getStringExtra(ContactsFragment.CONTACT_ID);

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
        contactsFragment.putExtra("fragmentName", "contacts");
        startActivity(contactsFragment);
        finish();
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

    private void onClickSave() {
        if (id != null) {
            updateContact(id);
        } else {
            addContact();
        }
    }

    @SuppressLint("Recycle")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    Cursor cursor = null;
                    try {
                        String phoneNumber = null;
                        String name = null;
                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        phoneNumber = cursor.getString(phoneIndex);
                        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        name = cursor.getString(nameIndex);

                        nameET.setText(name);
                        phoneET.setText(phoneNumber);
                        Toast.makeText(getApplicationContext(), R.string.successGetFromList, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.problemGetFromList, Toast.LENGTH_LONG).show();
        }
    }
}
