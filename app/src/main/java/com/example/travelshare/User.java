package com.example.travelshare;

public class User {
    String fullName, username,Email,Phoneno, password;

    public User() {
    }

    public User(String fullName, String username, String email, String phoneno, String password) {
        this.fullName = fullName;
        this.username = username;
        Email = email;
        Phoneno = phoneno;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneno() {
        return Phoneno;
    }

    public void setPhoneno(String phoneno) {
        Phoneno = phoneno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
