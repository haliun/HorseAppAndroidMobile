package com.example.dovchin.horseapplication1.models;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Khaliun on 4/9/2017.
 */
@IgnoreExtraProperties
public class User implements Serializable {

    private String first_name;
    private String last_name;
    private String email;
    private String city;
    private String district;
    private String password;
    private String localPath_img;
    private String profile_img;
    private String degree;
    private String userkey;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public String getFirst_name(){return first_name;}
    public String getLast_name(){return last_name;}
    public String getEmail(){return email;}
    public String getCity(){return city;}
    public String getDistrict(){return district;}
    public String getPassword(){return password;}
    public String getLocalPath_img() {
        return localPath_img;
    }
    public String getDegree() {
        return degree;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setLocalPath_img(String localPath_img) {
        this.localPath_img = localPath_img;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }
    public HashMap<String,Object> UserToFirebase() {
        HashMap<String, Object> users = new HashMap<>();
        users.put("first_name",first_name);
        users.put("last_name",last_name);
        users.put("password",password);
        users.put("id",userkey);
        users.put("email",email);
        users.put("city",city);
        users.put("district",district);
        users.put("degree",degree);
        users.put("profile_img",profile_img);
        users.put("localpath_img",localPath_img);
        return users;
    }
}