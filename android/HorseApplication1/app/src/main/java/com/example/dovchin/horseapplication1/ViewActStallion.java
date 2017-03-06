package com.example.dovchin.horseapplication1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by dovchin on 8/9/2016.
 */
public class ViewActStallion extends AppCompatActivity {
    Context context = this;
    ArrayList<DisplayData> showData;
    ImageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stallion_view_all);
        showListView();
    }

    private void showListView() {
        MyHelper myHelper = new MyHelper(context);
        showData= myHelper.getAllRows(myHelper);
        adapter= new ImageAdapter(context,R.layout.single_row_stallion,showData);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
