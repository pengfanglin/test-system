package com.project.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * http请求
 */
@Component
public class HttpUtils {
	private static CloseableHttpClient httpClient;
	private static CloseableHttpClient wechatHttpClient;
	private static Logger logger=LoggerFactory.getLogger(HttpUtils.class);
	@Autowired
	public HttpUtils(CloseableHttpClient httpClient,CloseableHttpClient wechatHttpClient){
		HttpUtils.httpClient=httpClient;
		HttpUtils.wechatHttpClient=wechatHttpClient;
	}

	/**
	 * 无请求头get请求
	 */
	public static String get(String url, Map<String, Object> params) {
		return get(url, null, params);
	}
	/**
	 * 有参数，有请求头get请求
	 */
	public static String get(String url,Map<String, String> headers,Map<String, Object> params) {
		//构建GET请求头
		String apiUrl = getUrlWithParams(url, params);
		HttpGet httpGet = new HttpGet(apiUrl);
		//设置header信息
		if ( headers != null && headers.size() > 0 ) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
		}
		CloseableHttpResponse response = null;
		try {
			//发送get请求
			response = httpClient.execute(httpGet);
			//如果返回值为空，或者返回状态码为空，代表请求失败，返回null
			if (response == null || response.getStatusLine() == null) {
				return null;
			}
			//得到状态码
			int statusCode = response.getStatusLine().getStatusCode();
			//请求成功
			if ( statusCode == HttpStatus.SC_OK ) {
				HttpEntity entityResponse = response.getEntity();
				//将返回结果编码后返回
				if (entityResponse != null) {
					return EntityUtils.toString(entityResponse, "UTF-8");
				}
			}else{
				return null;
			}
		} catch (IOException e) {
			logger.error("get请求发送失败");
			e.printStackTrace();
		} finally {
			if ( response != null ) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * 无请求头post请求
	 */
	public static String post(String url, Map<String, Object> params) {
		return post(url, null, params);
	}
	/**
	 * 有参数，有请求头post请求
	 */
	public static String post(String url,Map<String, String> headers,Map<String, Object> params) {
		HttpPost httpPost = new HttpPost(url);
		//配置请求headers
		if ( headers != null && headers.size() > 0 ) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}
		//配置请求参数
		if ( params != null && params.size() > 0 ) {
			HttpEntity entityReq = getUrlEncodedFormEntity(params);
			httpPost.setEntity(entityReq);
		}
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			if (response == null || response.getStatusLine() == null) {
				return null;
			}
			int statusCode = response.getStatusLine().getStatusCode();
			if ( statusCode == HttpStatus.SC_OK ) {
				HttpEntity entityRes = response.getEntity();
				if ( entityRes != null ) {
					return EntityUtils.toString(entityRes, "UTF-8");
				}
			}
			return null;
		} catch (IOException e) {
			logger.error("post请求发送失败");
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * 构建post请求体
	 */
	private static HttpEntity getUrlEncodedFormEntity(Map<String, Object> params) {
		List<NameValuePair> pairList = new LinkedList<>();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if(entry.getKey()!=null&&entry.getValue()!=null){
				NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				pairList.add(pair);
			}
		}
		return new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8"));
	}

	/**
	 * 将url和请求参数编码成字符串
	 */
	private static String getUrlWithParams(String url, Map<String, Object> params) {
		boolean first = true;
		StringBuilder sb = new StringBuilder(url);
		for (Map.Entry<String,Object> entry:params.entrySet()) {
			char ch = '&';
			if (first) {
				ch = '?';
				first = false;
			}
			String value = entry.getValue().toString();
			try {
				String encodeParams = URLEncoder.encode(value, "UTF-8");
				sb.append(ch).append(entry.getKey()).append("=").append(encodeParams);
			} catch (UnsupportedEncodingException e) {
				logger.error("get请求参数编码失败");
				throw new RuntimeException("get请求参数编码失败");
			}
		}
		return sb.toString();
	}
  /**
   * post方式发送json请求
   */
  public static String getByJson(String url, String json){
    HttpPost post = new HttpPost(url);
    try {
      StringEntity s = new StringEntity(json);
      s.setContentEncoding("UTF-8");
      s.setContentType("text/json");
      post.setEntity(s);
      HttpResponse res = httpClient.execute(post);
      if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
        return EntityUtils.toString(res.getEntity());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
	/**
	 * post请求发送xml数据
	 */
	public static String postByXml(String url, Map<String,Object> params) {
		HttpPost post = new HttpPost(url);
		String xml= OthersUtils.mapToXml(params);
		try {
			StringEntity s = new StringEntity(new String(xml.getBytes(),"ISO-8859-1"));
			s.setContentEncoding("utf-8");
			s.setContentType("text/xml; charset=utf-8");//发送xml数据需要设置contentType
			post.setEntity(s);
			HttpResponse res = httpClient.execute(post);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				return EntityUtils.toString(res.getEntity(),"utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 微信退款post请求发送xml数据，携带有证书文件
	 */
	public static String wechatPostByXml(String url, Map<String,Object> params) {
		HttpPost post = new HttpPost(url);
		String xml= OthersUtils.mapToXml(params);
		try {
			StringEntity s = new StringEntity(new String(xml.getBytes(),"ISO-8859-1"));
			s.setContentEncoding("UTF-8");
			s.setContentType("text/xml; charset=UTF-8");
			post.setEntity(s);
			HttpResponse res = wechatHttpClient.execute(post);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				return EntityUtils.toString(res.getEntity(),"UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
