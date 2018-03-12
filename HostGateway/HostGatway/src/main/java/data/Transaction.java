package data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TRANSACTION")
public class Transaction implements Serializable {

	private static final long serialVersionUID = 736759228769686073L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "PRIMARY_KEY")
	private String primaryKey;

	@Column(name = "ID")
	private int id;

	@Column(name = "ACCOUNTID")
	private int accountid;

	@Column(name = "USERID")
	private String userid;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DATE")
	private String date;

	@Column(name = "AMOUNT")
	private double amount;

	public Transaction() {
		this.id = 0;
		this.accountid = 0;
		this.userid = null;
		this.name = null;
		this.date = null;
		this.amount = 0.0;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccountid() {
		return accountid;
	}

	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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
