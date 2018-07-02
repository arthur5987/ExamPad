package com.xjhsk.exampad.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
 
 public class Md5CaculateUtil {
     
     public static String getHash(String fileName) throws IOException, NoSuchAlgorithmException{

         File f = new File(fileName);
         InputStream ins = new FileInputStream(f);
         
         byte[] buffer = new byte[8192];
         MessageDigest md5 = MessageDigest.getInstance("MD5");
         
         int len;
         while((len = ins.read(buffer)) != -1){
             md5.update(buffer, 0, len);
         }
 
         ins.close();
 //        也可以用apache自带的计算MD5方法
         return new String(Hex.encodeHex(DigestUtils.md5(md5.digest())));
     }

 }