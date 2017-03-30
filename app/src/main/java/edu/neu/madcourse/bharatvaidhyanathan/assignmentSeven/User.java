package edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven;

/**
 * Created by vaidhyanathannarayanan on 03/03/17.
 */

public class User {

    String name;
    String emailID;
    String userID;
    boolean active;
    String regID;
    int gameID;
    String score;
    String country = "United States";

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public User(){
        name = "";
        emailID = "";
        userID = "";
        active = false;
        regID = "";
        gameID = 0;
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
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
