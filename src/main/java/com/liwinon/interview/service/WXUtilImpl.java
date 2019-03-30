package com.liwinon.interview.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liwinon.interview.dao.IvuserDao;
import com.liwinon.interview.dao.SessionDao;
import com.liwinon.interview.entity.Ivuser;
import com.liwinon.interview.entity.Session;


@Service
public class WXUtilImpl implements WXUtil {
	@Autowired
	private SessionDao sessionDao;
	@Autowired
	private IvuserDao userDao;

	/**
	 * GET 请求 获取微信传来的code，然后发送url请求获取 openid、sessionkey等信息。
	 */
	public String reqGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		InputStreamReader isr = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setConnectTimeout(3000);
			connection.setRequestProperty("accept", "*/*");
			// connection.setRequestProperty("connection", "Keep-Alive"); //Connection:close短链接 ,keep-alive 长链接
			connection.setRequestProperty("connection", "close");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应 , 注意使用 utf-8 统一前后台数据库的编码格式
			isr = new InputStreamReader(connection.getInputStream(), "utf-8");
			in = new BufferedReader(isr);
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
				if (isr != null) {
					isr.close();
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 发起POST请求
	 * 
	 * @param url   发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public String reqPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		System.out.println("发出请求前的param：" + param);
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
//			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
			conn.setRequestProperty("Accept", "application/json");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/***
	 * 利用HTTPClient 发送GET 请求
	 * 
	 */
	public String get(String url)
	      {
	          String result = null;
	          CloseableHttpClient httpClient = HttpClients.createDefault();
	         HttpGet get = new HttpGet(url);
	         CloseableHttpResponse response = null;
	         try {
	             response = httpClient.execute(get);
	             if(response != null && response.getStatusLine().getStatusCode() == 200)
	            {
	                 HttpEntity entity = response.getEntity();
	                result = entityToString(entity);
	             }
	             return result;
	        } catch (IOException e) {
	            e.printStackTrace();
	         }finally {
	             try {
	                 httpClient.close();
	                 if(response != null)
	                 {
	                     response.close();
	                 }
	             } catch (IOException e) {
	                 e.printStackTrace();
	             }
	         }
	        return null;
	     }
	private String entityToString(HttpEntity entity) throws IOException {
		          String result = null;
		          if(entity != null)
		          {
		              long lenth = entity.getContentLength();
		              if(lenth != -1 && lenth < 2048)
		              {
		                  result = EntityUtils.toString(entity,"UTF-8");
		              }else {
		                 InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
		                 CharArrayBuffer buffer = new CharArrayBuffer(2048);
		                 char[] tmp = new char[1024];
		                int l;
		                while((l = reader1.read(tmp)) != -1) {
		                     buffer.append(tmp, 0, l);
		                 }
		                 result = buffer.toString();
		             }
		         }
		         return result;
		     }
	
	/**
	 * 发送 post请求 用HTTPclient 发送请求 ，解决乱码问题
	 */
	public static String post(String json, String URL) {
		String obj = null;
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		HttpPost httppost = new HttpPost(URL);
		httppost.addHeader("Content-type", "application/json; charset=utf-8");
		httppost.setHeader("Accept", "application/json");
		try {
			StringEntity s = new StringEntity(json, Charset.forName("UTF-8")); // 对参数进行编码，防止中文乱码
			s.setContentEncoding("UTF-8");
			httppost.setEntity(s);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				// 获取相应实体
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					obj = EntityUtils.toString(entity, "UTF-8");
				}

			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

	/**
	 * @author XiongJL
	 * @return 0代表错误，其余正整数即为用户ID
	 * @param String session_key 会话秘钥
	 */
	@Override
	public String getOpenId(String session_key) {
		Session table = sessionDao.findBySessionKey(session_key);
		if (table == null)
			return "0"; // 如果没有表，返回0
		Ivuser user = userDao.findByOpenid(table.getOpenId());
		if (user == null)
			return "0"; // 没有该用户
		return user.getOpenid(); // 返回用户的 ID
	}

	

}
