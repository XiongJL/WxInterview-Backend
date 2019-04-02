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
	private int type = 0 ;
	//0(未报名)1(已报名 默认)2(面试进行中)3(面试完毕)
	private int state = 1 ;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ivId;
		result = prime * result + ((openid == null) ? 0 : openid.hashCode());
		result = prime * result + ((publishDate == null) ? 0 : publishDate.hashCode());
		result = prime * result + state;
		result = prime * result + type;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ivmember other = (Ivmember) obj;
		if (ivId != other.ivId)
			return false;
		if (openid == null) {
			if (other.openid != null)
				return false;
		} else if (!openid.equals(other.openid))
			return false;
		if (publishDate == null) {
			if (other.publishDate != null)
				return false;
		} else if (!publishDate.equals(other.publishDate))
			return false;
		if (state != other.state)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Ivmember [ivId=" + ivId + ", openid=" + openid + ", type=" + type + ", state=" + state + "]";
	}
	
	
}
