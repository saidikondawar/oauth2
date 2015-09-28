package com.multi.oauth2client;

import java.util.HashMap;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("a", "1");
		map.put("name", "ȫ�浿");
		map.put("tel", "010-222-3333");
		map.put("client_id", "ea90fc1d-9f5b-420f-aa27-a958dc3aedc5");
		map.put("client_secret", "90558ea921fbe151563ce21329867a47be654273");

		System.out.println(Settings.getParamString(map, true));
	}

}
