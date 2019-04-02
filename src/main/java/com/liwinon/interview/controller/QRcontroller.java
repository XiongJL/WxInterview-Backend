package com.liwinon.interview.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.liwinon.interview.dao.SessionDao;
import com.liwinon.interview.entity.Interview;
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
		String result = qrcode.jionInterview(ivid, session);
		System.out.println("add 返回结果"+result);
		return result;
		
	}
	/**获取前面的人数*/
	@GetMapping(value="/interview/api/HowManyFront")
	public String HowManyFront(String ivid,String session) {
		
		return qrcode.howManyFront(ivid,session);
		
	}
	/**判断用户是否加入了一场面试*/
	@GetMapping(value="/interview/api/DidJion")
	public List<Interview> DidJion(String session) {
		
		return qrcode.didJion(session);
	}
	
	
	/**下载二维码*/
	@GetMapping(value = "/interview/api/downloadQrcode")
	public ResponseEntity<InputStreamResource> entryInfo(String name, HttpServletRequest req) {
		
		String path = "D:\\qrcode\\";
		if (StringUtils.isNotBlank(name)) {
			path = path+name;
			FileSystemResource file = new FileSystemResource(path);
			try {
				String fileName = new String(file.getFilename().getBytes(),"iso8859-1");
				if(file.exists()) {
					HttpHeaders headers =  new HttpHeaders();
					headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
					headers.add("Content-Disposition", "attachment;fileName="+fileName);
					headers.add("Pragma", "no-cache");
					headers.add("Expires", "0");
					return ResponseEntity.ok().headers(headers).contentLength(file.contentLength())
							.contentType(MediaType.parseMediaType("application/octet-stream"))
							.body(new InputStreamResource(file.getInputStream()));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		return null;
	}
}
