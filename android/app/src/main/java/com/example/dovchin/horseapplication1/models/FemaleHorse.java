package com.example.dovchin.horseapplication1.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Khaliun on 4/9/2017.
 */

public class FemaleHorse implements Serializable {
    private String name_horse;
    private String female_id;
    private String private_number;
    private String breed;
    private String birth_date;
    private String birth_place;
    private String blood;
    private String color_horse;
    private String group;
    private String horse_image;
    private String marking_image;
    private String mother_id;
    private String father_id;
    private Boolean activity;
    private String localpath_img;
    private String localpath_imgMarking;

    public FemaleHorse(){

    }
    public FemaleHorse(String name_horse, String female_id, String private_number, String breed, String birth_place,
                       String birth_date, String blood, String color_horse, String group, String horse_image, String marking_image,
                       String mother_id, String father_id){
        this.name_horse=name_horse;
        this.private_number=private_number;
        this.breed=breed;
        this.birth_date=birth_date;
        this.birth_place=birth_place;
        this.blood=blood;
        this.female_id=female_id;
        this.color_horse=color_horse;
        this.group=group;
        this.horse_image=horse_image;
        this.marking_image=marking_image;
        this.mother_id=mother_id;
        this.father_id=father_id;
    }
    public String getName_horse(){
        return name_horse;
    }
    public String getFemale_id(){
        return female_id;
    }
    public String getPrivate_number(){
        return private_number;
    }
    public String getBreed(){
        return breed;
    }
    public String getBirth_date(){
        return birth_date;
    }
    public String getBirth_place(){
        return birth_place;
    }
    public String getBlood(){
        return blood;
    }
    public String getColor_horse(){
        return color_horse;
    }
    public String getGroup(){
        return group;
    }
    public String getMother_id(){
        return mother_id;
    }
    public String getFather_id(){
        return father_id;
    }
    public String getHorse_image(){
        return horse_image;
    }
    public String getMarking_image(){
        return marking_image;
    }
    public String getLocalpath_img(){return localpath_img;}
    public String getLocalpath_imgMarking(){return localpath_imgMarking;}
    public void setName_horse(String name_horse){this.name_horse=name_horse;}
    public void setFemale_id(String female_id){this.female_id=female_id;}
    public void setPrivate_number(String private_number){this.private_number=private_number;}
    public void setBreed(String breed){this.breed=breed;}
    public void setBirth_date(String birth_date){this.birth_date=birth_date;}
    public void setBirth_place(String birth_place){this.birth_place=birth_place;}
    public void setBlood(String blood){this.blood=blood;}
    public void setColor_horse(String color_horse){this.color_horse=color_horse;}
    public void setGroup(String group){this.group=group;}
    public void setHorse_image(String horse_image){this.horse_image=horse_image;}
    public void setMarking_image(String marking_image){this.marking_image=marking_image;}
    public void setMother_id(String mother_id){this.mother_id=mother_id;}
    public void setFather_id(String father_id){this.father_id=father_id;}
    public void setLocalpath_img(String localpath_img){this.localpath_img=localpath_img;}
    public void setLocalpath_imgMarking(String localpath_imgMarking){this.localpath_imgMarking=localpath_imgMarking;}
    public Boolean getActivity(){return activity;}
    public void setActivity(Boolean activity){this.activity=activity;}
    public HashMap<String,Object> FemaleToFirebaseObject() {
        HashMap<String, Object> female_horses = new HashMap<>();
        female_horses.put("female_id",female_id);
        female_horses.put("name_horse", name_horse);
        female_horses.put("private_number", private_number);
        female_horses.put("breed",breed);
        female_horses.put("blood",blood);
        female_horses.put("color_horse",color_horse);
        female_horses.put("birth_date",birth_date);
        female_horses.put("birth_place",birth_place);
        female_horses.put("mother_id",mother_id);
        female_horses.put("father_id",father_id);
        female_horses.put("marking_image",marking_image);
        female_horses.put("horse_image",horse_image);
        female_horses.put("group",group);
        female_horses.put("activity",activity);
        female_horses.put("localpath_img",localpath_img);
        female_horses.put("localpath_markingImg",localpath_imgMarking);
        return  female_horses;
    }
}
