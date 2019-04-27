package com.lifecare.main.Lists;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lifecare.main.Models.Doctor;
import com.lifecare.main.R;

import java.util.List;

public class DoctorList extends ArrayAdapter<Doctor> {

    List<Doctor> doctors;
    private Activity context;

    public DoctorList(Activity context, List<Doctor> doctors) {
        super(context, R.layout.layout_doctor_list, doctors);
        this.context = context;
        this.doctors = doctors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_doctor_list, null, true);

        TextView textViewNameDoctor = listViewItem.findViewById(R.id.textViewNameDoctor);
        TextView textViewPhoneDoctor = listViewItem.findViewById(R.id.textViewPhoneDoctor);

        Doctor doctor = doctors.get(position);
        textViewNameDoctor.setText(doctor.getName());
        textViewPhoneDoctor.setText(doctor.getPhoneNumber());

        return listViewItem;
    }
}
