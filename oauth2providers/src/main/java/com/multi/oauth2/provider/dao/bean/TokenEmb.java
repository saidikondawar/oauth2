package com.multi.oauth2.provider.dao.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TokenEmb implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4736898748734760193L;
	private String client_id;
	private String userid;
	private String access_token;

	@Column(name = "client_id")
	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	@Column(name = "userid")
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name = "access_token")
	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

}
