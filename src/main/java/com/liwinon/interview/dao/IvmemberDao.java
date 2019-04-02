package com.liwinon.interview.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.liwinon.interview.entity.Ivmember;

public interface IvmemberDao extends JpaRepository<Ivmember, Integer> {
	//查找参加了这次面试的所有人
	@Query(value="SELECT i FROM Ivmember i WHERE i.ivId=:ivid ORDER BY i.publishDate ASC")
	List<Ivmember> findByIvId(int ivid);
	//查找该人参加且未结束的面试
	@Query(value="SELECT i FROM Ivmember i WHERE i.openid=:Openid AND (i.state=1 OR i.state=2)")
	List<Ivmember> findByOpenidWithIVING(String Openid);
	//查询人员对应的面试
	@Query(value="SELECT i FROM Ivmember i WHERE i.ivId=:ivid AND i.openid=:openid")
	Ivmember findByividAndOpenid(int ivid, String openid);
}
