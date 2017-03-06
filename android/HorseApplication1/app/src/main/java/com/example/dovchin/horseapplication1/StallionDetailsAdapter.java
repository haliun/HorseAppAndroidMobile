package com.example.dovchin.horseapplication1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by dovchin on 8/19/2016.
 */
public class StallionDetailsAdapter extends ArrayAdapter<DisplayData> {
    Context context;
    int layoutResourceId;
    ArrayList<DisplayData> data=new ArrayList<DisplayData>();
    public StallionDetailsAdapter(Context context, int layoutResourceId,

                                  ArrayList<DisplayData> data)

    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override


    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ImageHolder();
            holder.txtner = (TextView)row.findViewById(R.id.textView_infner);
            holder.txtughsil = (TextView)row.findViewById(R.id.textView_infugshil);
            holder.imgIcon = (ImageView)row.findViewById(R.id.imageView4);
            holder.txtzus = (TextView)row.findViewById(R.id.textView_infzus);
            holder.txtid = (TextView)row.findViewById(R.id.textView_infid);
            holder.txtognoo = (TextView)row.findViewById(R.id.textView_infognoo);
            holder.txtgazar = (TextView)row.findViewById(R.id.textView_infgazar);
            holder.txttsus = (TextView)row.findViewById(R.id.textView_inftsus);
            holder.txteh = (TextView)row.findViewById(R.id.textView_infeh);
            holder.txtetseg = (TextView)row.findViewById(R.id.textView_infetseg);


            row.setTag(holder);
        }
        else        {
            holder = (ImageHolder)row.getTag();
        }

        DisplayData picture = data.get(position);
        holder.txtner.setText(picture.dis_name);
        holder.txtughsil.setText(picture.dis_ugshil);
        holder.txtzus.setText(picture.dis_zus);
        holder.txtid.setText(picture.dis_id);
        holder.txtognoo.setText(picture.dis_ognoo);
        holder.txtgazar.setText(picture.dis_gazar);
        holder.txtetseg.setText(picture.dis_etseg);
        holder.txteh.setText(picture.dis_eh);
        holder.txttsus.setText(picture.dis_tsus);


        byte[] outImage = picture.dis_zurag;
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        holder.imgIcon.setImageBitmap(theImage);
        return row;

    }
    static class ImageHolder
    {
        ImageView imgIcon;
        TextView txtner;
        TextView txtughsil;
        TextView txtzus;
        TextView txtognoo;
        TextView txtgazar;
        TextView txttsus;
        TextView txtid;
        TextView txtetseg;
        TextView txteh;

    }

}

