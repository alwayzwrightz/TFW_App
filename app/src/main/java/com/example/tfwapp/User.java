package com.example.tfwapp;

public class User {

    public String name, email;
    public int red, green, blue;

    public User(){

    }

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public User(int red, int green, int blue){
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}
