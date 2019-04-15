package com.liwinon.interview.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.liwinon.interview.entity.Interview;

public interface InterviewDao extends JpaRepository<Interview, Integer> {
	Interview findByPublishTime(Date date);
	Interview findByCodeImg(String codeImg);
	Interview findByIvId(int ivid);
	//查询状态0,1 的面试信息
	@Query(value="SELECT i FROM Interview i WHERE i.ivType<>2 ORDER BY i.startTime")
	List<Interview> findByIvType();
	//根据状态查询面试者
}
