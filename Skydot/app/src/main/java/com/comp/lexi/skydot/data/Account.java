package com.comp.lexi.skydot.data;

import com.comp.lexi.skydot.utils.Variables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account implements Serializable {

    private static final long serialVersionUID = -6429506504199295664L;

    private int id;
    private String name;
    private String type; // banking, borrowing, investing
    private double balanceCAD;
    private double balanceUSD;

    private List<Transaction> history = new ArrayList<>();

    public Account() {
        this.id = 0;
        this.name = null;
        this.type = Variables.AccountType.BANKING.getValue();
        this.balanceCAD = 0.0;
        this.balanceUSD = 0.0;
        this.history = new ArrayList<Transaction>();
    }

    public boolean equals(Account other) {
        return other.getId() == this.id;
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

    public List<Transaction> getHistory() {
        return history;
    }

    public void setHistory(List<Transaction> history) {
        this.history = history;
    }

}