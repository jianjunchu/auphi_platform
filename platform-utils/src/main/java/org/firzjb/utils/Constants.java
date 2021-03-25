package org.firzjb.utils;


import org.apache.log4j.Logger;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {

	private static Logger logger = Logger.getLogger(Constants.class);
	public static String PARAM_ACTION = "action" ;
	public static String WEB_IP = "WebIP";



	public static Properties p = new Properties();
	static {
		try {
			p.load(Constants.class.getResourceAsStream("/constants_auphi.properties"));
			String webIP = p.getProperty(WEB_IP);
			if(webIP==null || "".equals(webIP)){
//				webIP = Constants.getWebIp();
//				p.setProperty(WEB_IP, webIP);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}

	public static String get(String key) {
		return p.getProperty(key, "");
	}

	public static String get(String key, String defaultValue) {
		return p.getProperty(key, defaultValue);
	}

	public static void set(String key, String value){
		try{
			Writer w=new FileWriter(Constants.class.getResource("").getFile());
			p.setProperty(key, value);
			p.store(w, key);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}

	}

	public static String getWebIp() {
		String ip = "";
		String chinaz = "http://ip.chinaz.com";

		StringBuilder inputLine = new StringBuilder();
		String read = "";
		URL url = null;
		HttpURLConnection urlConnection = null;
		BufferedReader in = null;
		try {
			url = new URL(chinaz);
			urlConnection = (HttpURLConnection) url.openConnection();
		    in = new BufferedReader( new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
			while((read=in.readLine())!=null){
				inputLine.append(read+"\r\n");
			}
			//System.out.println(inputLine.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}


		Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
		Matcher m = p.matcher(inputLine.toString());
		if(m.find()){
			String ipstr = m.group(1);
			ip = ipstr;
			//System.out.println(ipstr);
		}
		return ip;
	}
}
