package com.example.dovchin.horseapplication1;

/**
 * Created by dovchin on 8/9/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import java.util.ArrayList;

public class MyHelper extends SQLiteOpenHelper {

    final  String CREATE_QUERY = "create table " + TabInfoStallion.TAB_NAME +
            " ( " + TabInfoStallion.KEY_ID + " integer primary key autoincrement, " + TabInfoStallion.ANAME + " text not null, " + TabInfoStallion.AID + " text not null , "
            + TabInfoStallion.AUGSHIL + " text not null , " + TabInfoStallion.AZUS+ " text not null , " + TabInfoStallion.ATSUS + " text   , "
            + TabInfoStallion.AGAZAR + " text   , " + TabInfoStallion.AOGNOO + " text   , "
           + TabInfoStallion.AZURAG + " blob not null,"+ TabInfoStallion.AETSEG + " text , " + TabInfoStallion.AEH + " text );";

    final  String CREATE_TABLE_MORI = "create table " + TabInfoMaleHorse.MTAB_NAME +
            " ( " + TabInfoMaleHorse.KEY_ID + " integer primary key autoincrement, " + TabInfoMaleHorse.MANAME + " text not null, " + TabInfoMaleHorse.MAID + " text not null , "
            + TabInfoMaleHorse.MAUGSHIL + " text not null , " + TabInfoMaleHorse.MAZUS+ " text not null , " + TabInfoMaleHorse.MATSUS + " text   , "
            + TabInfoMaleHorse.MAGAZAR + " text   , " + TabInfoMaleHorse.MAOGNOO + " text   , "
            + TabInfoMaleHorse.MAZURAG + " blob ,"+ TabInfoMaleHorse.MAETSEG + " text , " + TabInfoMaleHorse.MAEH + " text );";


    Context ctx;


    public  MyHelper(Context context)
    {
        super(context, TabInfoStallion.DB_NAME,null,4);
        ctx=context;



    }

    @Override    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        db.execSQL(CREATE_TABLE_MORI);
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TabInfoStallion.TAB_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TabInfoMaleHorse.MTAB_NAME);
        onCreate(db);
    }


    public  void putInfo(MyHelper mob,String name,String id_azarga, String zus, String ugshil, String tsus, String gazar, String ognoo, byte [] zurag,String etseg,String eh)
    {
        SQLiteDatabase SQ= mob.getWritableDatabase();
        ContentValues CV=new ContentValues();
        CV.put(TabInfoStallion.ANAME, name);
        CV.put(TabInfoStallion.AID,id_azarga);
        CV.put(TabInfoStallion.AZUS, zus);
        CV.put(TabInfoStallion.AUGSHIL, ugshil);
        CV.put(TabInfoStallion.ATSUS, tsus);
        CV.put(TabInfoStallion.AGAZAR, gazar);
        CV.put(TabInfoStallion.AOGNOO, ognoo);
       // CV.put(TabInfoStallion.ATAMGA, tamga);
        CV.put(TabInfoStallion.AZURAG, zurag);
        CV.put(TabInfoStallion.AETSEG,etseg);
        CV.put(TabInfoStallion.AEH,eh);

        SQ.insert(TabInfoStallion.TAB_NAME, null, CV);
        Toast.makeText(ctx,"Амжилттай нэмэгдлээ!",Toast.LENGTH_LONG).show();
    }
    public  void putInfoMori(MyHelper mobm,String mname,String mid_azarga, String mzus, String mugshil, String mtsus, String mgazar, String mognoo, byte [] mzurag,String metseg,String meh)
    {
        SQLiteDatabase SQ= mobm.getWritableDatabase();
        ContentValues CV=new ContentValues();
        CV.put(TabInfoMaleHorse.MANAME, mname);
        CV.put(TabInfoMaleHorse.MAID,mid_azarga);
        CV.put(TabInfoMaleHorse.MAZUS, mzus);
        CV.put(TabInfoMaleHorse.MAUGSHIL, mugshil);
        CV.put(TabInfoMaleHorse.MATSUS, mtsus);
        CV.put(TabInfoMaleHorse.MAGAZAR, mgazar);
        CV.put(TabInfoMaleHorse.MAOGNOO, mognoo);
        // CV.put(TabInfoStallion.ATAMGA, tamga);
        CV.put(TabInfoMaleHorse.MAZURAG, mzurag);
        CV.put(TabInfoMaleHorse.MAETSEG,metseg);
        CV.put(TabInfoMaleHorse.MAEH,meh);

        SQ.insert(TabInfoMaleHorse.MTAB_NAME, null, CV);
        Toast.makeText(ctx,"Амжилттай нэмэгдлээ!",Toast.LENGTH_LONG).show();
    }
    public ArrayList<DisplayData> getAllRows(MyHelper mob)
    {
        ArrayList<DisplayData> data = new ArrayList<DisplayData>();
        SQLiteDatabase SQ = mob.getReadableDatabase();
        String col[] = {TabInfoStallion.ANAME, TabInfoStallion.AID, TabInfoStallion.AUGSHIL, TabInfoStallion.AZUS, TabInfoStallion.ATSUS, TabInfoStallion.AGAZAR, TabInfoStallion.AOGNOO, TabInfoStallion.AZURAG, TabInfoStallion.AETSEG, TabInfoStallion.AEH};
        Cursor cob = SQ.query(TabInfoStallion.TAB_NAME,col,null,null,null,null,null,null);
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
                displayData.setDis_gazar(cob.getString(5));
                displayData.setDis_ognoo(cob.getString(6));
              //  displayData.setDis_tamga(cob.getBlob(8));
                displayData.setDis_zurag(cob.getBlob(7));
                displayData.setDis_etseg(cob.getString(8));
                displayData.setDis_eh(cob.getString(9));
                data.add(displayData);
            }while(cob.moveToNext());
       }

        Toast.makeText(ctx,"Retrieved",Toast.LENGTH_LONG).show();
        return data;
    }
   public ArrayList<DisplayData> getAllRowsMori(MyHelper mmobm)
    {
        ArrayList<DisplayData> data = new ArrayList<DisplayData>();
        SQLiteDatabase SQ = mmobm.getReadableDatabase();
        String col[] = {TabInfoMaleHorse.MANAME, TabInfoMaleHorse.MAID, TabInfoMaleHorse.MAUGSHIL, TabInfoMaleHorse.MAZUS, TabInfoMaleHorse.MATSUS, TabInfoMaleHorse.MAGAZAR, TabInfoMaleHorse.MAOGNOO, TabInfoMaleHorse.MAZURAG, TabInfoMaleHorse.MAETSEG, TabInfoMaleHorse.MAEH};
        Cursor cob = SQ.query(TabInfoMaleHorse.MTAB_NAME,col,null,null,null,null,null,null);
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
                displayData.setDis_gazar(cob.getString(5));
                displayData.setDis_ognoo(cob.getString(6));
                //  displayData.setDis_tamga(cob.getBlob(8));
                displayData.setDis_zurag(cob.getBlob(7));
                displayData.setDis_etseg(cob.getString(8));
                displayData.setDis_eh(cob.getString(9));
                data.add(displayData);
            }while(cob.moveToNext());
        }


        return data;
    }
}