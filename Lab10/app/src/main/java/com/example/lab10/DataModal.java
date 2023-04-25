package com.example.lab10;

public class DataModal {

    // string variables for our name and stuff
    private String uname;
    private String pass;

    public DataModal(String uname, String pass) {
        this.uname = uname;
        this.pass = pass;
    }

    public String getName() {
        return uname;
    }

    public void setName(String name) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}