package com.example.dovchin.horseapplication1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by amour on 2/9/2017.
 */
public class MyHelperUserAdd extends SQLiteOpenHelper {

    final  String CREATE_QUERY_USER = "create table " + Tab_info_user.TAB_NAME +
            " ( " + Tab_info_user.KEY_ID + " integer primary key autoincrement, " + Tab_info_user.FNAME + " text not null, "
            + Tab_info_user.LNAME + " text not null , " + Tab_info_user.MAIL+ " text not null , " + Tab_info_user.CITY + " text   , "
            + Tab_info_user.SUM + " text  );";


    Context ctx;


    public  MyHelperUserAdd(Context context)
    {
        super(context, TabInfoStallion.DB_NAME,null,4);
        ctx=context;



    }

    @Override    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY_USER);

    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tab_info_user.TAB_NAME);
        onCreate(db);
    }


    public  void putInfoUser(MyHelperUserAdd mobuser,String fname,String lname, String mail, String ccity, String ssum)
    {
        SQLiteDatabase SQ= mobuser.getWritableDatabase();
        ContentValues CV=new ContentValues();
        CV.put(Tab_info_user.FNAME, fname);
        CV.put(Tab_info_user.LNAME,lname);
        CV.put(Tab_info_user.MAIL, mail);
        CV.put(Tab_info_user.CITY, ccity);
        CV.put(Tab_info_user.SUM, ssum);

        SQ.insert(Tab_info_user.TAB_NAME, null, CV);
        Toast.makeText(ctx,"Амжилттай!",Toast.LENGTH_LONG).show();
    }

    public ArrayList<DisplayData> getAllRowsUser(MyHelper mobuser)
    {
        ArrayList<DisplayData> data = new ArrayList<DisplayData>();
        SQLiteDatabase SQ = mobuser.getReadableDatabase();
        String col[] = {Tab_info_user.FNAME,Tab_info_user.LNAME,Tab_info_user.MAIL,Tab_info_user.CITY,Tab_info_user.SUM};
        Cursor cob = SQ.query(Tab_info_user.TAB_NAME,col,null,null,null,null,null,null);
        if(cob!=null)
        {
            cob.moveToFirst();
            do {
                DisplayData displayData = new DisplayData();
                displayData.setDis_name(cob.getString(0));
                displayData.setDis_id(cob.getString(1));
                displayData.setDis_zus(cob.getString(2));
                displayData.setDis_ugshil(cob.getString(3));
                displayData.setDis_tsus(cob.getString(4));
                data.add(displayData);
            }while(cob.moveToNext());
        }


        return data;
    }

}