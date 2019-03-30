package com.liwinon.interview.service;

import java.io.IOException;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;
import com.liwinon.interview.dao.InterviewDao;
import com.liwinon.interview.dao.SessionDao;
import com.liwinon.interview.entity.Interview;
import com.liwinon.interview.entity.Ivmember;
import com.liwinon.interview.utils.Cache;
import com.liwinon.interview.utils.CacheManager;
import com.liwinon.interview.utils.QrCodeCreateUtil;
import com.liwinon.interview.utils.Queue;
import com.liwinon.interview.utils.Utils;
@Service
public class QrServiceImpl implements QrService{
	@Autowired
	InterviewDao ivDao;
	@Autowired
	SessionDao sessionDao;
	private static final String URL = "https://localhost/interview/qrCode/addInterview";
//	private static final String URL = ""https://mesqrcode.liwinon.com/interview/qrCode/addInterview";
	
	/**
	 * 	发起一场面试
	 */
	@Transactional
	public String startInterview(String session,String startTime,String duringTime,String location) {
		String openid = sessionDao.findBySessionKey(session).getOpenId();
		String qrName = openid+Utils.dateToStamp(startTime)+location; //二维码文件名
		String Path = "D:\\qrcode\\"+qrName+".png";   //二维码路径在面试完成后删除
		if(ivDao.findByCodeImg(Path)!=null) {  //如果存在此二维码,则说明此次面试已存在	
				return "exist";
		}
		Date thatDay = Utils.strToDateTime(startTime);
		Date publishDate = Utils.nowDate();
		//获取openid
		
		System.out.println(thatDay);
		Interview iv = new Interview();
		iv.setOpenid(openid);
		iv.setDuringTime(Integer.valueOf(duringTime));
		iv.setPublishTime(publishDate);
		iv.setStartTime(thatDay);
		iv.setLocation(location);
		
		System.out.println("qrName:"+qrName);
		iv.setCodeInfo(qrName);  // 用着两个的base64 \ MD5码做记录 ?		
		
		System.out.println("Path:"+Path);
		iv.setCodeImg(Path);
		ivDao.save(iv);
		/*查找ivid号,再赋值codeinfo*/
		String ivid = String.valueOf(ivDao.findByPublishTime(publishDate).getIvId());
		String url = URL+"?ivid="+ivid;
		iv.setCodeInfo(url);
		ivDao.save(iv);
		System.out.println("url:"+url);
		/** 在缓存中创建一个队列来保存面试者信息*/
		Cache cache = new Cache();
		cache.setForever(true);
		cache.setKey(ivid);
		Queue queue = new Queue();  //创建一个空队列
		cache.setValue(queue);
		CacheManager.putCache(ivid, cache); //保存缓存
		System.out.println("缓存大小："+CacheManager.getCacheSize());
		System.out.println("缓存对象Value："+CacheManager.getCacheInfo(ivid).getValue());
		try {
			QrCodeCreateUtil.createQRCode(url, Path, 300, 300);
			return qrName; //返回二维码图片名字
		} catch (WriterException | IOException e) {
			e.printStackTrace();
		}
		
		return "fail";
	}

	/**
	 * 	 参加一场面试
	 */
	@Override
	public String jionInterview(String ivid, String session) {
		//通过session获取参加者openid
		String openid = sessionDao.findBySessionKey(session).getOpenId();
		//关联参加表
		Ivmember member = new Ivmember();
		member.setIvId(Integer.valueOf(ivid));
		member.setOpenid(openid);
		member.setPublishDate(Utils.nowDate());
		//设置当前面试的队列
		/**发起面试时就会创建一个缓存来保存queue,用户参加时查看是否存在,不存在说明,面试已结束*/
		if(CacheManager.hasCache(ivid)) {  //如果存在此次面试  
			Cache cache = CacheManager.getCache(ivid);
			Queue me = (Queue)cache.getValue();   // 获取存在的队列值.
		}else {
			Cache que = new Cache();
			que.setForever(true);
			que.setKey(ivid);
			Queue queue = new Queue();
		}
		
		return null;
	}
}
