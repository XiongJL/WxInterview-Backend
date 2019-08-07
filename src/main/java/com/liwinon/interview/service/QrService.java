package com.liwinon.interview.service;

import java.util.List;


import com.liwinon.interview.entity.Interview;
import com.liwinon.interview.entity.Ivuser;

public interface QrService {
	//发起一场面试
	public String startInterview(String session,String startTime,String duringTime,String location);
	public String startInterviewByWX(String session,String startTime,String duringTime,String location);
	
	//参加一场面试
	public String jionInterview(String ivid,String session);
	
	//api ,判断是否参加了一场面试 ,返回面试场次信息的对象数组
	public List<Interview> didJion(String session);
	
	//api , 判断前面还有多少个人
	public String howManyFront(String ivid,String session);
	
	//api , 查询正在进行和尚未开始的面试信息.
	public List<Interview> getInterview();
	//api , 查询正在进行和尚未开始的面试每场人数
	public int[] getInterviewNums();
	
	//api ,返回指定面试的队头人员
	public Ivuser getThisQueue(String ivid);
	
	//api , 完成对头人员面试,并返回下一个人员信息
	public String nextOne(String ivid);
	
	//api, 结束指定面试
	public String finishInterview(String ivid);
	
	//api, 开启指定的面试
	public String openInterview(String ivid);
}
