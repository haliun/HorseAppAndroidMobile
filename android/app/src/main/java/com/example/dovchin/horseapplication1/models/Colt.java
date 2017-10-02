package com.example.dovchin.horseapplication1.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Khaliun on 4/9/2017.
 */

public class Colt implements Serializable {
    private String name_horse;
    private String colt_id;
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
    private String localpath_img;
    private String localpath_imgMarking;
    private Boolean activity;

    public Colt(){

    }

    public String getName_horse(){
        return name_horse;
    }
    public String getColt_id(){
        return colt_id;
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
    public void setColt_id(String colt_id){this.colt_id=colt_id;}
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
    public HashMap<String,Object> ColtToFirebaseObject() {
        HashMap<String, Object> colts = new HashMap<>();
        colts.put("colt_id",colt_id);
        colts.put("name_horse", name_horse);
        colts.put("private_number", private_number);
        colts.put("breed",breed);
        colts.put("blood",blood);
        colts.put("color_horse",color_horse);
        colts.put("birth_date",birth_date);
        colts.put("birth_place",birth_place);
        colts.put("mother_id",mother_id);
        colts.put("father_id",father_id);
        colts.put("localpath_img",localpath_img);
        colts.put("localpath_imgmarking",localpath_imgMarking);
        colts.put("marking_image",marking_image);
        colts.put("horse_image",horse_image);
        colts.put("group",group);
        colts.put("activity",activity);
        return  colts;
    }
}
