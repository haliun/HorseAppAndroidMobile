package com.example.dovchin.horseapplication1.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Khaliun on 6/18/2017.
 */

public class Stable implements Serializable {
    private String id;
    private String name;
    private String city;
    private String district;
    private String type;
    private String season;
    private String num_horse;
    private String num_person;
    private String main_person;
    private String sun;
    private String grass;
    private String water;
    private String square;
    public Stable(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getGrass() {
        return grass;
    }

    public String getMain_person() {
        return main_person;
    }

    public String getName() {
        return name;
    }

    public String getNum_horse() {
        return num_horse;
    }

    public String getNum_person() {
        return num_person;
    }

    public String getSeason() {
        return season;
    }

    public String getSun() {
        return sun;
    }

    public String getType() {
        return type;
    }

    public String getWater() {
        return water;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setGrass(String grass) {
        this.grass = grass;
    }

    public void setMain_person(String main_person) {
        this.main_person = main_person;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum_horse(String num_horse) {
        this.num_horse = num_horse;
    }

    public void setNum_person(String num_person) {
        this.num_person = num_person;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
    }

    public HashMap<String,Object> StableToFirebase() {
        HashMap<String, Object> stable = new HashMap<>();
        stable.put("id",id);
        stable.put("name", name);
        stable.put("type", type);
        stable.put("city",city);
        stable.put("district",district);
        stable.put("number_horses",num_horse);
        stable.put("number_personals",num_person);
        stable.put("main_person",main_person);
        stable.put("season",season);
        stable.put("sqaure",square);
        stable.put("water",water);
        stable.put("sun",sun);
        stable.put("grass",grass);
        return  stable;
    }
}

