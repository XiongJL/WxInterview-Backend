package com.liwinon.interview.service;

import java.util.List;

public interface WXUtil {
	//发起HTTP  get 请求
	public String reqGet(String url, String param) ;
	//通过HTTPClient 发起get请求
	public String get(String url);
	/** 发起POST请求
	 *  @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 */
	public String reqPost(String url, String param);
	
	//获取用户的 openId
	public String getOpenId(String session_key);
	
}
