package data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PROFILE")
public class Profile implements Serializable {

	private static final long serialVersionUID = 1657796579261659521L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "PRIMARY_KEY")
	private String primaryKey;

	@Column(name = "USERID")
	private String userid;

	@Column(name = "PASSWORD")
	private String password;

	public Profile() {
		this.userid = null;
		this.password = null;
		this.primaryKey = "0";
	}

	public Profile(String userid, String password) {
		this.userid = userid;
		this.password = password;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}