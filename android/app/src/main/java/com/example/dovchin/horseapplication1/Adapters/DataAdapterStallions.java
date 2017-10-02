package com.example.dovchin.horseapplication1.Adapters;

import com.example.dovchin.horseapplication1.Activities.StallionDetails;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.models.Stallion;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khaliun on 5/24/2017.
 */

public class DataAdapterStallions extends RecyclerView.Adapter<DataAdapterStallions.ViewHolder> implements Filterable {
    private ArrayList<Stallion> mArrayList;
    private ArrayList<Stallion> mFilteredList;
    private List<Stallion> stallions;

    public DataAdapterStallions(ArrayList<Stallion> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public DataAdapterStallions.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_row_stallion, viewGroup, false);
        DataAdapterStallions.ViewHolder pvh = new DataAdapterStallions.ViewHolder(view,stallions);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final DataAdapterStallions.ViewHolder viewHolder, final int i) {
        viewHolder.position = i;
        final Stallion stallions = mArrayList.get(i);
        viewHolder.horse_name.setText(mFilteredList.get(i).getName_horse());
        viewHolder.horse_breed.setText(mFilteredList.get(i).getBreed());
        Picasso.with((viewHolder).img_horse.getContext()).load(mFilteredList.get(i).getHorse_image()).networkPolicy(NetworkPolicy.OFFLINE).into(((viewHolder)).img_horse, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with((viewHolder).img_horse.getContext()).load(mFilteredList.get(i).getHorse_image()).into(((viewHolder)).img_horse);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<Stallion> filteredList = new ArrayList<>();

                    for (Stallion stallion : mArrayList) {

                        if (stallion.getName_horse().toLowerCase().contains(charString) || stallion.getBreed().toLowerCase().contains(charString) ) {

                            filteredList.add(stallion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Stallion>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView horse_name, horse_breed;
        private ImageView img_horse;
        public int position;
        private List<Stallion> stallions;
        public ViewHolder(View view,final List<Stallion> stallions) {
            super(view);
            itemView.setOnClickListener(this);
            horse_name = (TextView)view.findViewById(R.id.textViewStallionNameRow);
            horse_breed = (TextView)view.findViewById(R.id.textViewAzargaBreedRow);
            img_horse = (ImageView)view.findViewById(R.id.imageViewAzarga3);

        }
        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent newIntent = new Intent(context,StallionDetails.class);
            newIntent.putExtra("stallions", mFilteredList.get(position));
            context.startActivity(newIntent);
            horse_name = (TextView)view.findViewById(R.id.textViewStallionNameRow);


        }

    }

}