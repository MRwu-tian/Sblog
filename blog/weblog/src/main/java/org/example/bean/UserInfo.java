package org.example.bean;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class UserInfo implements Serializable {
    private int id;
    private String userSerial;
    private String username;
    private String userPassword;
    private String userGender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date userBirthday;
    private String userPosition;
    private String userEducation;
    private String userSynopsis;
    private String userShool;
    private String userPictureIndex;
    private int role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserSerial() {
        return userSerial;
    }

    public void setUserSerial(String userSerial) {
        this.userSerial = userSerial;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public Date getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public String getUserEducation() {
        return userEducation;
    }

    public void setUserEducation(String userEducation) {
        this.userEducation = userEducation;
    }

    public String getUserSynopsis() {
        return userSynopsis;
    }

    public void setUserSynopsis(String userSynopsis) {
        this.userSynopsis = userSynopsis;
    }

    public String getUserShool() {
        return userShool;
    }

    public void setUserShool(String userShool) {
        this.userShool = userShool;
    }

    public String getUserPictureIndex() {
        return userPictureIndex;
    }

    public void setUserPictureIndex(String userPictureIndex) {
        this.userPictureIndex = userPictureIndex;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", userSerial='" + userSerial + '\'' +
                ", username='" + username + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userGender='" + userGender + '\'' +
                ", userBirthday=" + userBirthday +
                ", userPosition='" + userPosition + '\'' +
                ", userEducation='" + userEducation + '\'' +
                ", userSynopsis='" + userSynopsis + '\'' +
                ", userShool='" + userShool + '\'' +
                ", userPictureIndex='" + userPictureIndex + '\'' +
                ", role=" + role +
                '}';
    }
}