package com.liwinon.interview.entity;

import java.io.Serializable;

/**
 * 联合主键类
 * 
 * @author XIongJL
 *
 */
public class IvmemberPK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 20190327L;
	// 面试ID
	private int ivId;
	// 参加者的id
	private String openid;
	public IvmemberPK() {}; //两种构造函数
	public IvmemberPK(int ivId, String openid) {
		super();
		this.ivId = ivId;
		this.openid = openid;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ivId;
		result = prime * result + ((openid == null) ? 0 : openid.hashCode());
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
		IvmemberPK other = (IvmemberPK) obj;
		if (ivId != other.ivId)
			return false;
		if (openid == null) {
			if (other.openid != null)
				return false;
		} else if (!openid.equals(other.openid))
			return false;
		return true;
	}
	
}
