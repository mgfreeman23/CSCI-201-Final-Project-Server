package com.collegeapp;
import java.util.ArrayList;
import java.util.List;

public class User {
	// Variables
    private int userID;
    private String emailAddress;
    private String username;
    private String password;
    private int age;
    private String major;
    private String hometown;
    private List<String> hobbies;
    private String instagram;
    // Default constructor
    public User() {
        this.userID = -1;
        this.emailAddress = "";
        this.username = "";
        this.password = "";
        this.age = 0;
        this.major = "";
        this.hometown = "";
        this.hobbies = new ArrayList<>();
        this.instagram = "";
    }
    // Initializing constructor
    public User(int userID, String emailAddress, String username, String password, int age,
                String major, String hometown, List<String> hobbies, String instagram) {
        this.userID = userID;
        this.emailAddress = emailAddress;
        this.username = username;
        this.password = password;
        this.age = age;
        this.major = major;
        this.hometown = hometown;
        this.hobbies = hobbies;
        this.instagram = instagram;
    }
    // Getters and setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setSocialMediaLinks(String instagram) {
        this.instagram = instagram;
    }
}
