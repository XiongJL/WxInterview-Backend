package com.liwinon.interview.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "ivmember")
@IdClass(IvmemberPK.class)   //联合主键类
public class Ivmember {
	// 面试ID
	@Id
	private int ivId;
	//参加者的id
	@Id
	private String openid;
	//0(面试者 默认) 1(面试官) 2(被邀请成为面试官) 3(拒绝成为面试官)
	private int type;
	//0(未报名)1(已报名 默认)2(面试进行中)3(面试完毕)
	private int state;
	//扫描参加时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date publishDate;
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "Ivmember [ivId=" + ivId + ", openid=" + openid + ", type=" + type + ", state=" + state + "]";
	}
	
	
}
