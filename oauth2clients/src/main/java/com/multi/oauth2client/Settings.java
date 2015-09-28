package com.multi.oauth2client;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Settings {
	public static final String CLIENT_ID = "ea90fc1d-9f5b-420f-aa27-a958dc3aedc5";
	public static final String CLIENT_KEY = "90558ea921fbe151563ce21329867a47be654273";
	public static final String AUTHORIZE_URL="http://localhost:8000/oauth2provider/oauth2/auth";
	public static final String ACCES_TOKEN_URL = "http://localhost:8000/oauth2provider/oauth2/token";
	public static final String RESPONSE_TYPE = "code";
	public static final String REDIRECT_URI = "http://localhost:8000/oauth2client/callback.jsp";
	public static final String PERSONAL_INFO_URL = "http://localhost:8000/oauth2provider/resource/myinfo.do";
	public static final String SCOPE = "reademail,sendemail,readboard,calendar,personalinfo";

	public static String getParamString(HashMap<String,String> map, boolean useAuthHeader) throws Exception {
		int i=0;
		String strout = "";
		for (String key : map.keySet()) {
			if (useAuthHeader == true && 
					(key.equals("client_id") || key.equals("client_secret") )) {
				continue;
			}
			if (i !=0 )	strout += "&";
			strout += key + "=" + URLEncoder.encode(map.get(key), "utf-8");
			i++;
		}
				
		return strout;
	}
	
	public static String generateRandomState() {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			String base = ""+System.currentTimeMillis();
			byte[] hash = sha1.digest(base.getBytes());
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
