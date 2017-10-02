package com.example.dovchin.horseapplication1.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Khaliun on 4/9/2017.
 */

public class Prize implements Serializable{
    private String name_prize;
    private String prize_id;
    private String date_prize;
    private String description;
    private String horse_id;
    private String olymp_name;
    public Prize(){}
    public String getName_prize(){ return name_prize;}
    public String getPrize_id(){ return prize_id;}
    public String getDatePrize(){return date_prize;}
    public String getDescription(){return description;}
    public String getHorse_id(){return horse_id;}
    public String getOlymp_name(){return olymp_name;}
    public void setName_prize(String name_prize){this.name_prize=name_prize;}
    public void setPrize_id(String prize_id){this.prize_id=prize_id;}
    public void setDate_prize(String date_prize){this.date_prize=date_prize;}
    public void setDescription(String description){this.description=description;}
    public void setHorse_id(String horse_id){this.horse_id=horse_id;}
    public void setOlymp_name(String olymp_name){this.olymp_name=olymp_name;}
    public HashMap<String,Object> PrizetoFirebaseObject() {
        HashMap<String, Object> prize = new HashMap<>();
        prize.put("prize_id",prize_id);
        prize.put("name_prize", name_prize);
        prize.put("date", date_prize);
        prize.put("horse_id",horse_id);
        prize.put("olymp_name",olymp_name);
        prize.put("description",description);
        return  prize;
    }





}
