package com.agri.chattla.model;

import java.io.Serializable;

public class UserFirbase implements Serializable {

    private String id;
    private String phoneNumber ;
    private String UserName ;
    private String PassWord ;
    private String Age ;
    private String Gov ;
    private String Cit ;
    private String Info ;
    private String U_C ;
    private String status;
    private String field;
    private String consultId;
    private String degree;
    private String experience;
    private String FcmToken;
    private String topic;
    private String topic_two;
    private String field_two;
    private String topic_three;
    private String field_three;
    private String rate;
    private String profile;
    private double balance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTopic_three() {
        return topic_three;
    }

    public void setTopic_three(String topic_three) {
        this.topic_three = topic_three;
    }

    public String getField_three() {
        return field_three;
    }

    public void setField_three(String field_three) {
        this.field_three = field_three;
    }

    public String getTopic_two() {
        return topic_two;
    }

    public void setTopic_two(String topic_two) {
        this.topic_two = topic_two;
    }

    public String getField_two() {
        return field_two;
    }

    public void setField_two(String field_two) {
        this.field_two = field_two;
    }

    public String getFcmToken() {
        return FcmToken;
    }

    public void setFcmToken(String fcmToken) {
        FcmToken = fcmToken;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getConsultId() {
        return consultId;
    }

    public void setConsultId(String consultId) {
        this.consultId = consultId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserFirbase() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserFirbase(String phoneNumber, String userName, String passWord, String age, String gov, String cit, String info, String u_c) {
        this.phoneNumber = phoneNumber;
        this.UserName = userName;
        this.PassWord = passWord;
        this.Age = age;
        this.Gov = gov;
        this.Cit = cit;
        this.Info = info;
        this.U_C = u_c ;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        this.PassWord = passWord;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        this.Age = age;
    }

    public String getGov() {
        return Gov;
    }

    public void setGov(String gov) {
        this.Gov = gov;
    }

    public String getCit() {
        return Cit;
    }

    public void setCit(String cit) {
        this.Cit = cit;
    }

    public String getInfo() {
        return Info;
    }

    public String getU_C() {
        return U_C;
    }

    public void setU_C(String u_C) {
        U_C = u_C;
    }

    public void setInfo(String info) {
        this.Info = info;
    }
}
