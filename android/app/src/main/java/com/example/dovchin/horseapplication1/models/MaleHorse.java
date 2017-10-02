package com.example.dovchin.horseapplication1.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Khaliun on 4/9/2017.
 */

public class MaleHorse implements Serializable {
    private String maleHorse_id;
    private String name_horse;
    private String private_number;
    private String breed;
    private String birth_date;
    private String birth_place;
    private String blood;
    private String color_horse;
    private List<String> olymps=new ArrayList<>();
    private List<String> prizes=new ArrayList<>();
    private String group_id;
    private String horse_image;
    private String marking_image;
    private String mother_id;
    private String father_id;
    private Boolean activity;
    private String localpath_img;
    private String localpath_imgMarking;



    public MaleHorse(){

    }

    public String getMaleHorse_id(){return maleHorse_id;}
    public void setMaleHorse_id(String maleHorse_id){
        this.maleHorse_id=maleHorse_id;
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
    public String getGroup_id(){
        return group_id;
    }
    public void setGroup_id(String group_id){this.group_id=group_id;}
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
    public Boolean getActivity(){return activity;}
    public void setActivity(Boolean activity){this.activity=activity;}
    public String getLocalpath_img(){return localpath_img;}
    public String getLocalpath_imgMarking(){return localpath_imgMarking;}
    public void setLocalpath_img(String localpath_img){this.localpath_img=localpath_img;}
    public void setLocalpath_imgMarking(String localpath_imgMarking){this.localpath_imgMarking=localpath_imgMarking;}

    public HashMap<String,Object> MaleHorsetoFirebaseObject() {
        HashMap<String, Object> male_horse = new HashMap<>();
        male_horse.put("male_horse_id",maleHorse_id);
        male_horse.put("name_horse", name_horse);
        male_horse.put("private_number", private_number);
        male_horse.put("breed",breed);
        male_horse.put("blood",blood);
        male_horse.put("color_horse",color_horse);
        male_horse.put("birth_date",birth_date);
        male_horse.put("birth_place",birth_place);
        male_horse.put("mother_id",mother_id);
        male_horse.put("father_id",father_id);
        male_horse.put("marking_image",marking_image);
        male_horse.put("horse_image",horse_image);
        male_horse.put("olymps",olymps);
        male_horse.put("prizes",prizes);
        male_horse.put("group_id",group_id);
        male_horse.put("activity",activity);
        male_horse.put("localpath_img",localpath_img);
        male_horse.put("localpath_markingImg",localpath_imgMarking);
        return  male_horse;
    }

}


