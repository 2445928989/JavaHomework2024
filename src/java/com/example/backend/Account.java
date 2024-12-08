package com.example.backend;

public class Account {

    private int accountId;
    private String accountName;
    private String accountpassword;
    public Account() {}
    public Account(int accountId, String accountName, String accountpassword) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountpassword = accountpassword;
    }
    public int getAccountId() {
        return accountId;
    }
}
