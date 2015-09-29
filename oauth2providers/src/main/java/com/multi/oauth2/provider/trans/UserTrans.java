package com.multi.oauth2.provider.trans;

import com.multi.oauth2.provider.dao.bean.UserBean;
import com.multi.oauth2.provider.vo.UserVO;

public class UserTrans {

	public static UserBean getBeanFromVO(UserVO vo) {

		UserBean bean = new UserBean();

		bean.setPassword(vo.getPassword());
		bean.setUserid(vo.getUserid());
		bean.setUsername(vo.getUsername());
		bean.setUserno(vo.getUserno());

		return bean;
	}

	public static UserVO getBeanFromVO(UserBean vo) {

		UserVO bean = new UserVO();

		bean.setPassword(vo.getPassword());
		bean.setUserid(vo.getUserid());
		bean.setUsername(vo.getUsername());
		bean.setUserno(vo.getUserno());

		return bean;
	}

}
