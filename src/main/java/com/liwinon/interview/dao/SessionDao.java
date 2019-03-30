package com.liwinon.interview.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.liwinon.interview.entity.Session;
//第二个泛型是主键类型
public interface SessionDao extends JpaRepository<Session, String>{
	Session findBySessionKey(String sessionKey);
}
