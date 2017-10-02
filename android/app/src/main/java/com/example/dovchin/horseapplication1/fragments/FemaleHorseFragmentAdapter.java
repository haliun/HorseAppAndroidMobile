package com.example.dovchin.horseapplication1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dovchin.horseapplication1.Activities.StallionGroupActivity;
import com.example.dovchin.horseapplication1.Adapters.FemaleHorseAdapter;
import com.example.dovchin.horseapplication1.Adapters.MaleHorseAdapter;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.models.FemaleHorse;
import com.example.dovchin.horseapplication1.models.MaleHorse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Khaliun on 5/24/2017.
 */

public class FemaleHorseFragmentAdapter extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_female_adapter, container, false);
        String group_name= ((StallionGroupActivity)getActivity()).getHorse_name();

        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.female_rec_fragment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ArrayList<FemaleHorse> items = new ArrayList<FemaleHorse>();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            String group_name= ((StallionGroupActivity)getActivity()).getHorse_name();
            //Toast.makeText(getActivity(),group_name, Toast.LENGTH_SHORT).show();
            Query applesQuery=mDatabase.child("users").child(mUserId).child("FemaleHorses").orderByChild("group").equalTo(group_name);
            applesQuery.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            items.clear();
                            Log.w("femaleHorses", "getUser:onCancelled " + dataSnapshot.toString());
                            Log.w("femaleHorses", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                FemaleHorse horses = data.getValue(FemaleHorse.class);
                                items.add(horses);
                            }
                            FemaleHorseAdapter adapter=new FemaleHorseAdapter(items);
                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("femaleHorses", "getUser:onCancelled", databaseError.toException());

                        }


                    }
            );


        }
    }

}


