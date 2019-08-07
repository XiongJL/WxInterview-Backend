package com.liwinon.interview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.liwinon.interview.dao.InterviewDao;
import com.liwinon.interview.entity.Interview;
import com.liwinon.interview.entity.Ivuser;
import com.liwinon.interview.service.QrService;

@RestController
public class InterviewController {
	@Autowired
	QrService service;
	@Autowired
	InterviewDao ivDao;
	/**
	 * 	获取正在进行的以及尚未开始的面试
	 * @return
	 */
	@GetMapping(value="/interview/api/getInterview")
	public List<Interview> getInterview() {
		
		return service.getInterview();
	}
	/**
	 * 	获取正在进行的以及尚未开始的面试人数
	 * @return
	 */
	@GetMapping(value="/interview/api/getInterviewNums")
	public int[] getInterviewNums() {
		return service.getInterviewNums();
	}
	/**
	 * 	获取指定ivid号的面试信息
	 * @param ivid
	 * @return
	 */
	@GetMapping(value="/interview/api/getInterview/{ivid}")
	public Interview getInterviewByIvid(@PathVariable String ivid) {
		System.out.println("getInterview ivid is :"+ivid);
		return ivDao.findByIvId(Integer.valueOf(ivid));
	}
	/**
	 * 	获取此次面试的队头人员
	 * @param ivid
	 * @return
	 */
	@GetMapping(value="/interview/api/getThisQueue/{ivid}")
	public Ivuser getThisQueue(@PathVariable String ivid) {
		return service.getThisQueue(ivid);
	}
	/**
	 * 	下一个面试人员, 出队操作
	 * @param ivid
	 * @return
	 */
	@GetMapping(value="/interview/api/nextOne/{ivid}")
	public String nextOne(@PathVariable String ivid) {
		return service.nextOne(ivid);
	}
	
	@GetMapping(value="/interview/api/finish/{ivid}")
	public String finishInterview(@PathVariable String ivid) {
		
		return service.finishInterview(ivid);
		
	}
	/** 	开启指定的面试 
	 * @return 
	 */
	@GetMapping(value="/interview/api/openInterview/{ivid}")
	public String openInterview(@PathVariable String ivid) {
		return service.openInterview(ivid);	
	}
}
