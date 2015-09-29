package com.multi.oauth2.provider.dao.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_users")
public class UserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8732118001667411293L;
	private String userid;
	private String password;
	private String username;
	private long userno;

	public UserBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserBean(String userid, String password, String username, long userno) {
		super();
		this.userid = userid;
		this.password = password;
		this.username = username;
		this.userno = userno;
	}

	@Id
	@Column(name = "userid", unique = true, nullable = false)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "userno")
	public long getUserno() {
		return userno;
	}

	public void setUserno(long userno) {
		this.userno = userno;
	}

}
