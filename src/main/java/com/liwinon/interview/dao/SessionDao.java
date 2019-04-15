package com.liwinon.interview.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.liwinon.interview.entity.Session;
//第二个泛型是主键类型
public interface SessionDao extends JpaRepository<Session, String>{
	@Query(value="SELECT s FROM Session s where s.sessionKey = :sessionKey")
	Session findBySessionKey(String sessionKey);
}
