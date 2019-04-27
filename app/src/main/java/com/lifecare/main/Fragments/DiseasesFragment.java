package com.lifecare.main.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lifecare.main.Activities.FillDisease;
import com.lifecare.main.Lists.DiseaseList;
import com.lifecare.main.Models.Disease;
import com.lifecare.main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiseasesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiseasesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiseasesFragment extends Fragment {
    public static final String DISEASE_ID = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listView;
    List<Disease> diseases;
    DatabaseReference dbDiseases;
    Query query;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public DiseasesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiseasesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiseasesFragment newInstance(String param1, String param2) {
        DiseasesFragment fragment = new DiseasesFragment();
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
        View view = inflater.inflate(R.layout.fragment_diseases, container, false);
        final Button btn = view.findViewById(R.id.addDiseaseBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDiseaseIntent = new Intent(getActivity(), FillDisease.class);
                startActivity(addDiseaseIntent);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        listView = view.findViewById(R.id.listViewDiseases);

        String uid = FirebaseAuth.getInstance().getUid();
        dbDiseases = FirebaseDatabase.getInstance().getReference("Diseases");
        query = dbDiseases.orderByChild("uid").equalTo(uid);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Disease disease = diseases.get(i);

                Intent openUpdate = new Intent(getActivity(), FillDisease.class);
                openUpdate.putExtra(DISEASE_ID, disease.getId());

                startActivity(openUpdate);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        diseases = new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                diseases.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Disease disease = postSnapshot.getValue(Disease.class);
                    diseases.add(disease);
                }

                DiseaseList diseaseAdapter = new DiseaseList(getActivity(), diseases);
                listView.setAdapter(diseaseAdapter);
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
