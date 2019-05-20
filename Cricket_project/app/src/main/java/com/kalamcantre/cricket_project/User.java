package com.kalamcantre.cricket_project;

public class User {
    public String username,email,phone_number,image,address;

    User(){


    }

    public User(String username, String email, String phone_number) {
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
    }

    public User(String username, String email, String phone_number, String image, String address) {
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
        this.image = image;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
