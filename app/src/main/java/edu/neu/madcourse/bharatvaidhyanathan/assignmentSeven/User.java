package edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven;

/**
 * Created by vaidhyanathannarayanan on 03/03/17.
 */

public class User {

    String name;
    String emailID;
    String userID;
    boolean isActive;
    String regID;

    public User(){
        name = "";
        emailID = "";
        userID = "";
        isActive = false;
        regID = "";
    }


    public String getEmailID() {
        return emailID;
    }

    public String getName() {
        return name;
    }

    public String getRegID() {
        return regID;
    }

    public String getUserID() {
        return userID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
