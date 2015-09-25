package com.multi.oauth2.provider.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.multi.oauth2.provider.vo.ClientVO;
import com.multi.oauth2.provider.vo.TokenVO;
import com.multi.oauth2.provider.vo.UserVO;

@Repository
public class OAuth2DAO extends SqlMapClientDaoSupport {
	@Autowired
	public void setMySqlMapClient(SqlMapClient sqlMapClient) {
		this.setSqlMapClient(sqlMapClient);
	}

	public UserVO loginProcess(UserVO vo) throws Exception {
		return (UserVO) this.getSqlMapClient().queryForObject("login",
				vo);

	}

	public UserVO getUserInfo(UserVO vo) throws Exception {
		return (UserVO) this.getSqlMapClient().queryForObject(
				"userinfo", vo);
	}
	
	public List<ClientVO> getClientList(UserVO vo) throws Exception {
		return (List<ClientVO>)this.getSqlMapClient().queryForList("clientlist", vo);
	}
	
	public ClientVO getClientOne(ClientVO vo) throws Exception {
		return (ClientVO)this.getSqlMapClient().queryForObject("clientone", vo);
	}
	
	public void deleteClient(ClientVO vo) throws Exception {
		this.getSqlMapClient().delete("deleteclient", vo.getClient_id());
	}
	
	public void insertClient(ClientVO vo) throws Exception {
		this.getSqlMapClient().insert("insertclient", vo);
	}
	
	
	public void createToken(TokenVO vo) throws Exception {
		this.getSqlMapClient().insert("createToken", vo);
	}
	
	public TokenVO selectRefreshToken(TokenVO vo) throws Exception {
		return (TokenVO)this.getSqlMapClient().queryForObject("selectRefreshToken", vo);
	}
	public TokenVO selectToken(TokenVO vo) throws Exception {
		return (TokenVO)this.getSqlMapClient().queryForObject("selectToken", vo);
	}
	
	public TokenVO selectTokenByCode(TokenVO vo) throws Exception {
		return (TokenVO)this.getSqlMapClient().queryForObject("selectTokenByCode", vo);
	}
	
	public void updateAccessToken(TokenVO vo) throws Exception {
		this.getSqlMapClient().update("updateAccessToken", vo);
	}
	
	public void deleteExpiredToken(TokenVO vo) throws Exception {
		this.getSqlMapClient().delete("deleteExpiredToken", vo);
	}
	
	public void deleteToken(TokenVO vo) throws Exception {
		this.getSqlMapClient().delete("deleteToken", vo);
	}
	
	public void deleteExpiredToken(long ms) throws Exception {
		this.getSqlMapClient().delete("deleteExpiredToken", ms);
	}

}
