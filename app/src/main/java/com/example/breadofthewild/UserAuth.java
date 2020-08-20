package com.example.breadofthewild;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

public class UserAuth {
    private static UserAuth instance;
    private Context context;

    private String username;
    private String email;
    private String password;
    private String jwt;
    private boolean isAuth;

    private SharedPreferences sp;
    private User user;

    private final String userData = "userData";

    private UserAuth(Context context) {
        this.context = context;
    }

    public static synchronized UserAuth getInstance(Context context) {
        if( instance == null ) {
            instance = new UserAuth(context);
        }
        return instance;
    }

    //SETTERS

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setJWT(String jwt) {
        this.jwt = jwt;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public void setPreferences(SharedPreferences sp) {
        this.sp = sp;
    }

    public void setUser(User user) {
        this.user = user;
    }


    //GETTERS

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getJwt() {
        return jwt;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public SharedPreferences getSp() {
        return sp;
    }

    public User getUser() {
        return user;
    }

    //STORE SHARED PREFERENCES
    public void storeInSp(User user) {
        SharedPreferences.Editor spEditor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        spEditor.putString(userData, json);
        spEditor.commit();
    }

    //RETRIEVE SHAREDPREFERENCES
    public User retrieveFromSp(String key) {
        Gson gson = new Gson();
        String json = sp.getString(key, "");
        User user = gson.fromJson(json, User.class);
        Log.d("retrievesp", user.getUsername());
        return user;
    }




}

