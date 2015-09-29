package com.multi.oauth2.provider.view.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.oauth.v2.OAuth2Constant;
import net.oauth.v2.OAuth2ErrorConstant;
import net.oauth.v2.OAuth2Exception;
import net.oauth.v2.OAuth2Scope;
import net.oauth.v2.OAuth2Util;
import net.oauth.v2.RequestAccessTokenVO;
import net.oauth.v2.RequestAuthVO;
import net.oauth.v2.ResponseAccessTokenVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.multi.oauth2.provider.dao.OAuth2HDAO;
import com.multi.oauth2.provider.util.OAuth2AccessTokenService;
import com.multi.oauth2.provider.vo.ClientVO;
import com.multi.oauth2.provider.vo.TokenVO;
import com.multi.oauth2.provider.vo.UserVO;

@Controller
public class OAuth2Controller {

	@Autowired
	private OAuth2HDAO dao;

	@Autowired
	private OAuth2AccessTokenService tokenService;

	private TokenVO createTokenToTable(RequestAuthVO rVO, UserVO uVO, ClientVO cVO) throws OAuth2Exception {
		TokenVO tVO;
		try {
			tVO = new TokenVO();
			tVO.setClient_id(rVO.getClient_id());

			//
			tVO.setUserid(uVO.getUserid());
			tVO.setToken_type(OAuth2Constant.TOKEN_TYPE_BEARER);
			tVO.setScope(rVO.getScope());

			// expires_in refresh_token
			tVO.setExpires_in(OAuth2Constant.EXPIRES_IN_VALUE);
			tVO.setRefresh_token(OAuth2Util.generateToken());
			tVO.setCode(OAuth2Util.generateToken());
			tVO.setClient_type(cVO.getClient_type());

			tVO.setAccess_token(OAuth2Util.generateToken());
			//
			long currentTimeStamp = OAuth2Util.getCurrentTimeStamp();
			tVO.setCreated_at(currentTimeStamp);
			tVO.setCreated_rt(currentTimeStamp);

			System.out.println(tVO);
			// tbl_Toke
			dao.createToken(tVO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new OAuth2Exception(401, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}
		return tVO;
	}

	// Access
	private TokenVO refreshToken(String clientRefreshToken) throws OAuth2Exception {
		TokenVO tempVO = new TokenVO();
		tempVO.setRefresh_token(clientRefreshToken);
		TokenVO tVO = null;
		try {
			tVO = dao.selectRefreshToken(tempVO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}

		if (tVO == null) {
			throw new OAuth2Exception(401, OAuth2ErrorConstant.INVALID_TOKEN);
		}

		tVO.setAccess_token(OAuth2Util.generateToken());
		tVO.setCreated_at(OAuth2Util.getCurrentTimeStamp());

		try {
			dao.updateAccessToken(tVO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(401, OAuth2ErrorConstant.INVALID_TOKEN);
		}

		return tVO;
	}

	// ****Utility

	@RequestMapping(value = "auth", method = RequestMethod.GET)
	public ModelAndView authorize(RequestAuthVO vo, HttpServletResponse response, HttpServletRequest request) throws OAuth2Exception {
		ModelAndView mav = new ModelAndView();

		HttpSession session = request.getSession();
		UserVO loginnedVO = (UserVO) session.getAttribute("userVO");

		// 1.
		if (loginnedVO != null) {
			//
			mav.addObject("isloginned", true);
		} else {
			mav.addObject("isloginned", false);
		}

		System.out.println("## server flow 2.1");
		// 2.1
		ClientVO clientVO1 = new ClientVO();
		clientVO1.setClient_id(vo.getClient_id());
		ClientVO cVO = null;
		try {
			cVO = dao.getClientOne(clientVO1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}

		if (cVO == null) {
			throw new OAuth2Exception(401, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}

		System.out.println("## server flow 2.2");
		System.out.println(vo.getResponse_type());

		// 2.2 response_type
		if (!vo.getResponse_type().equals(OAuth2Constant.RESPONSE_TYPE_CODE) && !vo.getResponse_type().equals(OAuth2Constant.RESPONSE_TYPE_TOKEN)) {
			throw new OAuth2Exception(401, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
		}

		System.out.println("## server flow 3");
		if (!OAuth2Scope.isScopeValid(vo.getScope(), cVO.getScope())) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_SCOPE);
		}

		System.out.println("## server flow 4");
		String gt = vo.getResponse_type();
		if (!gt.equals(OAuth2Constant.RESPONSE_TYPE_CODE) && !gt.equals(OAuth2Constant.RESPONSE_TYPE_TOKEN)) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
		}

		System.out.println("## server flow 5");
		// 5 auth/auth.jsp
		mav.addObject("requestAuthVO", vo);
		mav.addObject("clientVO", cVO);
		mav.setViewName("auth/auth");
		return mav;
	}

	@RequestMapping(value = "auth", method = RequestMethod.POST)
	public String authorizePost(Model model, RequestAuthVO rVO, HttpServletRequest request, HttpServletResponse response) throws OAuth2Exception {
		// 0. request
		String isAllow = request.getParameter("isallow");
		String userid = request.getParameter("userid");
		String password = request.getParameter("password");

		// 0.2 userid,password
		if (!isAllow.equals("true")) {
			return "auth/auth_deny";
		}

		UserVO uVO = null;
		if (userid != null && password != null) {
			UserVO uVOTemp = new UserVO(userid, password, "", 0);
			try {
				uVO = dao.loginProcess(uVOTemp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "auth/auth.jsp";
			}
		} else if (request.getSession().getAttribute("userVO") != null) {
			uVO = (UserVO) request.getSession().getAttribute("userVO");
		} else {
			throw new OAuth2Exception(401, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}

		// 1. client_id
		ClientVO cVOTemp = new ClientVO();
		cVOTemp.setClient_id(rVO.getClient_id());
		System.out.println("## rVO ClientID : " + rVO.getClient_id());
		ClientVO cVO;
		try {
			cVO = dao.getClientOne(cVOTemp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new OAuth2Exception(400, OAuth2ErrorConstant.SERVER_ERROR);
		}

		if (cVO == null) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}

		// 2. scope
		if (!OAuth2Scope.isScopeValid(rVO.getScope(), cVO.getScope())) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_SCOPE);
		}

		// 3. redirect_uri
		if (!rVO.getRedirect_uri().equals(cVO.getRedirect_uri())) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.NOT_MATCH_REDIRECT_URI);
		}

		// 4. token, code �����Ͽ� ���̺� �߰�,
		// refresh token�� ������� ���� ���� ���̺� ����� ������ code �ʵ尪�� �����
		TokenVO tVO = createTokenToTable(rVO, uVO, cVO);

		// 5. response_type Ȯ���ϰ� code, token�� ��쿡 ���� ���� �ٸ� �帧 ó��
		String response_type = rVO.getResponse_type();
		String redirect = "";
		if (response_type.equals(OAuth2Constant.RESPONSE_TYPE_CODE)) {
			// redirect_uri?code=XXXXXXXXX
			redirect = "redirect:" + rVO.getRedirect_uri() + "?code=" + tVO.getCode();
			if (rVO.getState() != null) {
				redirect += "&state=" + rVO.getState();
			}
		} else if (response_type.equals(OAuth2Constant.RESPONSE_TYPE_TOKEN)) {
			// useragent
			ResponseAccessTokenVO tokenVO = null;

			if (OAuth2Constant.USE_REFRESH_TOKEN) {
				tokenVO = new ResponseAccessTokenVO(tVO.getAccess_token(), tVO.getToken_type(), tVO.getExpires_in(), tVO.getRefresh_token(), rVO.getState(), tVO.getCreated_at());
			} else {
				// OAuth2AccessToken .
				tokenVO = new ResponseAccessTokenVO(this.tokenService.generateAccessToken(cVO.getClient_id(), cVO.getClient_secret(), uVO.getUserid(), password), OAuth2Constant.TOKEN_TYPE_BEARER, 0, null, rVO.getState(), tVO.getCreated_at());
				tokenVO.setExpires_in(0);
				tokenVO.setRefresh_token(null);
			}

			String acc = OAuth2Util.getAccessTokenToFormUrlEncoded(tokenVO);
			redirect = "redirect:" + rVO.getRedirect_uri() + "#" + acc;
		} else {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
		}

		return redirect;
	}

	// access_token , refresh_token
	// grant_typ password client_credentials if
	@RequestMapping(value = "token")
	public String accessToken(RequestAccessTokenVO ratVO, Model model, HttpServletRequest request) throws OAuth2Exception {

		String json = "";

		// grant type
		// password, client_credential
		System.out.println("### token flow 1");
		System.out.println("### grant_type : " + ratVO.getGrant_type());

		if (ratVO.getGrant_type().equals(OAuth2Constant.GRANT_TYPE_AUTHORIZATION_CODE)) {
			ResponseAccessTokenVO resVO = accessTokenServerFlow(ratVO, request);
			json = OAuth2Util.getJSONFromObject(resVO);
		} else if (ratVO.getGrant_type().equals(OAuth2Constant.GRANT_TYPE_PASSWORD)) {
			throw new OAuth2Exception(500, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
		} else if (ratVO.getGrant_type().equals(OAuth2Constant.GRANT_TYPE_CLIENT_CREDENTIALS)) {
			throw new OAuth2Exception(500, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
		} else if (ratVO.getGrant_type().equals(OAuth2Constant.GRANT_TYPE_REFRESH_TOKEN)) {
			// refresh token
			if (OAuth2Constant.USE_REFRESH_TOKEN) {
				ResponseAccessTokenVO resVO = refreshTokenFlow(ratVO, request);
				json = OAuth2Util.getJSONFromObject(resVO);
			} else {
				throw new OAuth2Exception(500, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
			}
		} else {
			throw new OAuth2Exception(500, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
		}
		model.addAttribute("json", json);
		return "json/json";
	}

	// grant_type�� authorization_code�� ��
	private ResponseAccessTokenVO accessTokenServerFlow(RequestAccessTokenVO ratVO, HttpServletRequest request) throws OAuth2Exception {

		// GET Client ID Client Secret Authorization Header
		System.out.println("### token flow 2");
		if (request.getMethod().equalsIgnoreCase("GET")) {
			String authHeader = (String) request.getHeader("Authorization");
			if (authHeader == null || authHeader.equals("")) {
				throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_PARAMETER);
			}
			// Basic
			OAuth2Util.parseBasicAuthHeader(authHeader, ratVO);
		}

		// 1. ClientID, Secret
		System.out.println("### token flow 3");
		if (ratVO.getClient_id() == null || ratVO.getClient_secret() == null) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_PARAMETER);
		}

		ClientVO cVOTemp = new ClientVO();
		cVOTemp.setClient_id(ratVO.getClient_id());
		cVOTemp.setClient_secret(ratVO.getClient_secret());
		ClientVO cVO = null;
		try {
			cVO = dao.getClientOne(cVOTemp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}

		// 1.2 client
		System.out.println("### token flow 4");
		if (cVO == null) {
			throw new OAuth2Exception(401, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}

		// 2. redirect_uri
		System.out.println("### token flow 5");
		if (!ratVO.getRedirect_uri().equals(cVO.getRedirect_uri())) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.NOT_MATCH_REDIRECT_URI);
		}

		// 3. code
		System.out.println("### token flow 6");
		if (ratVO.getCode() == null) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_PARAMETER);
		}

		TokenVO tVOTemp = new TokenVO();
		tVOTemp.setCode(ratVO.getCode());
		TokenVO tVO = null;
		try {
			tVO = dao.selectTokenByCode(tVOTemp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}

		if (tVO == null) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_CODE);
		}

		// 4. expire , refresh token
		if (OAuth2Constant.USE_REFRESH_TOKEN) {
			System.out.println("### token flow 7");
			long expires = tVO.getCreated_at() + tVO.getExpires_in();
			if (System.currentTimeMillis() > expires) {
				// code access token
				throw new OAuth2Exception(400, OAuth2ErrorConstant.EXPIRED_TOKEN);
			}
		}

		// 5. state
		// 6. ResponseAccessToken --> json
		System.out.println("### token flow 9");
		ResponseAccessTokenVO resVO = new ResponseAccessTokenVO();

		resVO.setIssued_at(tVO.getCreated_at());
		resVO.setState(ratVO.getState());
		resVO.setToken_type(tVO.getToken_type());
		if (OAuth2Constant.USE_REFRESH_TOKEN) {
			resVO.setAccess_token(tVO.getAccess_token());
			resVO.setExpires_in(tVO.getExpires_in());
			resVO.setRefresh_token(tVO.getRefresh_token());
		} else {
			// 6.1. password UserVO
			// ResponsToken token
			UserVO uVOTemp = new UserVO();
			uVOTemp.setUserid(tVO.getUserid());
			UserVO uVO = null;
			try {
				uVO = dao.getUserInfo(uVOTemp);
			} catch (Exception e) {
				e.printStackTrace();
				throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
			}

			if (uVO == null) {
				throw new OAuth2Exception(500, OAuth2ErrorConstant.INVALID_USER);
			}

			// token ���̺� ���ڵ� ����
			try {
				dao.deleteToken(tVO);
			} catch (Exception e) {
				e.printStackTrace();
				throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
			}

			resVO.setAccess_token(this.tokenService.generateAccessToken(cVO.getClient_id(), cVO.getClient_secret(), uVO.getUserid(), uVO.getPassword()));
		}

		return resVO;

	}

	// grant_type authorization_code
	private ResponseAccessTokenVO refreshTokenFlow(RequestAccessTokenVO ratVO, HttpServletRequest request) throws OAuth2Exception {
		// 1. refresh Token
		// GET Client ID Client Secret Authorization Header
		if (request.getMethod().equalsIgnoreCase("GET")) {
			String authHeader = (String) request.getHeader("Authorization");
			if (authHeader == null || authHeader.equals("")) {
				throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_PARAMETER);
			}
			// Basic
			OAuth2Util.parseBasicAuthHeader(authHeader, ratVO);
		}

		// 2. ClientID, Secret -->
		if (ratVO.getClient_id() == null || ratVO.getClient_secret() == null) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_PARAMETER);
		}

		// 3. clientID client_secret
		ClientVO cVOTemp = new ClientVO();
		cVOTemp.setClient_id(ratVO.getClient_id());
		ClientVO cVO = null;
		try {
			cVO = dao.getClientOne(cVOTemp);
		} catch (Exception e) {
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}

		if (cVO == null) {
			throw new OAuth2Exception(500, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}

		if (ratVO.getClient_secret() != null && !cVO.getClient_secret().equals(ratVO.getClient_secret())) {
			throw new OAuth2Exception(500, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}

		// 4. refresh token
		if (ratVO.getRefresh_token() == null) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_PARAMETER);
		}

		TokenVO tVOTemp = new TokenVO();
		tVOTemp.setRefresh_token(ratVO.getRefresh_token());
		TokenVO tVO = null;
		try {
			tVO = dao.selectRefreshToken(tVOTemp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}

		if (tVO == null) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_TOKEN);
		}

		// 5. TokenVOs accessToken --> DB
		// --> refreshToken ResponseAccessTokenVO --> JSON
		tVO.setAccess_token(OAuth2Util.generateToken());
		tVO.setCreated_at(OAuth2Util.getCurrentTimeStamp());
		try {
			dao.updateAccessToken(tVO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}

		ResponseAccessTokenVO resVO = new ResponseAccessTokenVO(tVO.getAccess_token(), tVO.getToken_type(), tVO.getExpires_in(), null, ratVO.getState(), tVO.getCreated_at());

		return resVO;
	}
}
