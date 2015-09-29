package com.multi.oauth2.provider.trans;

import com.multi.oauth2.provider.dao.bean.ClientBean;
import com.multi.oauth2.provider.vo.ClientVO;

public class ClientTrans {

	static public ClientBean getBeanToVo(ClientVO vo) {
		ClientBean bean = new ClientBean();

		bean.setClient_id(vo.getClient_id());
		bean.setClient_name(vo.getClient_name());
		bean.setClient_secret(vo.getClient_secret());
		bean.setClient_type(vo.getClient_type());
		bean.setDescription(vo.getDescription());
		bean.setRedirect_uri(vo.getRedirect_uri());
		bean.setRegdate(vo.getRegdate());
		bean.setScope(vo.getScope());
		bean.setUserid(vo.getUserid());

		return bean;

	}

	static public ClientVO getVoToBean(ClientBean vo) {
		ClientVO bean = new ClientVO();

		bean.setClient_id(vo.getClient_id());
		bean.setClient_name(vo.getClient_name());
		bean.setClient_secret(vo.getClient_secret());
		bean.setClient_type(vo.getClient_type());
		bean.setDescription(vo.getDescription());
		bean.setRedirect_uri(vo.getRedirect_uri());
		bean.setRegdate(vo.getRegdate());
		bean.setScope(vo.getScope());
		bean.setUserid(vo.getUserid());

		return bean;

	}

}
