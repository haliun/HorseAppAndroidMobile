package com.example.dovchin.horseapplication1.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Khaliun on 4/9/2017.
 */

public class Olymp implements Serializable{
    private String olymp_id;
    private String olymp_name;
    private String type;
    private String date;
    private String place;
    private String category;
    private String num_horses;
    private String description;
    private List<String> prize;
    private String horse_id;
    public Olymp(){}

    public String getOlymp_id(){return olymp_id;}
    public String getOlymp_name(){return olymp_name;}
    public String getType(){return type;}
    public String getDate(){return date;}
    public String getPlace(){return place;}
    public String getCategory(){return category;}
    public String getNum_horses(){return num_horses;}
    public String getDescription(){return description;}
    public List<String> getPrizes(){return prize;}
    public String  getHorse_id(){return horse_id;}
    public void setOlymp_id(String olymp_id){this.olymp_id=olymp_id;}
    public void setOlymp_name(String olymp_name){this.olymp_name=olymp_name;}
    public void setType(String type){this.type=type;}
    public void setDate(String date){this.date=date;}
    public void setPlace(String place){this.place=place;}
    public void setCategory(String category){this.category=category;}
    public void setNum_horses(String num_horses){this.num_horses=num_horses;}
    public void setDescription(String description){this.description=description;}
    public void setPrizes(List<String> prize){this.prize=prize;}
    public void setHorse_id(String horse_id){this.horse_id=horse_id;}
    public HashMap<String,Object> OlymptoFirebaseObject() {
        HashMap<String, Object> olymps = new HashMap<>();
        olymps.put("olymp_id",olymp_id);
        olymps.put("olymp_name", olymp_name);
        olymps.put("type", type);
        olymps.put("date",date);
        olymps.put("place",place);
        olymps.put("category",category);
        olymps.put("number_horses",num_horses);
        olymps.put("description",description);
        olymps.put("prizes",prize);
        olymps.put("horse_id",horse_id);
        return  olymps;
    }
}
