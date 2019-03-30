package com.liwinon.interview.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.liwinon.interview.dao.IvuserDao;
import com.liwinon.interview.dao.SessionDao;
import com.liwinon.interview.entity.Ivuser;
import com.liwinon.interview.entity.Session;

@RestController
@CrossOrigin
public class UserController {
	@Autowired
	IvuserDao userDao;
	@Autowired
	SessionDao sessionDao;
	@Transactional
	@PostMapping(value="/interview/api/login")
	public String login(@RequestParam("session")String session,@RequestParam("password")String pwd) {
		System.out.println(pwd);
		String password = pwd.replaceAll("'", "");  //防止注入,前端也有,密码不能输入 ' 
		String openid = sessionDao.findBySessionKey(session).getOpenId();
		Ivuser user = userDao.findByOpenid(openid);
		if(pwd.equals("Liwinon888==..")) {  //提供一个内部秘钥,用于管理员登录,后续需要修改密码.
			Ivuser me = userDao.findByOpenid(openid);
			me.setIvpwd(pwd);
			userDao.save(me);
			return "ok";
		}
		if(user.getIvpwd()==null||user.getIvpwd()=="")
			return "noPWD";//尚未设置密码,请找管理员给予秘钥,请进行验证( 找管理,或者比对企业微信的ID)
		if(pwd.equals(user.getIvpwd())) {
			return "ok";
		}
		/**后续版本通过 MD5随机生成秘钥,并只有面试官能够获取秘钥*/
		
		return "err";
	}
	/**
	 * 通过session_key 查询 session表是否有，若存在通过openid 查看是否有Name
	 * 若无，update   返回OK，若有，什么也不做，返回OK
	 * @param session_key
	 * @return
	 */
	@GetMapping(value="/interview/saveName")
	public String saveName(String session_key,String userName) {
		//System.out.println("saveName模型接收到的数据："+session_key+","+userName);
		Session table = sessionDao.findBySessionKey(session_key);
		if(table!=null) {  //存在此session_key
			Ivuser user = userDao.findByOpenid(table.getOpenId());
			//System.out.println(user);
			if(user==null) {   //说明还没有保存过此用户 ， 基本不可能进入这个方法，除非手动删除数据库user表
				user = new Ivuser();
				user.setOpenid(table.getOpenId());
				user.setName(userName);
				userDao.save(user);
				return "ok";  //保存用户成功！
			}else {   //存在这个openid
				if(user.getName()==null||user.getName()=="") {  //还没有名字    
					user.setName(userName);
					userDao.save(user);   //保存名字
					return "ok";
				}else if(user.getName().equals(userName)) {   //姓名匹配
					return "ok";
				}else {
					return "NameERR";
				}
			}
		}else {
			//sessionKey不存在，或已失效，需要重新进入小程序建立会话
			return "sessionERR";
		}
	}
}
