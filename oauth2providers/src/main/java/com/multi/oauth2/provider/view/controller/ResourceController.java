package com.multi.oauth2.provider.view.controller;

import javax.servlet.http.HttpServletRequest;

import net.oauth.v2.OAuth2Constant;
import net.oauth.v2.OAuth2ErrorConstant;
import net.oauth.v2.OAuth2Exception;
import net.oauth.v2.OAuth2Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.multi.oauth2.provider.dao.OAuth2HDAO;
import com.multi.oauth2.provider.vo.ClientVO;
import com.multi.oauth2.provider.vo.TokenVO;
import com.multi.oauth2.provider.vo.UserVO;

/*
 * 
 */

@Controller
public class ResourceController {

	@Autowired
	private OAuth2HDAO dao;

	@RequestMapping(value = "resource/myinfo.do", method = RequestMethod.GET)
	public String getMyInfo(Model model, HttpServletRequest request) throws OAuth2Exception {

		TokenVO tVO = (TokenVO) request.getAttribute(OAuth2Constant.RESOURCE_TOKEN_NAME);

		// 1. User
		UserVO uVOTemp = new UserVO();
		uVOTemp.setUserid(tVO.getUserid());
		UserVO uVO = null;
		try {
			uVO = dao.getUserInfo(uVOTemp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}
		uVO.setPassword(null); //

		// 2. Client Ap
		//
		ClientVO cVOTemp = new ClientVO();
		cVOTemp.setClient_id(tVO.getClient_id());

		ClientVO cVO = null;
		try {
			cVO = dao.getClientOne(cVOTemp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}
		String jsonClient = OAuth2Util.getJSONFromObject(cVO);
		System.out.println("##Client : " + jsonClient);

		String jsonUser = OAuth2Util.getJSONFromObject(uVO);
		System.out.println("##user : " + jsonUser);
		model.addAttribute("json", jsonUser);

		return "json/json";
	}
}
