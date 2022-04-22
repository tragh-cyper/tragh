package com.sso.shiroweb.entity;

import java.util.List;


public class User {

    private String id;
    private String name;
    private String password;
    private List<String> userRoles;
    private List<String> userPerms;

    public User() {
    }

    public User(String name, String password, List<String> userRoles, List<String> userPerms) {
        this.name = name;
        this.password = password;
        this.userRoles = userRoles;
        this.userPerms = userPerms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }

    public List<String> getUserPerms() {
        return userPerms;
    }

    public void setUserPerms(List<String> userPerms) {
        this.userPerms = userPerms;
    }
}
