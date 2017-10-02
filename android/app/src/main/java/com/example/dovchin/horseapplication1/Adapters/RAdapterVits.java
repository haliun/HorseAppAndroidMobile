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

import java.util.List;

/**
 * Created by Khaliun on 5/8/2017.
 */

public class RAdapterVits extends RecyclerView.Adapter {
    private List<FoodVit> foodVits_list;
    protected Context context;
    public RAdapterVits(List<FoodVit> foodVits_list){
        this.foodVits_list=foodVits_list;
    }

    @Override
    public int getItemCount() {
        return foodVits_list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_food, parent, false);
        SimpleItemViewHolder pvh = new SimpleItemViewHolder(v,foodVits_list);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RAdapterVits.SimpleItemViewHolder viewHolder = (RAdapterVits.SimpleItemViewHolder) holder;
        viewHolder.position = position;
        FoodVit foodVit1 = foodVits_list.get(position);
       ((RAdapterVits.SimpleItemViewHolder) holder).name_horse.setText(foodVit1.getName());
        ((RAdapterVits.SimpleItemViewHolder) holder).date_horse.setText(foodVit1.getDate());


    }


    public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name_horse;

        public TextView date_horse;
        public int position;
        private List<FoodVit> foodVits;
        public SimpleItemViewHolder(View itemView, final List<FoodVit> foodVits) {
            super(itemView);
            itemView.setOnClickListener(this);
            name_horse = (TextView) itemView.findViewById(R.id.textView_row_name_vit);
            date_horse = (TextView) itemView.findViewById(R.id.textView_name_date_vit);

        }
        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, FoodVitDetails.class);
            intent.putExtra("foods", foodVits_list.get(position));
            context.startActivity(intent);
            name_horse = (TextView) itemView.findViewById(R.id.textView_row_name_vit);


        }



    }




}
