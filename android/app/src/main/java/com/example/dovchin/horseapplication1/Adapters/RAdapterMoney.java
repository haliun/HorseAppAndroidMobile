package com.example.dovchin.horseapplication1.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dovchin.horseapplication1.Activities.FoodVitDetails;
import com.example.dovchin.horseapplication1.Activities.MoneyDetails;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.models.FoodVit;
import com.example.dovchin.horseapplication1.models.Money;

import java.util.List;

/**
 * Created by Khaliun on 6/18/2017.
 */

public class RAdapterMoney extends RecyclerView.Adapter {
    private List<Money> foodVits_list;
    protected Context context;
    public RAdapterMoney(List<Money> foodVits_list){
        this.foodVits_list=foodVits_list;
    }

    @Override
    public int getItemCount() {
        return foodVits_list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_money, parent, false);
        RAdapterMoney.SimpleItemViewHolder pvh = new RAdapterMoney.SimpleItemViewHolder(v,foodVits_list);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RAdapterMoney.SimpleItemViewHolder viewHolder = (RAdapterMoney.SimpleItemViewHolder) holder;
        viewHolder.position = position;
        Money foodVit = foodVits_list.get(position);
        ((RAdapterMoney.SimpleItemViewHolder) holder).name_horse.setText(foodVit.getName());
        ((RAdapterMoney.SimpleItemViewHolder) holder).date_horse.setText(foodVit.getSum());
        ((RAdapterMoney.SimpleItemViewHolder)holder).type.setText(foodVit.getType());


    }


    public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name_horse;
        public TextView date_horse,type;
        public int position;
        private List<Money> foodVits;
        public SimpleItemViewHolder(View itemView, final List<Money> foodVits) {
            super(itemView);
            itemView.setOnClickListener(this);
            name_horse = (TextView) itemView.findViewById(R.id.textView46);
            date_horse = (TextView) itemView.findViewById(R.id.textView94);
            type=(TextView) itemView.findViewById(R.id.textView126);

        }
        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, MoneyDetails.class);
            intent.putExtra("money", foodVits_list.get(position));
            context.startActivity(intent);
            name_horse = (TextView) itemView.findViewById(R.id.textView46);


        }



    }




}
