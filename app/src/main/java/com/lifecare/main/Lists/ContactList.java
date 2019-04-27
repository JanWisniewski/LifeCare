package com.lifecare.main.Lists;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lifecare.main.Models.Contact;
import com.lifecare.main.R;

import java.util.List;

public class ContactList extends ArrayAdapter<Contact> {

    List<Contact> contacts;
    private Activity context;

    public ContactList(Activity context, List<Contact> contacts) {
        super(context, R.layout.layout_contact_list, contacts);
        this.context = context;
        this.contacts = contacts;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_contact_list, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.textViewName);
        TextView textViewPhone = listViewItem.findViewById(R.id.textViewPhone);

        Contact contact = contacts.get(position);
        textViewName.setText(contact.getName());
        textViewPhone.setText(contact.getPhone());

        return listViewItem;
    }
}
