package com.example.dovchin.horseapplication1.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.dovchin.horseapplication1.Adapters.MaleHorseAdapter;
import com.example.dovchin.horseapplication1.Adapters.RAdapterFood;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.auth.LoginActivity;
import com.example.dovchin.horseapplication1.models.FoodVit;
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

public class MaleHorseAdapterFragment extends Fragment {

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

        View view = inflater.inflate(R.layout.fragment_male_horse_adapter, container, false);
        String group_name= ((StallionGroupActivity)getActivity()).getHorse_name();

        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_rec_male);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ArrayList<MaleHorse> items = new ArrayList<MaleHorse>();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser==null){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }else{
            mUserId=mFirebaseUser.getUid();
            String group_name= ((StallionGroupActivity)getActivity()).getHorse_name();
            //Toast.makeText(getActivity(),group_name, Toast.LENGTH_SHORT).show();
            Query applesQuery=mDatabase.child("users").child(mUserId).child("MaleHorses").orderByChild("group_id").equalTo(group_name);
            applesQuery.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            items.clear();
                            Log.w("MaleHorses", "getUser:onCancelled " + dataSnapshot.toString());
                            Log.w("MaleHorses", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                MaleHorse horses = data.getValue(MaleHorse.class);
                                Log.w("MaleHorses", "getUser:onCancelled " + horses.getName_horse());
                                items.add(horses);
                            }
                            MaleHorseAdapter adapter=new MaleHorseAdapter(items);
                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("MaleHorses", "getUser:onCancelled", databaseError.toException());

                        }


                    }
            );


        }
    }

}

