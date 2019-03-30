package com.liwinon.interview.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.liwinon.interview.entity.Ivuser;

public interface IvuserDao extends JpaRepository<Ivuser, String>{
	Ivuser findByOpenid(String openid);
}
