package com.example.dovchin.horseapplication1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by dovchin on 8/19/2016.
 */
public class ViewActMaleHorse extends AppCompatActivity {
    Context context = this;
    ArrayList<DisplayData> showData2;
    ImageAdapterMaleHorse adapter2;
    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.male_horse_view_all);
        showListView2();
    }

    private void showListView2() {
        MyHelper myHelperMori = new MyHelper(context);
        showData2= myHelperMori.getAllRowsMori(myHelperMori);
        adapter2= new ImageAdapterMaleHorse(context,R.layout.single_row_malehorse,showData2);
        ListView listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(adapter2);
    }
}
