package com.xjhsk.exampad.utils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * xhk加密工具
 * @author fly
 * @version 1.0 
 */
public class XhkCrypto {
	static String[] x={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	static String[] y={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	//重要变量！！！
	static String[][] s_box={{"A4","4C","DA","A2","DF","33","F5","8F","9A","40","CA","06","2A","22","78","90"},
							{"56","46","4F","E8","B6","8C","92","09","5F","89","9D","EF","5A","FB","F4","50"},
							{"4D","0A","6F","66","70","E0","60","2E","88","93","9B","BF","14","EC","4B","AC"},
							{"1F","8A","A9","4A","6C","7F","31","3B","DD","1C","B4","C7","D2","C0","91","D8"},
							{"41","6A","62","48","0F","CE","18","94","82","F2","8D","A3","3F","79","36","59"},
							{"67","D1","1A","30","0D","FE","FF","81","AA","AF","71","02","A1","1E","BC","10"},
							{"CB","25","E9","83","C4","20","B5","26","EA","C1","53","49","29","54","08","72"},
							{"19","63","52","9E","F0","DE","EB","77","85","B1","3C","5D","A5","55","2D","E3"},
							{"F6","4E","F7","76","CC","E7","B0","8E","86","A0","12","6B","99","35","E6","04"},
							{"D4","FA","84","B3","D0","05","7E","A6","27","CF","E1","68","58","3D","8B","87"},
							{"D9","BB","C3","45","38","B2","ED","C6","34","DB","D6","16","75","2F","F1","69"},
							{"B8","2B","BD","98","5C","28","65","AD","B9","CD","E5","5B","6D","2C","13","03"},
							{"0B","C9","1D","3E","5E","39","D3","D7","61","D5","80","97","C8","01","43","6E"},
							{"15","F9","BA","F8","F3","17","11","32","E4","37","A8","21","57","FD","07","44"},
							{"00","42","7A","E2","95","74","AE","0C","C2","64","EE","3A","51","23","73","0E"},
							{"96","BE","47","B7","9C","AB","A7","24","C5","DC","7C","9F","7D","7B","FC","1B"}};
	//重要变量！！！
	static String[][] x_box={{"E0","CD","5B","BF","8F","95","0B","DE","6E","17","21","C0","E7","54","EF","44"}, 
							{"5F","D6","8A","BE","2C","D0","AB","D5","46","70","52","FF","39","C2","5D","30"}, 
							{"65","DB","0D","ED","F7","61","67","98","B5","6C","0C","B1","BD","7E","27","AD"}, 
							{"53","36","D7","05","A8","8D","4E","D9","A4","C5","EB","37","7A","9D","C3","4C"}, 
							{"09","40","E1","CE","DF","A3","11","F2","43","6B","33","2E","01","20","81","12"}, 
							{"1F","EC","72","6A","6D","7D","10","DC","9C","4F","1C","BB","B4","7B","C4","18"}, 
							{"26","C8","42","71","E9","B6","23","50","9B","AF","41","8B","34","BC","CF","22"}, 
							{"24","5A","6F","EE","E5","AC","83","77","0E","4D","E2","FD","FA","FC","96","35"}, 
							{"CA","57","48","63","92","78","88","9F","28","19","31","9E","15","4A","87","07"}, 
							{"0F","3E","16","29","47","E4","F0","CB","B3","8C","08","2A","F4","1A","73","FB"}, 
							{"89","5C","03","4B","00","7C","97","F6","DA","32","58","F5","2F","B7","E6","59"}, 
							{"86","79","A5","93","3A","66","14","F3","B0","B8","D2","A1","5E","B2","F1","2B"}, 
							{"3D","69","E8","A2","64","F8","A7","3B","CC","C1","0A","60","84","B9","45","99"}, 
							{"94","51","3C","C6","90","C9","AA","C7","3F","A0","02","A9","F9","38","75","04"}, 
							{"25","9A","E3","7F","D8","BA","8E","85","13","62","68","76","2D","A6","EA","1B"}, 
							{"74","AE","49","D4","1E","06","80","82","D3","D1","91","1D","FE","DD","55","56"}};
	
	
	   
	   /**
	    * 加密方法
	    * @param srcFile  目标文件
	    * @param destfile  结果文件
	    */
	   public static void encrypt(String srcFile,String destfile){
			try{
			 	   InputStream is=new FileInputStream(srcFile);
			 	   FileOutputStream fos=new FileOutputStream(destfile);
			 	   byte[] b=new byte[1024];
	 	 
			 	   int j=0;
		
			 	   while((j=is.read(b))!=-1){

			 		 fos.write(update(b,j));
   
			 	   }
			 	  
			 	   is.close();
			 	   fos.close();
			 	
			    } catch(Exception e){
			 	  
			    } 

		}
	   /**
	    * 解密方法
	    * @param srcFile  目标文件
	    * @param destfile 结果文件
	    */
	   public static void decrypt(String srcFile,String destfile){
			try{
			 	   InputStream is=new FileInputStream(srcFile);
			 	   FileOutputStream fos=new FileOutputStream(destfile);
			 	   byte[] b=new byte[1024];
	 	 
			 	   int j=0;
		
			 	   while((j=is.read(b))!=-1){

			 		 fos.write(unupdate(b,j));
  
			 	   }
			 	  
			 	   is.close();
			 	   fos.close();
			 	
			    } catch(Exception e){
			 	  
			    } 
			
		}
	   /**
	    * 盒子替换与换位
	    * @param b
	    * @param j
	    * @return
	    */
	   public static byte[] update(byte[] b,int j){

	 		  byte[] b1=new byte[j/2];
		 	  byte[] b2=new byte[j/2];
		 	  int x; 
		 	  int y;
	 		  for(int i=0;i<b.length;i++){
	 			  if(i<j/2){	 				  
	 				 x = (b[i] & 0x0f0) >> 4;    		          
		             y = b[i] & 0x0f;  
		             b1[i]=hexStringToBytes(s_box[x][y]);
		            		 		      
	 			  }else{
	 				 x = (b[i] & 0x0f0) >> 4;  		          
		             y = b[i] & 0x0f;  
		             b2[i-j/2]=hexStringToBytes(s_box[x][y]);
	 			  }
	 		  }
	 		
	 		   
			 return addBytes(b2,b1);
	   }
	   
	   /**
	    * 盒子替换与换位 反解
	    * @param b
	    * @param j
	    * @return
	    */
	   public static byte[] unupdate(byte[] b,int j){
		   
			
	 		  byte[] b1=new byte[j/2];
		 	  byte[] b2=new byte[j/2];
		 	  int x; 
		 	  int y;
	 		  for(int i=0;i<b.length;i++){
	 			  if(i<j/2){	 				  
	 				 x = (b[i] & 0x0f0) >> 4;    		          
		             y = b[i] & 0x0f;  
		             b1[i]=hexStringToBytes(x_box[x][y]);
		            		 		      
	 			  }else{
	 				 x = (b[i] & 0x0f0) >> 4;  		          
		             y = b[i] & 0x0f;  
		             b2[i-j/2]=hexStringToBytes(x_box[x][y]);
	 			  }
	 		  }
	 		
	 		   
			 return addBytes(b2,b1);
}
	   
	   public static byte[] addBytes(byte[] data1, byte[] data2) {  
		    byte[] data3 = new byte[data1.length + data2.length];  
		    System.arraycopy(data1, 0, data3, 0, data1.length);  
		    System.arraycopy(data2, 0, data3, data1.length, data2.length);  
		    return data3;  
		  
	   }  
	   public static byte hexStringToBytes(String hexString) {   
		    if (hexString == null || hexString.equals("")) {   
		        return (Byte) null;   
		    }
		    int length = hexString.length() / 2;   
		    char[] hexChars = hexString.toCharArray();   
		    byte[] d = new byte[length];   
		    for (int i = 0; i < length; i++) {   
		        int pos = i * 2;   
		        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
		    }   
		    return d[0];   
		}   
	    private static byte charToByte(char c) {   
			
		    return (byte) "0123456789ABCDEF".indexOf(c);   
		}

//	   	public static void main(String[] args) throws Exception {
//
//
//
//		   //encrypt("D:\\testjiami\\xhk_1_001.zip","D:\\testjiami\\xhk_1_011.zip");//加密
////		   decrypt("D:\\testjiami\\xhk_1_011.zip","D:\\testjiami\\xhk_1_021.zip");  //解密
//
//
//
//	   }

}
