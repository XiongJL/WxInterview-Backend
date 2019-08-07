package com.liwinon.interview.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;
import com.liwinon.interview.dao.InterviewDao;
import com.liwinon.interview.dao.IvmemberDao;
import com.liwinon.interview.dao.IvuserDao;
import com.liwinon.interview.dao.SessionDao;
import com.liwinon.interview.entity.Interview;
import com.liwinon.interview.entity.Ivmember;
import com.liwinon.interview.entity.Ivuser;
import com.liwinon.interview.utils.Cache;
import com.liwinon.interview.utils.CacheManager;
import com.liwinon.interview.utils.QrCodeCreateUtil;
import com.liwinon.interview.utils.Queue;
import com.liwinon.interview.utils.Utils;
import com.liwinon.interview.utils.WxUtils;
@Service
public class QrServiceImpl implements QrService{
	@Autowired
	InterviewDao ivDao;
	@Autowired
	SessionDao sessionDao;
	@Autowired
	IvmemberDao memberDao; 
	@Autowired
	IvuserDao userDao;
	@Autowired
	HttpUtil http;
//	private static final String URL = "https://localhost/interview/qrCode/addInterview";
	private static final String URL = "https://mesqrcode.liwinon.com/interview/qrCode/addInterview";
	
	/** 业务方法:
	 * 	发起一场面试
	 * @deprecated 废弃,通过微信接口生成二维码
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
		createQueue(ivid,true);
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
	@Transactional
	public String startInterviewByWX(String session,String startTime,String duringTime,String location) {
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
		
		System.out.println("url:"+url);
		/** 在缓存中创建一个队列来保存面试者信息*/
		createQueue(ivid,true);
		System.out.println("缓存大小："+CacheManager.getCacheSize());
		System.out.println("缓存对象Value："+CacheManager.getCacheInfo(ivid).getValue());
		/*通过微信接口生成二维码*/
		String token = WxUtils.getAccess_token().getAccess_token();
		String json = "{\"scene\": \""+iv.getIvId()+"\",\"width\":300,\"page\":\"pages/index/index\"}";
		byte[] data = http.post("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+token,
				json);
		//new一个文件对象用来保存图片
		File imageFile = new File(Path); 
		//创建输出流 
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(imageFile);
			//写入数据 
			outStream.write(data);
			ivDao.save(iv);  //保存面试
			return qrName;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//关闭输出流 
				outStream.close(); 
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		return "fail";
	}

	/**业务方法:
	 * 	
	 * 	 参加一场面试
	 * @return 返回当前用户加入后的队列总人数
	 */
	@Override
	public String jionInterview(String ivid, String session) {
		Interview query = ivDao.findByIvId(Integer.valueOf(ivid));
		//通过session获取参加者openid
		String openid = sessionDao.findBySessionKey(session).getOpenId();
		//用户参加时查看是面试否存在*/
		//通过数据库查询ivtype
		if(query!=null && query.getIvType()!=2) {  //如果存在此次面试,且面试未结束
			//如果没有此次面试信息!说明服务器重启了,重新创建缓存队列
			if(CacheManager.getCache(ivid)==null) {  
				createQueue(ivid,true);
			}
			Cache cache = CacheManager.getCache(ivid);  
			System.out.println("查询的缓存:"+cache);
			Queue me = (Queue)cache.getValue();   // 获取存在的队列值.
			/**判断队列中是否已经存在该用户.根据业务情况考虑是否需要更换方式*/
			if(me.getNum()>0) {
				Object[] arrs = me.getQueueList();
				System.out.println("join queue's value: "+ arrs.toString());
				for(Object mem : arrs) {
					Ivmember m = (Ivmember)mem;
					System.out.println("转换的对象"+m);
					if(m==null||m.getOpenid()=="")
						break;
					if(m.getOpenid().equals(openid)) {
						return "exist"; //已参加过此次面试
					}
				}
			}
			//关联参加表
			Ivmember member = new Ivmember();
			member.setIvId(Integer.valueOf(ivid));
			member.setOpenid(openid);
			member.setPublishDate(Utils.nowDate());
			member.setState(1);
			//设置当前面试的队列
			/**发起面试时就会创建一个缓存来保存queue*/ 
			me.enqueue(member);  // 添加该用户到队列
			cache.setValue(me);   //重新更新缓存
			System.out.println("保存的缓存"+me);
			CacheManager.putCache(ivid, cache);  //保存缓存
			memberDao.save(member);      //保存到数据库关联表
			int numbers = me.getNum();
			return String.valueOf(numbers);
			//更新缓存
		}
		
		return "over";
	}
	/**工具方法 :   
	 * 	在缓存中创建一个队列来保存面试者信息
	 * 	
	 * @param ivid 面试场次id
	 * @param isAll true 查询所有人, false 只查询尚未面试,或面试进行中的人
	 */
	public void createQueue(String ivid,boolean isAll) {
		Cache cache = new Cache();
		cache.setForever(true);
		cache.setKey(ivid);
		Queue queue = new Queue();  //创建一个空队列
		/**查询数据库此次面试是否已经有人参加,防止服务器重启后丢失缓存造成重复添加**/
		
		List<Ivmember> list = null;
		if(isAll) {
			list = memberDao.findAllByIvId(Integer.valueOf(ivid));
		}else {
			list = memberDao.findIngByIvId(Integer.valueOf(ivid));
		}
		if(list.size()>0) {
			for(Ivmember member : list) {   //如果存在参加的面试则入队, 顺序根据扫描时间.
				System.out.println("PublishDate: "+member.getPublishDate());
				queue.enqueue(member);
			}
		}
		cache.setValue(queue);
		CacheManager.putCache(ivid, cache); //保存缓存
	}
	
	/**判断是否参加了一场面试*/
	@Override
	public List<Interview> didJion(String session) {
		System.out.println("DidJion GET SESSION is:"+session);
		String openid = sessionDao.findBySessionKey(session).getOpenId();
		List<Ivmember> list = memberDao.findByOpenidWithIVING(openid);
		List<Interview> results = new ArrayList<Interview>();
		for(Ivmember mem: list) {
			results.add(ivDao.findByIvId(mem.getIvId()));
		}
		
		return results;
	}
	/**判断前面还有多少个人*/
	@Override
	public String howManyFront(String ivid,String session) {
		if(CacheManager.getCache(ivid)==null) {  
			System.out.println("队列不存在, 准备创建队列.");
			createQueue(ivid,false);
		}
		System.out.println("howManyFornt GET session is :"+session);
		String openid = sessionDao.findBySessionKey(session).getOpenId();
		Cache cache = CacheManager.getCache(ivid);  
		Queue me = (Queue)cache.getValue();   // 获取存在的队列值.
		int id = Integer.valueOf(ivid);
		Ivmember iv = memberDao.findByividAndOpenid(id,openid);
		return String.valueOf(me.getYourFront(iv));
	}
	/**
	 * 	查询面试进行和未开始的.
	 */
	@Override
	public List<Interview> getInterview() {
		System.out.println("查询可操作性的面试信息");
//		List<Interview>list = ivDao.findByIvType();
//		for(Interview i : list) {
//			System.out.println(i);
//		}
		return ivDao.findByIvType();
	}
	/**
	 * 查询面试进行和未开始的各个总人数
	 * @return
	 */
	public int[] getInterviewNums(){
		List<Interview>list = ivDao.findByIvType();
		int[] nums = new int[list.size()];
		for(int i=0 ; i< list.size();i++) {
			String ivid = String.valueOf(list.get(i).getIvId());
			if(CacheManager.getCache(ivid)==null) {  
				System.out.println("队列不存在, 准备创建队列.");
				createQueue(ivid,false);
			}
			Cache cache = CacheManager.getCache(ivid);  
			Queue me = (Queue)cache.getValue();   // 获取存在的队列值.
			nums[i] = me.getNum();
		}
		
		return nums;
	}
	/**
	 * 	返回此次面试的队头人员, 不需要面试完毕的人
	 */
	@Override
	public Ivuser getThisQueue(String ivid) {
		//如果没有此次面试信息!说明服务器重启了,重新创建缓存队列
		if(CacheManager.getCache(ivid)==null) {  
			System.out.println("队列不存在, 准备创建队列.");
			createQueue(ivid,false);
		}
		Cache cache = CacheManager.getCache(ivid);  
		System.out.println("查询的缓存:"+cache);
		Queue me = (Queue)cache.getValue();   // 获取存在的队列值.
		System.out.println("num:"+me.getNum());
		if(me.getNum()>0) {
			Object mem = me.getFirst();  //获取第一个且不出队
			Ivmember m = (Ivmember)mem;
			System.out.println("转换的对象"+m);
			//通过面试人员关联表查找面试人员信息表
			System.out.println(userDao.findByOpenid(m.getOpenid()));
			if(m.getOpenid()==null || m.getOpenid()=="")
				return null;
			return userDao.findByOpenid(m.getOpenid());
		}
		return null;
	}
	
	/***
	 *	 完成对头人员面试,并返回操作结果，出队操作
	 */
	@Override
	public String nextOne(String ivid) {
		if(CacheManager.getCache(ivid)==null) {  
			System.out.println("队列不存在, 准备创建队列.");
			createQueue(ivid,false);
		}
		Cache cache = CacheManager.getCache(ivid);  
		System.out.println("查询的缓存:"+cache);
		Queue me = (Queue)cache.getValue();   // 获取存在的队列值.
		System.out.println("num:"+me.getNum());
		if(me.getNum()>0) {
			Object mem = me.dequeue();//出队 ,  num--
			Ivmember m = (Ivmember)mem;
			System.out.println(m);
			// 改变面试者状态   3面试完成
			m.setState(3);
			memberDao.save(m);
			if(m!=null&&me.getNum()!=0)
				return "ok";
		}
		return "empty";
	}
	/**
	 * 	结束指定面试
	 */
	@Override
	public String finishInterview(String ivid) {
		Interview i = ivDao.findByIvId(Integer.valueOf(ivid));
		i.setIvType(2);  // 0(面试未开始)1(面试进行中)2(面试结束)
		//清除队列
		if(CacheManager.getCache(ivid)!=null) {
			Cache cache = CacheManager.getCache(ivid);  
			Queue me = (Queue)cache.getValue();   
			me.DestroyQueue();   //清除队列
			CacheManager.clearOnly(ivid);  //清除缓存
		}
		ivDao.save(i);
		return "ok";
	}
	/**
	 * 开启指定的面试
	 */
	@Override
	public String openInterview(String ivid) {
		Interview iv = ivDao.findByIvId(Integer.valueOf(ivid));
		iv.setIvType(1);//0(面试未开始)1(面试进行中)2(面试结束)
		ivDao.save(iv);
		return "ok";
	}
}
