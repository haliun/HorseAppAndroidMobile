package com.example.dovchin.horseapplication1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dovchin on 8/19/2016.
 */
public class StallionGroupAdapter extends RecyclerView.Adapter<StallionGroupAdapter.MySuregHolder> {
    ArrayList<DisplayData> arrayList = new ArrayList<DisplayData>();
    Context context;
    public StallionGroupAdapter(ArrayList<DisplayData> arrayList, Context ctx)
    {
        this.arrayList = arrayList;
        context=ctx;
    }
    @Override


    public MySuregHolder onCreateViewHolder(ViewGroup parent, int viewType)

    {
        View view = LayoutInflater.from(parent.getContext()).

                inflate(R.layout.single_row_stallion,parent,false);
        MySuregHolder mySuregHolder = new MySuregHolder(view,context,arrayList);
        return mySuregHolder;
    }

    @Override    public void onBindViewHolder(MySuregHolder holder, int position) {
        DisplayData myData =  arrayList.get(position);
      //  holder.imageView.setImageResource(myData.);
        //holder.title.setText(myData.getName());
        //holder.desc.setText(myData.getDescription());
    }

    @Override    public int getItemCount() {
        return arrayList.size();
    }
    public static class MySuregHolder extends RecyclerView.ViewHolder

            implements View.OnClickListener
    {
        ImageView imageView;
        TextView title,desc;
        ArrayList<DisplayData> list = new ArrayList<DisplayData>();
        Context cobject;
        public MySuregHolder(View itemView,Context cob,ArrayList<DisplayData> lob) {
            super(itemView);
            cobject=cob;
            list=lob;
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imageView3);
            title = (TextView) itemView.findViewById(R.id.textView_sureg_ner);

        }

        @Override        public void onClick(View v) {
            int position = getAdapterPosition();
            DisplayData data = list.get(position);
            Intent intent = new Intent(cobject,StallionGroupActivity.class);
            intent.putExtra("image_id",data.dis_zurag);
            intent.putExtra("title_id",data.dis_name);
            cobject.startActivity(intent);
        }
    }
}




