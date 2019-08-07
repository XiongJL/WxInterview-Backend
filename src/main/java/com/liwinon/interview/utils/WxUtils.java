package com.liwinon.interview.utils;

import java.util.Calendar;

import com.liwinon.interview.entity.AccessToken;
import com.liwinon.interview.service.HttpUtil;
import com.liwinon.interview.service.HttpUtilImpl;

import net.sf.json.JSONObject;

public class WxUtils {
	/**
	 * 获取 企业微信的 Access_token
	 * 
	 * @param id      corpid 企业ID,不是应用ID
	 * @param secrect corpsecret 应用的凭证密钥
	 * @return
	 */
	private static String AgentId = "wx9b0a872925d2df23";
	private static String Secret = "9f909a5fbabbb1205ca753dc733e6d31";
	public static AccessToken AccessToken = new AccessToken();

	public static String reqAccess_token(String id, String secrect) {
		// 使用Auto注入 在static内中无法使用，它指向空，可以在Spring中使用set注入
		HttpUtil http = new HttpUtilImpl();
		String url = "https://api.weixin.qq.com/cgi-bin/token";
		String param = "grant_type=client_credential&appid=" + id + "&secret=" + secrect;
		String result = http.reqGet(url, param);
		return result;
	}
	/**
	 *	获取静态AccessToken类.
	 * @return
	 */
	public static AccessToken getAccess_token() {
		// {"errcode":0,"errmsg":"ok","access_token":"91-FR....jw","expires_in":7200}
		System.out.println("开始发送GET请求.");
		JSONObject json = JSONObject.fromObject(reqAccess_token(AgentId, Secret));
		System.out.println(json);
		AccessToken.setAccess_token(json.getString("access_token"));
		AccessToken.setExpires_in(json.getInt("expires_in"));
		//token.setType(type);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.SECOND, json.getInt("expires_in"));
		AccessToken.setInvalidTime(now.getTime());
		System.out.println("token:" + AccessToken);
		return AccessToken;
	}
}
