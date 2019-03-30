package com.liwinon.interview.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ivuser")
public class Ivuser {
	@Id
	private String openid;
	
	private String ivpwd;

	private String name; 
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getIvpwd() {
		return ivpwd;
	}

	public void setIvpwd(String ivpwd) {
		this.ivpwd = ivpwd;
	}

	@Override
	public String toString() {
		return "Ivuser [openid=" + openid + ", ivpwd=" + ivpwd + ", name=" + name + "]";
	}

	
	
	
}
