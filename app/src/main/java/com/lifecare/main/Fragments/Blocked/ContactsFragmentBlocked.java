package com.lifecare.main.Fragments.Blocked;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lifecare.main.Activities.ReadContact;
import com.lifecare.main.Lists.ContactList;
import com.lifecare.main.Models.Contact;
import com.lifecare.main.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragmentBlocked extends Fragment {
    public static final String CONTACT_ID = null;

    ListView listView;
    DatabaseReference dbContacts;
    List<Contact> contacts;
    Query query;

    public ContactsFragmentBlocked() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_blocked, container, false);
        getActivity().setTitle(R.string.contacts);

        listView = view.findViewById(R.id.listViewContacts);

        String uid = FirebaseAuth.getInstance().getUid();
        dbContacts = FirebaseDatabase.getInstance().getReference("Contacts");
        query = dbContacts.orderByChild("uid").equalTo(uid);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Contact contact = contacts.get(i);

                Intent openUpdate = new Intent(getActivity(), ReadContact.class);
                openUpdate.putExtra(CONTACT_ID, contact.getId());

                startActivity(openUpdate);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        contacts = new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                contacts.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = postSnapshot.getValue(Contact.class);
                    contacts.add(contact);
                }

                ContactList contactAdapter = new ContactList(getActivity(), contacts);
                listView.setAdapter(contactAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
