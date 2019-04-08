package com.radiusagent.radius;
public class Users {
    private String name;
    private String email;
    private String age;
    private String gender;
    private String dp;
    Users(){}
    Users(String name,String email,String age,String gender,String dp) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.dp = dp;
    }
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getAge() {return age;}
    public void setAge(String age) {this.age = age;}
    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}
    public String getDp() {return dp;}
    public void setDp(String thumbnail) {this.dp = dp;}
}