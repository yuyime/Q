package com.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpHelper {
	/*
	 * urlAPI:
	 * http://192.168.173.1:8080/QRAccess/QRAccess.action?Username=yuyi&Userpass=123&uuid=
	 * return "http://192.168.173.1:8080/QRAccess/QRAccess.action?yourname=icanin&yourpass=123&yourid=";
	 * */
	private String  baseurl;
	private String  username;
	private String  userpass;
	private String  uuid;
	private String  realURL;
	private String  result;
	
	
	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public String getRealURL() {
		return realURL;
	}


	public void setRealURL(String realURL) {
		this.realURL = realURL;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getUserpass() {
		return userpass;
	}


	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getBaseurl() {
		return baseurl;
	}


	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}


	public boolean switchParams(String initURL,String iaccount,String ipass){
		//策略一：拆分拼凑法
		try {
			String temp[]=initURL.split("&");
			this.baseurl=temp[0].split("[?]")[0];
			this.uuid=temp[temp.length-1].split("=")[1];
			this.realURL=this.baseurl
					+"?Username="+iaccount
					+"&Userpass="+ipass
					+"&uuid="+this.uuid;
			//System.out.println("策略一:"+this.realURL);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("策略一失效，尝试策略二");
			//策略二：替换法
			try {
				if(-1==initURL.indexOf("yourname=icanin&yourpass=123&yourid=")){
					return false;
				}
				
				String tempString="Username="+iaccount+"&Userpass="+ipass+"&uuid=";
				this.realURL=initURL.replaceAll("yourname=icanin&yourpass=123&yourid=",tempString);
				//System.out.println("策略二:"+this.realURL);
				return true;
			} catch (Exception ee) {
				System.out.println("策略二也失效，参数已经被篡改！！！");
				ee.printStackTrace();
				return false;
			}
		}
	}
	
	
	public boolean requestQRAccessServer(){
		System.out.println("URL="+this.realURL);
		HttpGet httpGet=new HttpGet(this.realURL);
		//HttpGet httpGet=new HttpGet("http://www.baidu.com");
		HttpClient httpClient =new DefaultHttpClient();
		HttpResponse response=null;
		HttpEntity httpEntity=null;
		InputStream inputStream=null;
		try {
			response=httpClient.execute(httpGet);
			httpEntity=response.getEntity();
			inputStream=httpEntity.getContent();
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
			this.result="";
			String readLine="";
			while((readLine=reader.readLine())!=null){
				this.result+=readLine;
			}
			System.out.println("返回结果："+this.result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				inputStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return false;
	}
}
