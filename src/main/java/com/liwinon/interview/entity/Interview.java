package com.liwinon.interview.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="interview")
public class Interview {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//面试ID
	private int ivId;
	//发起者openid
	private String openid;
	//面试地点
	private String location;
	//创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date publishTime;
	//开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date startTime;
	//每场面试大约持续时间(15min)
	private int duringTime;
	//二维码信息
	private String codeInfo;
	//二维码路径
	private String codeImg;
	//面试状态 0(面试未开始)1(面试进行中)2(面试结束)
	private int ivType;
	
	
	public int getIvType() {
		return ivType;
	}
	public void setIvType(int ivType) {
		this.ivType = ivType;
	}
	public String getLocation() {
		return location;
	}
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getIvId() {
		return ivId;
	}

	public void setIvId(int ivId) {
		this.ivId = ivId;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getDuringTime() {
		return duringTime;
	}

	public void setDuringTime(int duringTime) {
		this.duringTime = duringTime;
	}

	public String getCodeInfo() {
		return codeInfo;
	}

	public void setCodeInfo(String codeInfo) {
		this.codeInfo = codeInfo;
	}

	public String getCodeImg() {
		return codeImg;
	}

	public void setCodeImg(String codeImg) {
		this.codeImg = codeImg;
	}
	@Override
	public String toString() {
		return "Interview [ivId=" + ivId + ", openid=" + openid + ", location=" + location + ", publishTime="
				+ publishTime + ", startTime=" + startTime + ", duringTime=" + duringTime + ", codeInfo=" + codeInfo
				+ ", codeImg=" + codeImg + ", ivType=" + ivType + "]";
	}
	
}
