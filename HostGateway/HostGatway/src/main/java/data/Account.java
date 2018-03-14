package data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ACCOUNT")
public class Account implements Serializable {

	private static final long serialVersionUID = 736759228769686073L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "PRIMARY_KEY")
	private String primaryKey;

	@Column(name = "ID")
	private int id;

	@Column(name = "USERID")
	private String userid;

	@Column(name = "NAME")
	private String name;

	@Column(name = "TYPE")
	private String type; // banking, borrowing, investing

	@Column(name = "CAD")
	private double balanceCAD;

	@Column(name = "USD")
	private double balanceUSD;

	public Account() {
		this.id = 0;
		this.userid = null;
		this.name = null;
		this.type = null;
		this.balanceCAD = 0.0;
		this.balanceUSD = 0.0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("userid", getUserid());
		map.put("id", getId());
		map.put("name", getName());
		map.put("type", getType());
		map.put("CAD", getBalanceCAD());
		map.put("USD", getBalanceUSD());

		return map;
	}

}