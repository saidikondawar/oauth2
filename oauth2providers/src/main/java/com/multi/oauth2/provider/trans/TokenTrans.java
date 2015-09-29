package com.multi.oauth2.provider.trans;

import com.multi.oauth2.provider.dao.bean.TokenBean;
import com.multi.oauth2.provider.dao.bean.TokenEmb;
import com.multi.oauth2.provider.vo.TokenVO;

public class TokenTrans {

	public static TokenBean getBeanToVo(TokenVO vo) {

		TokenBean bean = new TokenBean();

		TokenEmb emb = new TokenEmb();

		emb.setAccess_token(vo.getAccess_token());
		emb.setClient_id(vo.getClient_id());
		emb.setUserid(vo.getUserid());
		bean.setTokenEmb(emb);
		bean.setClient_type(vo.getClient_type());
		bean.setCode(vo.getCode());
		bean.setCreated_at(vo.getCreated_at());
		bean.setCreated_rt(vo.getCreated_rt());
		bean.setExpires_in(vo.getExpires_in());
		bean.setRefresh_token(vo.getRefresh_token());
		bean.setScope(vo.getScope());
		bean.setState(vo.getState());
		bean.setToken_type(vo.getToken_type());

		return bean;
	}

	static public TokenVO getBeanToVo(TokenBean vo) {

		TokenVO bean = new TokenVO();

		TokenEmb emb = vo.getTokenEmb();

		bean.setAccess_token(emb.getAccess_token());
		bean.setClient_id(emb.getClient_id());
		bean.setUserid(emb.getUserid());
		bean.setClient_type(vo.getClient_type());
		bean.setCode(vo.getCode());
		bean.setCreated_at(vo.getCreated_at());
		bean.setCreated_rt(vo.getCreated_rt());
		bean.setExpires_in(vo.getExpires_in());
		bean.setRefresh_token(vo.getRefresh_token());
		bean.setScope(vo.getScope());
		bean.setState(vo.getState());
		bean.setToken_type(vo.getToken_type());

		return bean;
	}

}
