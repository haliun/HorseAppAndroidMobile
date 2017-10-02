package com.example.dovchin.horseapplication1.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dovchin.horseapplication1.Activities.FemaleHorsesDetails;
import com.example.dovchin.horseapplication1.Activities.MaleHorseDetails;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.models.FemaleHorse;
import com.example.dovchin.horseapplication1.models.MaleHorse;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Khaliun on 5/24/2017.
 */

public class FemaleHorseAdapter extends RecyclerView.Adapter {
    private List<FemaleHorse> horse_list;
    protected Context context;
    public FemaleHorseAdapter(List<FemaleHorse> horse_list){
        this.horse_list=horse_list;
    }

    @Override
    public int getItemCount() {
        return horse_list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_malehorse, parent, false);
        FemaleHorseAdapter.SimpleItemViewHolder pvh = new FemaleHorseAdapter.SimpleItemViewHolder(v,horse_list);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        FemaleHorseAdapter.SimpleItemViewHolder viewHolder = (FemaleHorseAdapter.SimpleItemViewHolder) holder;
        viewHolder.position = position;
        final FemaleHorse horses = horse_list.get(position);
        ((FemaleHorseAdapter.SimpleItemViewHolder) holder).name_horse.setText(horses.getName_horse());
        ((FemaleHorseAdapter.SimpleItemViewHolder) holder).breed_horse.setText(horses.getBreed());
        Picasso.with(((FemaleHorseAdapter.SimpleItemViewHolder) holder).image_horse.getContext()).load(horses.getHorse_image()).networkPolicy(NetworkPolicy.OFFLINE).into(((FemaleHorseAdapter.SimpleItemViewHolder) holder).image_horse, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(((FemaleHorseAdapter.SimpleItemViewHolder)holder).image_horse.getContext()).load(horses.getHorse_image()).into(((FemaleHorseAdapter.SimpleItemViewHolder)holder).image_horse);
            }
        });            ((FemaleHorseAdapter.SimpleItemViewHolder)holder).group.setText(horses.getGroup());


    }


    public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name_horse;
        public TextView breed_horse;
        public ImageView image_horse;
        public TextView group;
        public int position;
        private List<FemaleHorse> maleHorses;
        public SimpleItemViewHolder(View itemView, final List<FemaleHorse> maleHorses) {
            super(itemView);
            itemView.setOnClickListener(this);

            name_horse = (TextView) itemView.findViewById(R.id.textViewmorinamem);
            breed_horse = (TextView) itemView.findViewById(R.id.textViewMori2);
            image_horse=(ImageView)itemView.findViewById(R.id.imageView6);
            group=(TextView)itemView.findViewById(R.id.textView96);

        }
        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, FemaleHorsesDetails.class);
            intent.putExtra("female_horses", horse_list.get(position));
            context.startActivity(intent);
            name_horse = (TextView) itemView.findViewById(R.id.textViewmorinamem);


        }



    }




}
