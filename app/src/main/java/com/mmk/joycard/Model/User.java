package com.mmk.joycard.Model;

public class User {

    private String displayName;
    private String password;
    private String userId;
    private String username;
    private String balance;


    public User() {
    }

    public User(String displayName, String password, String userId, String username, String balance) {
        this.displayName = displayName;
        this.password = password;
        this.userId = userId;
        this.username = username;
        this.balance = balance;
    }

    public User(String displayName, String password, String username, String balance) {
        this.displayName = displayName;
        this.password = password;
        this.username = username;
        this.balance = balance;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
