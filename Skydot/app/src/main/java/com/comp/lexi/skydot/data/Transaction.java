package com.comp.lexi.skydot.data;

import java.io.Serializable;

public class Transaction implements Serializable {

    private static final long serialVersionUID = 736759228769686073L;

    private int id;
    private String name;
    private String date;
    private double amount;

    public Transaction() {
        this.id = 0;
        this.name = null;
        this.date = null;
        this.amount = 0.0;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}