package com.aks.evaccinationapp;

public class Child {

    public String age, bloodGrp, gender, name;

    public Child(){

    }

    public Child(String name, String age, String gender, String bloodGrp) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodGrp = bloodGrp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }
}
