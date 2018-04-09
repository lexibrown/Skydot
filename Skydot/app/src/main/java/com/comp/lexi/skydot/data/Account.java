package com.comp.lexi.skydot.data;

import com.comp.lexi.skydot.utils.Variables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {

    private static final long serialVersionUID = -6429506504199295664L;

    private String userid;
    private int id;
    private String name;
    private String type; // banking, borrowing, investing
    private double CAD;
    private double USD;

    private int status;
    
    private List<Transaction> transactions = new ArrayList<>();

    public Account() {
        this.userid = null;
        this.id = 0;
        this.name = null;
        this.type = Variables.AccountType.BANKING.getValue();
        this.CAD = 0.0;
        this.USD = 0.0;
        this.transactions = new ArrayList<Transaction>();
        this.status = 0;
    }

    public boolean equals(Account other) {
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

    public double getCAD() {
        return CAD;
    }

    public void setCAD(double CAD) {
        this.CAD = CAD;
    }

    public double getUSD() {
        return USD;
    }

    public void setUSD(double USD) {
        this.USD = USD;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}