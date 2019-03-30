package com.liwinon.interview.service;

public interface QrService {
	//发起一场面试
	public String startInterview(String session,String startTime,String duringTime,String location);
	//参加一场面试
	public String jionInterview(String ivid,String session);
}
