package com.example.longtest.Model;

public class UserRegister {
    public String name,username,email,password,latitude,longitude,isSharing,lasttime,userid;

    public UserRegister(String name, String username, String email, String password, String latitude, String longitude, String isSharing, String lasttime, String userid) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isSharing = isSharing;
        this.lasttime = lasttime;
        this.userid = userid;

    }
    public UserRegister()
    {}
}
