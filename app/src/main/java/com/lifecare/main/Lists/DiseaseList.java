package com.lifecare.main.Lists;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lifecare.main.Models.Disease;
import com.lifecare.main.R;

import java.util.List;

public class DiseaseList extends ArrayAdapter<Disease> {

    List<Disease> diseases;
    private Activity context;

    public DiseaseList(Activity context, List<Disease> diseases) {
        super(context, R.layout.layout_disease_list, diseases);
        this.context = context;
        this.diseases = diseases;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_disease_list, null, true);

        TextView textViewDisease = listViewItem.findViewById(R.id.textViewDisease);
        TextView textViewState = listViewItem.findViewById(R.id.textViewState);

        Disease disease = diseases.get(position);

        String[] diseaseArray = listViewItem.getResources().getStringArray(R.array.diseasesArray);

        String[] stateArray = listViewItem.getResources().getStringArray(R.array.stateArray);

        textViewDisease.setText(diseaseArray[disease.getDiseaseID()]);
        textViewState.setText(stateArray[disease.getStateID()]);

        return listViewItem;
    }
}
