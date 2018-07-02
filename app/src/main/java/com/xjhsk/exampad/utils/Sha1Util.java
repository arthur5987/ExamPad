package com.xjhsk.exampad.utils;

import com.xjhsk.exampad.api.Constants;

import java.security.MessageDigest;

public class Sha1Util {
	
	
//	/**
//	 * 验证是否合法
//	 * @param str 收到的参数值合集
//	 * @param sha token值
//	 * @return
//	 */
//	public static boolean checkSha1(String str,String sha){
//		if(getSha1(str+ Constants.KEY).equals(sha)){
//			return true;
//		}else{
//			return false;
//		}
//
//	}
	/**
	 * 加密
	 * @param str 需要加密的文字
	 * @return
	 */
	public static String getSha1(String str){
	    if (null == str || 0 == str.length()){
	        return null;
	    }
	    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	            'a', 'b', 'c', 'd', 'e', 'f'};
	    try {
	        MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
	        mdTemp.update(str.getBytes("UTF-8"));
	         
	        byte[] md = mdTemp.digest();
	        int j = md.length;
	        char[] buf = new char[j * 2];
	        int k = 0;
	        for (int i = 0; i < j; i++) {
	            byte byte0 = md[i];
	            buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
	            buf[k++] = hexDigits[byte0 & 0xf];
	        }
	        return new String(buf);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "";
	    } 
	}

}
