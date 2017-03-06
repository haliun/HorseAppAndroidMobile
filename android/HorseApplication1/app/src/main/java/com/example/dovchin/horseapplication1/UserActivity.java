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
 * Created by amour on 2/13/2017.
 */
public class UserActivity  extends ArrayAdapter<Diplay_user> {
    Context context;
    int layoutResourceId;
    ArrayList<Diplay_user> data=new ArrayList<Diplay_user>();
    public UserActivity(Context context, int layoutResourceId, ArrayList<Diplay_user> data)
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
            holder.txtovog = (TextView)row.findViewById(R.id.textView_userfname);
            holder.txtner = (TextView)row.findViewById(R.id.textView_userlname);
            holder.txtmail = (TextView)row.findViewById(R.id.textView_usermail);
            holder.txtaimag = (TextView)row.findViewById(R.id.textView_userhot);
            holder.txtsum = (TextView)row.findViewById(R.id.textView_usersum);



            row.setTag(holder);
        }
        else        {
            holder = (ImageHolder)row.getTag();
        }

        Diplay_user picture = data.get(position);
        holder.txtovog.setText(picture.dis_fname);
        holder.txtner.setText(picture.dis_lname);
        holder.txtmail.setText(picture.dis_mail);
        holder.txtaimag.setText(picture.dis_aimag);
        holder.txtsum.setText(picture.dis_sum);
        return row;

    }
    static class ImageHolder
    {
        TextView txtner;
        TextView txtovog;
        TextView txtmail;
        TextView txtaimag;
        TextView txtsum;

    }

}

