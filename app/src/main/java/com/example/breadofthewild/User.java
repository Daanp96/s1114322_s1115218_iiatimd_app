package com.example.breadofthewild;

import java.io.Serializable;

public class User implements Serializable {
    public String username;
    public String token;

    public User() {

    }

    public User(String username, String token) {
        this.username = username;
        this.token = token;

    }

    public String getUsername() { return username; }

    public String getToken() { return  token; }

    public void setUsername(String name) { this.username = username; }

    public void setToken(String token) { this.token = token; }
}
