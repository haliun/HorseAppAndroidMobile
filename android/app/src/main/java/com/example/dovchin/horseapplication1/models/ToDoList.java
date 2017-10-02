package com.example.dovchin.horseapplication1.models;

import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;

/**
 * Created by Khaliun on 4/6/2017.
 */

public class ToDoList implements Serializable{
    private String name;
    private String description;
    private String date;
    public ToDoList(){

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

    public HashMap<String,String> toFirebaseObject() {
        HashMap<String, String> todo = new HashMap<String, String>();
        todo.put("name", name);
        todo.put("description", description);
        todo.put("date",date);

        return  todo;
    }

}
