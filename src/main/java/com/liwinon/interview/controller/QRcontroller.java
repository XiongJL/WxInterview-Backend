package com.liwinon.interview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.liwinon.interview.dao.SessionDao;
import com.liwinon.interview.service.QrService;



@RestController
public class QRcontroller {
	@Autowired
	QrService qrcode;
	/**发起人发起一场面试,创建一个二维码*/
	@GetMapping(value="/interview/qrCode/create")
	public String createQrcode(String session,String startTime,String duringTime,String location) {
		System.out.println(session+" "+startTime+" "+duringTime+" "+location);
		String qrname = qrcode.startInterview(session, startTime, duringTime,location);
		System.out.println("创建二维码返回值为:"+qrname);
		return qrname;
	}
	
	/**参加一场面试,用户扫描二维码后发起请求.此链接作为 createQrcode 方法中二维码的一部分.*/
	@GetMapping(value="/interview/qrCode/addInterview")
	public String addin(String ivid,String session) {
		System.out.println(ivid+" ------ "+session);

		
		return null;
		
	}
}
