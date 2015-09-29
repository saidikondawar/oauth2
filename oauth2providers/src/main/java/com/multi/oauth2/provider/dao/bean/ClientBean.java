package com.multi.oauth2.provider.dao.bean;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_client")
public class ClientBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -565865847206373259L;
	private String client_id;
	private String client_secret;
	private String userid;
	private String client_name;
	private String description;
	private String client_url;
	private String client_type;
	private String scope;
	private String redirect_uri;
	private Date regdate;

	public ClientBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClientBean(String client_id, String client_secret, String userid, String client_name, String description, String client_url, String client_type, String scope, String redirect_uri, Date regdate) {
		super();
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.userid = userid;
		this.client_name = client_name;
		this.description = description;
		this.client_url = client_url;
		this.client_type = client_type;
		this.scope = scope;
		this.redirect_uri = redirect_uri;
		this.regdate = regdate;
	}

	@Id
	@Column(name = "client_id", unique = true, nullable = false)
	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	@Column(name = "client_secret")
	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	@Column(name = "userid")
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name = "client_name")
	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "client_url")
	public String getClient_url() {
		return client_url;
	}

	public void setClient_url(String client_url) {
		this.client_url = client_url;
	}

	@Column(name = "client_type")
	public String getClient_type() {
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}

	@Column(name = "scope")
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Column(name = "redirect_uri")
	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	@Column(name = "regdate")
	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

}
