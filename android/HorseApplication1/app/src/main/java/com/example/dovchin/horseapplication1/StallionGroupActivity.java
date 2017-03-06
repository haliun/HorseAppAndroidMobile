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
 * Created by dovchin on 8/5/2016.
 */
public class StallionGroupActivity extends ArrayAdapter<DisplayData> {
    Context context;
    int layoutResourceId;
    ArrayList<DisplayData> data=new ArrayList<DisplayData>();
    public StallionGroupActivity(Context context, int layoutResourceId,

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
            holder.txtTitle = (TextView)row.findViewById(R.id.textViewmorinamem);
            holder.txtDesc = (TextView)row.findViewById(R.id.textViewMori2);
            holder.imgIcon = (ImageView)row.findViewById(R.id.imageViewAzarga3);

            row.setTag(holder);
        }
        else        {
            holder = (ImageHolder)row.getTag();
        }
        DisplayData picture = data.get(position);
        holder.txtTitle.setText(picture.dis_name);
        holder.txtDesc.setText(picture.dis_ugshil);

        byte[] outImage=picture.dis_zurag;
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        holder.imgIcon.setImageBitmap(theImage);
        return row;
    }
    static class ImageHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtDesc;

    }
}