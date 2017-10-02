package com.example.dovchin.horseapplication1.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Khaliun on 6/18/2017.
 */

public class Money implements Serializable {
    private String id;
    private String name;
    private String date;
    private String sum;
    private String type;
    private String desc;
    public Money(){

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName(){
        return name;
    }
    public String getDate(){
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public String getSum() {
        return sum;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public void setType(String type) {
        this.type = type;
    }
    public HashMap<String,Object> MoneyToFirebaseObject() {
        HashMap<String, Object> money = new HashMap<>();
        money.put("money_id",id);
        money.put("name", name);
        money.put("date", date);
        money.put("sum",sum);
        money.put("description",desc);
        money.put("type",type);
        return  money;
    }
}
