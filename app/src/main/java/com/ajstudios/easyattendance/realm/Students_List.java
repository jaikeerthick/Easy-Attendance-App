package com.ajstudios.easyattendance.realm;

import io.realm.RealmObject;


public class Students_List extends RealmObject {

    String id;
    String name_student;
    String regNo_student;
    String mobileNo_student;
    String class_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_student() {
        return name_student;
    }

    public void setName_student(String name_student) {
        this.name_student = name_student;
    }

    public String getRegNo_student() {
        return regNo_student;
    }

    public void setRegNo_student(String regNo_student) {
        this.regNo_student = regNo_student;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getMobileNo_student() {
        return mobileNo_student;
    }

    public void setMobileNo_student(String mobileNo_student) {
        this.mobileNo_student = mobileNo_student;
    }


}
