package com.liwinon.interview.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.liwinon.interview.entity.Interview;

public interface InterviewDao extends JpaRepository<Interview, Integer> {
	Interview findByPublishTime(Date date);
	Interview findByCodeImg(String codeImg);
	Interview findByIvId(int ivid);
}
