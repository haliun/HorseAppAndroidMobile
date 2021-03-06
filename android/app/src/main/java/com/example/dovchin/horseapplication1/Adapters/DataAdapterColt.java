package com.example.dovchin.horseapplication1.Adapters;

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

import com.example.dovchin.horseapplication1.Activities.ColtDetailsActivity;
import com.example.dovchin.horseapplication1.Activities.StallionDetails;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.models.Colt;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khaliun on 6/18/2017.
 */

public class DataAdapterColt extends RecyclerView.Adapter<DataAdapterColt.ViewHolder> implements Filterable {
    private ArrayList<Colt> mArrayList;
    private ArrayList<Colt> mFilteredList;
    private List<Colt> stallions;

    public DataAdapterColt(ArrayList<Colt> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public DataAdapterColt.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_row_colt, viewGroup, false);
        DataAdapterColt.ViewHolder pvh = new DataAdapterColt.ViewHolder(view,stallions);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final DataAdapterColt.ViewHolder viewHolder, final int i) {
        viewHolder.position = i;
        final Colt stallions = mArrayList.get(i);
        viewHolder.horse_name.setText(mFilteredList.get(i).getName_horse());
        viewHolder.horse_breed.setText(mFilteredList.get(i).getBreed());
        viewHolder.group.setText(mFilteredList.get(i).getGroup());
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

                    ArrayList<Colt> filteredList = new ArrayList<>();

                    for (Colt stallion : mArrayList) {

                        if (stallion.getName_horse().toLowerCase().contains(charString)||stallion.getGroup().toLowerCase().contains(charString) || stallion.getBreed().toLowerCase().contains(charString) ) {

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
                mFilteredList = (ArrayList<Colt>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView horse_name, horse_breed,group;
        private ImageView img_horse;
        public int position;
        private List<Colt> stallions;
        public ViewHolder(View view,final List<Colt> stallions) {
            super(view);
            itemView.setOnClickListener(this);
            horse_name = (TextView) itemView.findViewById(R.id.textView127);
            horse_breed = (TextView) itemView.findViewById(R.id.textView129);
            img_horse=(ImageView)itemView.findViewById(R.id.imageView18);
            group=(TextView)itemView.findViewById(R.id.textView128);

        }
        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent newIntent = new Intent(context,ColtDetailsActivity.class);
            newIntent.putExtra("colts", mFilteredList.get(position));
            context.startActivity(newIntent);
            horse_name = (TextView) itemView.findViewById(R.id.textView127);


        }

    }

}