package com.multi.oauth2.provider.dao.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_token")
public class TokenBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8739076640562649718L;
	private String refresh_token;
	private String token_type;
	private String scope;
	private String code;
	private String state;
	private String client_type;
	private long created_at;
	private long created_rt;
	private long expires_in;
	private TokenEmb tokenEmb;

	public TokenBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Column(name = "refresh_token")
	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	@Column(name = "token_type")
	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	@Column(name = "scope")
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "created_at")
	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	@Column(name = "created_rt")
	public long getCreated_rt() {
		return created_rt;
	}

	public void setCreated_rt(long created_rt) {
		this.created_rt = created_rt;
	}

	@Column(name = "expires_in")
	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	@Column(name = "state")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "client_type")
	public String getClient_type() {
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}

	@EmbeddedId
	public TokenEmb getTokenEmb() {
		return tokenEmb;
	}

	public void setTokenEmb(TokenEmb tokenEmb) {
		this.tokenEmb = tokenEmb;
	}

}
