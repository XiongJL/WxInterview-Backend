package com.liwinon.interview.interview;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.liwinon.interview.service.HttpUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterviewApplicationTests {
	@Autowired
	HttpUtil http;
	@Test
	public void contextLoads() {
		String value = http.reqGet("http://appinter.sunwoda.com/weixin/blessing/findBirthdayUserNow.json"
				, "page=0&pageSize=2");
		System.out.println(value);
	}

}
