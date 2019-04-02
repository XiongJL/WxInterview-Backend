package com.liwinon.interview.service;

import java.util.List;

import com.liwinon.interview.entity.Interview;

public interface QrService {
	//发起一场面试
	public String startInterview(String session,String startTime,String duringTime,String location);
	//参加一场面试
	public String jionInterview(String ivid,String session);
	
	//api ,判断是否参加了一场面试 ,返回面试场次信息的对象数组
	public List<Interview> didJion(String session);
	
	//api , 判断前面还有多少个人
	public String howManyFront(String ivid,String session);
}
