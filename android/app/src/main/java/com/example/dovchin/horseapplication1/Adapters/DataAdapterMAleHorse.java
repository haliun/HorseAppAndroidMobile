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

import com.example.dovchin.horseapplication1.Activities.FemaleHorsesDetails;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.models.FemaleHorse;
import com.example.dovchin.horseapplication1.models.MaleHorse;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khaliun on 6/18/2017.
 */

public class DataAdapterMAleHorse extends RecyclerView.Adapter<DataAdapterMAleHorse.ViewHolder> implements Filterable {
    private ArrayList<MaleHorse> mArrayList;
    private ArrayList<MaleHorse> mFilteredList;
    private List<MaleHorse> stallions;

    public DataAdapterMAleHorse(ArrayList<MaleHorse> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public DataAdapterMAleHorse.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_row_malehorse, viewGroup, false);
        DataAdapterMAleHorse.ViewHolder pvh = new DataAdapterMAleHorse.ViewHolder(view, stallions);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final DataAdapterMAleHorse.ViewHolder viewHolder, final int i) {
        viewHolder.position = i;
        final MaleHorse stallions = mArrayList.get(i);
        viewHolder.horse_name.setText(mFilteredList.get(i).getName_horse());
        viewHolder.horse_breed.setText(mFilteredList.get(i).getBreed());
        viewHolder.group.setText(mFilteredList.get(i).getGroup_id());
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

                    ArrayList<MaleHorse> filteredList = new ArrayList<>();

                    for (MaleHorse stallion : mArrayList) {

                        if (stallion.getName_horse().toLowerCase().contains(charString) || stallion.getGroup_id().toLowerCase().contains(charString) || stallion.getBreed().toLowerCase().contains(charString)) {

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
                mFilteredList = (ArrayList<MaleHorse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView horse_name, horse_breed, group;
        private ImageView img_horse;
        public int position;
        private List<MaleHorse> stallions;

        public ViewHolder(View view, final List<MaleHorse> stallions) {
            super(view);
            itemView.setOnClickListener(this);
            horse_name = (TextView) itemView.findViewById(R.id.textViewmorinamem);
            horse_breed = (TextView) itemView.findViewById(R.id.textViewMori2);
            img_horse=(ImageView)itemView.findViewById(R.id.imageView6);
            group=(TextView)itemView.findViewById(R.id.textView96);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent newIntent = new Intent(context, FemaleHorsesDetails.class);
            newIntent.putExtra("maleHorses", mFilteredList.get(position));
            context.startActivity(newIntent);
            horse_name = (TextView) itemView.findViewById(R.id.textViewmorinamem);


        }

    }
}

