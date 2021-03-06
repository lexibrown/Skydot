package data;

import java.io.Serializable;

public class Account implements Serializable {

    private static final long serialVersionUID = 736759228769686073L;

    private int id;
    private String name;
    private String type; // banking, borrowing, investing
    private double balanceCAD;
    private double balanceUSD;

    public Account() {
        this.id = 0;
        this.name = null;
        this.type = null;
        this.balanceCAD = 0.0;
        this.balanceUSD = 0.0;
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

}