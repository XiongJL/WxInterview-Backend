package com.liwinon.interview.service;

import org.springframework.stereotype.Service;

import com.liwinon.interview.utils.WxUtils;

@Service
public class WxServiceImpl implements WxService {

	
	@Override
	public String getAccess_token() {
		WxUtils.getAccess_token();
		return null;
	}

}
