package com.example.dovchin.horseapplication1.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dovchin.horseapplication1.Activities.FoodVitDetails;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.models.FoodVit;
import com.example.dovchin.horseapplication1.models.Stable;

import java.util.List;

/**
 * Created by Khaliun on 6/18/2017.
 */

public class RAdapterZuchee extends RecyclerView.Adapter {
    private List<Stable> foodVits_list;
    protected Context context;
    public RAdapterZuchee(List<Stable> foodVits_list){
        this.foodVits_list=foodVits_list;
    }

    @Override
    public int getItemCount() {
        return foodVits_list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_stable, parent, false);
        RAdapterZuchee.SimpleItemViewHolder pvh = new RAdapterZuchee.SimpleItemViewHolder(v,foodVits_list);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RAdapterZuchee.SimpleItemViewHolder viewHolder = (RAdapterZuchee.SimpleItemViewHolder) holder;
        viewHolder.position = position;
        Stable foodVit = foodVits_list.get(position);
        ((RAdapterZuchee.SimpleItemViewHolder) holder).name_horse.setText(foodVit.getName());
        ((RAdapterZuchee.SimpleItemViewHolder) holder).date_horse.setText(foodVit.getCity());


    }


    public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name_horse;
        public TextView date_horse;
        public int position;
        private List<Stable> foodVits;
        public SimpleItemViewHolder(View itemView, final List<Stable> foodVits) {
            super(itemView);
            itemView.setOnClickListener(this);
            name_horse = (TextView) itemView.findViewById(R.id.textView_stable_name);
            date_horse = (TextView) itemView.findViewById(R.id.textView_stable_bairshil);

        }
        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, FoodVitDetails.class);
            intent.putExtra("stable", foodVits_list.get(position));
            context.startActivity(intent);
            name_horse = (TextView) itemView.findViewById(R.id.textView_stable_name);


        }



    }




}
