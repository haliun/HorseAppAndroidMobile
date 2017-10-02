package com.example.dovchin.horseapplication1.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dovchin.horseapplication1.Activities.MaleHorseDetails;
import com.example.dovchin.horseapplication1.R;
import com.example.dovchin.horseapplication1.models.MaleHorse;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Khaliun on 5/24/2017.
 */

public class MaleHorseAdapter extends RecyclerView.Adapter {
    private List<MaleHorse> horse_list;
    protected Context context;
    public MaleHorseAdapter(List<MaleHorse> horse_list){
        this.horse_list=horse_list;
    }

    @Override
    public int getItemCount() {
        return horse_list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_malehorse, parent, false);
        MaleHorseAdapter.SimpleItemViewHolder pvh = new MaleHorseAdapter.SimpleItemViewHolder(v,horse_list);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        MaleHorseAdapter.SimpleItemViewHolder viewHolder = (MaleHorseAdapter.SimpleItemViewHolder) holder;
        viewHolder.position = position;
        final MaleHorse horses = horse_list.get(position);
        ((MaleHorseAdapter.SimpleItemViewHolder) holder).name_horse.setText(horses.getName_horse());
        ((MaleHorseAdapter.SimpleItemViewHolder) holder).breed_horse.setText(horses.getBreed());
        Picasso.with(((MaleHorseAdapter.SimpleItemViewHolder) holder).image_horse.getContext()).load(horses.getHorse_image()).networkPolicy(NetworkPolicy.OFFLINE).into(((MaleHorseAdapter.SimpleItemViewHolder) holder).image_horse, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(((MaleHorseAdapter.SimpleItemViewHolder)holder).image_horse.getContext()).load(horses.getHorse_image()).into(((MaleHorseAdapter.SimpleItemViewHolder)holder).image_horse);
            }
        });            ((MaleHorseAdapter.SimpleItemViewHolder)holder).group.setText(horses.getGroup_id());


    }


    public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name_horse;
        public TextView breed_horse;
        public ImageView image_horse;
        public TextView group;
        public int position;
        private List<MaleHorse> maleHorses;
        public SimpleItemViewHolder(View itemView, final List<MaleHorse> maleHorses) {
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
            Intent intent = new Intent(context, MaleHorseDetails.class);
            intent.putExtra("maleHorses", horse_list.get(position));
            context.startActivity(intent);
            name_horse = (TextView) itemView.findViewById(R.id.textViewmorinamem);


        }



    }




}
