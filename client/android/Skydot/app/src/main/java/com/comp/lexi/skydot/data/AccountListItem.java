package com.comp.lexi.skydot.data;

import com.comp.lexi.skydot.utils.Variables;

import java.io.Serializable;

public class AccountListItem implements Serializable {

    private static final long serialVersionUID = 736759228769686073L;

    private String userid;
    private int id;
    private String name;
    private String type; // banking, borrowing, investing
    private double balanceCAD;
    private double balanceUSD;

    public AccountListItem() {
        this.userid = null;
        this.id = 0;
        this.name = null;
        this.type = Variables.AccountType.BANKING.getValue();
        this.balanceCAD = 0.0;
        this.balanceUSD = 0.0;
    }

    public AccountListItem(Account account) {
        this.userid = account.getUserid();
        this.id = account.getId();
        this.name = account.getName();
        this.type = account.getType();
        this.balanceCAD = account.getCAD();
        this.balanceUSD = account.getUSD();
    }

    public boolean equals(AccountListItem other) {
        return other.getId() == this.id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalanceCAD() {
        return balanceCAD;
    }

    public void setBalanceCAD(double balanceCAD) {
        this.balanceCAD = balanceCAD;
    }

    public double getBalanceUSD() {
        return balanceUSD;
    }

    public void setBalanceUSD(double balanceUSD) {
        this.balanceUSD = balanceUSD;
    }

}