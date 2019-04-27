package com.lifecare.main.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lifecare.main.Activities.FillDrug;
import com.lifecare.main.Lists.DrugList;
import com.lifecare.main.Models.Drug;
import com.lifecare.main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrugsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrugsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrugsFragment extends Fragment {
    public static final String DRUG_ID = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listView;
    List<Drug> drugs;
    DatabaseReference dbDrugs;
    Query query;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public DrugsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrugsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrugsFragment newInstance(String param1, String param2) {
        DrugsFragment fragment = new DrugsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drugs, container, false);
        final Button btn = view.findViewById(R.id.addMedicineBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDrugIntent = new Intent(getActivity(), FillDrug.class);
                startActivity(addDrugIntent);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        listView = view.findViewById(R.id.listViewDrugs);

        String uid = FirebaseAuth.getInstance().getUid();
        dbDrugs = FirebaseDatabase.getInstance().getReference("Drugs");
        query = dbDrugs.orderByChild("uid").equalTo(uid);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Drug drug = drugs.get(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent openUpdate = new Intent(getActivity(), FillDrug.class);
                        openUpdate.putExtra(DRUG_ID, drug.getId());

                        startActivity(openUpdate);
                    }
                });
                builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbDrugs.child(drug.getId()).removeValue().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), R.string.deletedDrug, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                builder.show();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        drugs = new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                drugs.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Drug drug = postSnapshot.getValue(Drug.class);
                    drugs.add(drug);
                }

                DrugList drugAdapter = new DrugList(getActivity(), drugs);
                listView.setAdapter(drugAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
