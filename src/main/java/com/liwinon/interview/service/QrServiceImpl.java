package com.liwinon.interview.service;

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
	@Autowired
	IvmemberDao memberDao; 
	private static final String URL = "https://localhost/interview/qrCode/addInterview";
//	private static final String URL = ""https://mesqrcode.liwinon.com/interview/qrCode/addInterview";
	
	/** 业务方法:
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
		createQueue(ivid);
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
				createQueue(ivid);
			}
			Cache cache = CacheManager.getCache(ivid);  
			System.out.println("查询的缓存:"+cache);
			Queue me = (Queue)cache.getValue();   // 获取存在的队列值.
			/**判断队列中是否已经存在该用户.根据业务情况考虑是否需要更换方式*/
			if(me.getNum()>0) {
				Object[] arrs = me.getQueueList();
				
				for(Object mem : arrs) {
					Ivmember m = (Ivmember)mem;
					System.out.println("转换的对象"+m);
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
	 */
	public void createQueue(String ivid) {
		Cache cache = new Cache();
		cache.setForever(true);
		cache.setKey(ivid);
		Queue queue = new Queue();  //创建一个空队列
		/**查询数据库此次面试是否已经有人参加,防止服务器重启后丢失缓存造成重复添加**/
		List<Ivmember> list = memberDao.findByIvId(Integer.valueOf(ivid));
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
		String openid = sessionDao.findBySessionKey(session).getOpenId();
		Cache cache = CacheManager.getCache(ivid);  
		Queue me = (Queue)cache.getValue();   // 获取存在的队列值.
		int id = Integer.valueOf(ivid);
		Ivmember iv = memberDao.findByividAndOpenid(id,openid);
		return String.valueOf(me.getYourFront(iv));
	}
}
