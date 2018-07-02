package com.xjhsk.exampad.utils;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;



/**
 * 对文件进行压缩和加密
 * 对文件进行解压和解密
 * @author fenghao
 *
 */
public class CompressUtils {

    //PAD试卷包
    public static String KEY_ZIP_PAPER="220198e9406e44b3a5a220f971a97f6bc1706a4588b04aef9b646f8ea89d71ac01f370f66597419eab75c5f56fe787d15f59a5f4765b4f38adbb7ffc9816cc64";
    //PAD答卷包
    public static String KEY_ZIP_ANS="b3d5000952f14a5ab5e88143dbe5ebd5835108d5a85b4d1cbc0a7315d6ba450d53c7d6fd3bd1418fa91f371ff368cde951f9d82695e64c51b55220a4e010e6ab";

    /**
     * 解压加密的压缩文件
     * @param zipfile
     * @param dest
     * @param passwd
     * @throws ZipException
     */
    public static void unZip(File zipfile,String dest,String passwd) throws ZipException{
        ZipFile zfile=new ZipFile(zipfile);
//        zfile.setFileNameCharset("GBK");//在GBK系统中需要设置
        if(!zfile.isValidZipFile()){
            throw new ZipException("压缩文件不合法，可能已经损坏！");
        }
        File file=new File(dest);
        if(file.isDirectory() && !file.exists()){
            file.mkdirs();
        }
        if(zfile.isEncrypted()){
            zfile.setPassword(passwd.toCharArray());
        }
        zfile.extractAll(dest);
    }
    /**
     * 压缩文件且加密
     * @param src
     * @param dest
     * @param is
     * @param passwd
     */
    public static void zip(String src,String dest,boolean is,String passwd){
        File srcfile=new File(src);
        //创建目标文件
        String destname = buildDestFileName(srcfile, dest);
        ZipParameters par=new ZipParameters();
        par.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        par.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        if(passwd!=null){
            par.setEncryptFiles(true);
            par.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            par.setPassword(passwd.toCharArray());
        }
        try {
            ZipFile zipfile=new ZipFile(destname);
            if(srcfile.isDirectory()){
                if(!is){
                    File[] listFiles = srcfile.listFiles();
                    ArrayList<File> temp=new ArrayList<File>();
                    Collections.addAll(temp, listFiles);
                    zipfile.addFiles(temp, par);
                }
//                zipfile.addFolder(srcfile, par);
            }else{
                zipfile.addFile(srcfile, par);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }


    }
    /**
     * 目标文件名称
     * @param srcfile
     * @param dest
     * @return
     */
    public static String buildDestFileName(File srcfile,String dest){
        if(dest==null){//没有给出目标路径时
            if(srcfile.isDirectory()){
                dest=srcfile.getParent()+File.separator+srcfile.getName()+".zip";
            }else{
                String filename=srcfile.getName().substring(0,srcfile.getName().lastIndexOf("."));
                dest=srcfile.getParent()+File.separator+filename+".zip";
            }
        }else{
            createPath(dest);//路径的创建
            if(dest.endsWith(File.separator)){
                String filename="";
                if(srcfile.isDirectory()){
                    filename=srcfile.getName();
                }else{
                    filename=srcfile.getName().substring(0, srcfile.getName().lastIndexOf("."));
                }
                dest+=filename+".zip";
            }
        }
        return dest;
    }
    /**
     * 路径创建
     * @param dest
     */
    private static void createPath(String dest){
        File destDir=null;
        if(dest.endsWith(File.separator)){
            destDir=new File(dest);//给出的是路径时
        }else{
            destDir=new File(dest.substring(0,dest.lastIndexOf(File.separator)));
        }
        if(!destDir.exists()){
            destDir.mkdirs();
        }
    }

//    @org.junit.Test
//    public void Test(){
//        String src="/home/fenghao/document/书籍类资料/Maven实战 高清扫描完整版.pdf";
//        String dest="/home/fenghao/zip/maven/123.zip";
//        zip(src, dest, true, "123456");
//    }
}