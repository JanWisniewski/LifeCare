package com.lifecare.main.Lists;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lifecare.main.Models.Drug;
import com.lifecare.main.R;

import java.util.List;

public class DrugList extends ArrayAdapter<Drug> {

    List<Drug> drugs;
    private Activity context;

    public DrugList(Activity context, List<Drug> drugs) {
        super(context, R.layout.layout_drug_list, drugs);
        this.context = context;
        this.drugs = drugs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_drug_list, null, true);

        TextView textViewNameDrug = listViewItem.findViewById(R.id.textViewNameDrug);
        TextView textViewConflict = listViewItem.findViewById(R.id.textViewConflict);

        Drug drug = drugs.get(position);
        textViewNameDrug.setText(drug.getName());
        textViewConflict.setText(drug.getConflicts());

        return listViewItem;
    }
}
