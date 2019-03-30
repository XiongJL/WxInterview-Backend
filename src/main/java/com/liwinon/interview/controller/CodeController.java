package com.liwinon.interview.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.liwinon.interview.entity.Ivuser;
import com.liwinon.interview.entity.Session;
import com.liwinon.interview.service.WXUtil;
import com.liwinon.interview.dao.IvuserDao;
import com.liwinon.interview.dao.SessionDao;

import net.sf.json.JSONObject;

@RestController
public class CodeController {
	@Autowired
	private SessionDao sessionDao;
	@Autowired
	private IvuserDao userDao;
	@Autowired
	private WXUtil util;
	
	/**
	 * 小程序加载时获取openid 和 session_key
	 * 并通过openid 保存用户唯一标识。
	 * @param url
	 * @return
	 */
	@GetMapping(value="/interview/wxapi/code")
	public String getCode(String url,String appid,String secrets,String code,String grant) {
		String param = appid+secrets+code+grant;
		System.out.println("wxApiParam:"+param);
		String key = util.get(url+"?"+param);	
		System.out.println("getCode:"+ key);
		JSONObject req = JSONObject.fromObject(key);
		String session_key = req.getString("session_key");
		String openid = req.getString("openid");
		
		Session table = new Session();   //创建临时会话session表对应的 ORM对象。
		table.setOpenId(openid);
		table.setSessionKey(session_key);
		table.setCreateTime(new Date());
		if(userDao.findByOpenid(openid)!=null){ //如果已经存在这个用户，则什么也不做，反之，创建这个用户并存入数据库	
		}else{
			Ivuser user = new Ivuser();
			user.setOpenid(openid);
			userDao.save(user);
		}
		sessionDao.save(table);  //保存用户的会话及openid到数据库的定时清理表	
		return session_key;
	}
	
	
}
