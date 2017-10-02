package com.example.dovchin.horseapplication1.models;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Khaliun on 4/9/2017.
 */

public class Stallion implements Serializable {
    private String stallion_id;
    private String name_horse;
    private String private_number;
    private String breed;
    private String birth_date;
    private String birth_place;
    private String blood;
    private String color_horse;
    private List<String> olymps=new ArrayList<>();
    private List<String> prizes=new ArrayList<>();
    private List<String> group=new ArrayList<>();
    private String horse_image;
    private String marking_image;
    private Boolean activity;
    private String mother_id;
    private String localpath_img;
    private String localpath_imgMarking;
    private String father_id;


    public Stallion(){

    }
    public Stallion(String stallion_id,String name_horse, String private_number, String breed, String birth_date, String birth_place,
                    String blood, String color_horse, ArrayList<String> olymps, ArrayList<String> prizes, ArrayList<String> group, String horse_image,
                    String marking_image, String mother_id, String father_id){
        this.stallion_id=stallion_id;
        this.name_horse=name_horse;
        this.private_number=private_number;
        this.breed=breed;
        this.birth_date=birth_date;
        this.birth_place=birth_place;
        this.blood=blood;
        this.color_horse=color_horse;
        this.olymps=olymps;
        this.prizes=prizes;
        this.group=group;
        this.horse_image=horse_image;
        this.marking_image=marking_image;
        this.mother_id=mother_id;
        this.father_id=father_id;

    }
    public String getStallion_id(){return stallion_id;}
    public void setStallion_id(String stallion_id){
        this.stallion_id=stallion_id;
    }
    public String getName_horse(){
        return  name_horse;
    }
    public void setName_horse(String name_horse){
        this.name_horse=name_horse;
    }
    public String getPrivate_number(){
        return  private_number;
    }
    public void setPrivate_number(String private_number){
        this.private_number=private_number;
    }
    public String getBreed(){
        return breed;
    }
    public void setBreed(String breed){
        this.breed=breed;
    }
    public String getBirth_date(){
        return birth_date;
    }
    public void setBirth_date(String birth_place){
        this.birth_date=birth_place;
    }
    public String getBirth_place(){
        return birth_place;
    }
    public void setBirth_place(String birth_place){
        this.birth_place=birth_place;
    }
    public String getBlood(){
        return blood;
    }
    public void setBlood(String blood){
        this.blood=blood;
    }
    public String getColor_horse(){
        return color_horse;
    }
    public void setColor_horse(String color_horse){
        this.color_horse=color_horse;
    }
    public String getMarking_image(){
        return marking_image;
    }
    public void setMarking_image(String marking_image){
        this.marking_image=marking_image;
    }
    public String getHorse_image(){
        return horse_image;
    }
    public void setHorse_image(String horse_image){
        this.horse_image=horse_image;
    }
    public List<String> getOlymps(){
        return olymps;
    }
    public List<String> getPrizes(){
        return  prizes;
    }
    public List<String> getGroup(){
        return group;
    }
    public String getLocalpath_img(){return localpath_img;}
    public String getLocalpath_imgMarking(){return localpath_imgMarking;}
    public String getMother_id(){
        return mother_id;
    }
    public String getFather_id(){
        return father_id;
    }
    public void setMother_id(String mother_id){
        this.mother_id=mother_id;
    }
    public void setFather_id(String father_id){
        this.father_id=father_id;
    }
    public void setLocalpath_img(String localpath_img){this.localpath_img=localpath_img;}
    public void setLocalpath_imgMarking(String localpath_imgMarking){this.localpath_imgMarking=localpath_imgMarking;}
    public Boolean getActivity(){return activity;}
    public void setActivity(Boolean activity){this.activity=activity;}
    public HashMap<String,Object> StalliontoFirebaseObject() {
        HashMap<String, Object> stallions = new HashMap<>();
        stallions.put("stallion_id",stallion_id);
        stallions.put("name_horse", name_horse);
        stallions.put("private_number", private_number);
        stallions.put("breed",breed);
        stallions.put("blood",blood);
        stallions.put("color_horse",color_horse);
        stallions.put("birth_date",birth_date);
        stallions.put("birth_place",birth_place);
        stallions.put("mother_id",mother_id);
        stallions.put("father_id",father_id);
        stallions.put("marking_image",marking_image);
        stallions.put("horse_image",horse_image);
        stallions.put("olymps",olymps);
        stallions.put("prizes",prizes);
        stallions.put("group",group);
        stallions.put("activity",activity);
        stallions.put("localpath_img",localpath_img);
        stallions.put("localpath_imgmarking",localpath_imgMarking);
        return  stallions;
    }

}
