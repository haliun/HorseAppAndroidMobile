package com.example.dovchin.horseapplication1.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Khaliun on 4/28/2017.
 */

public class FoodVit implements Serializable {
    private String name;
    private String food_id;
    private String type;
    private String description;
    private String date;
    private String quantity;
    private String sum;
    public FoodVit(){

    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date=date;
    }
    public String getFood_id(){return food_id;}
    public void setFood_id(String food_id){this.food_id=food_id;}
    public String getType(){return type;}
    public void setType(String type){this.type=type;}
    public String getQuantity(){return quantity;}
    public void setQuantity(String quantity){this.quantity=quantity;}
    public String getSum(){return sum;}
    public void setSum(String sum){this.sum=sum;}
    public HashMap<String,String> FoodtoHashMap() {
        HashMap<String, String> foodVit = new HashMap<String, String>();
        foodVit.put("name", name);
        foodVit.put("description", description);
        foodVit.put("date",date);
        foodVit.put("sum",sum);
        foodVit.put("quantity",quantity);
        foodVit.put("food_id",food_id);
        foodVit.put("type",type);
        return  foodVit;
    }
}
