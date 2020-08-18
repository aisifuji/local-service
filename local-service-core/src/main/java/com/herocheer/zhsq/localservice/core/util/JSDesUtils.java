package com.herocheer.zhsq.localservice.core.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * Des加密工具类
 *
 */
public class JSDesUtils {
	private static final Logger log = LoggerFactory.getLogger(JSDesUtils.class);


	/**
	 * 加密数据，并把加密后的明文再经base64密码
	 * @param datasource 明文
	 * @param key 
	 * @return
	 * @throws Exception
	 */
	public static String encode(String datasource, String key){
	    try{
	        SecureRandom random = new SecureRandom();
	        DESKeySpec desKey = new DESKeySpec(key.getBytes());
	        //创建一个密匙工厂，然后用它把DESKeySpec转换成
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        SecretKey securekey = keyFactory.generateSecret(desKey);
	        //Cipher对象实际完成加密操作
	        Cipher cipher = Cipher.getInstance("DES");
	        //用密匙初始化Cipher对象
	        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
	        //现在，获取数据并加密
	        return Base64Utils.encodeToString(cipher.doFinal(datasource.getBytes()));
	    }catch(Throwable e){
	        e.printStackTrace();
	        return null;
	    }
	}
	
	
	/**
	 * DES解密
	 * @return
	 */
	public static String decode(String src, String key) throws Exception {
		if(src==null||"".equals(src)){
			return "";
		}
	    // DES算法要求有一个可信任的随机数源
	    SecureRandom random = new SecureRandom();
	    // 创建一个DESKeySpec对象
	    DESKeySpec desKey = new DESKeySpec(key.getBytes());
	    // 创建一个密匙工厂
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	    // 将DESKeySpec对象转换成SecretKey对象
	    SecretKey securekey = keyFactory.generateSecret(desKey);
	    // Cipher对象实际完成解密操作
	    Cipher cipher = Cipher.getInstance("DES");
	    // 用密匙初始化Cipher对象
	    cipher.init(Cipher.DECRYPT_MODE, securekey, random);
	    // 真正开始解密操作
	    return new String(cipher.doFinal(Base64Utils.decodeFromString(src.replace("\r\n", ""))), StandardCharsets.UTF_8);
	}

	/**
	 * 图片加密算法
	 * @param args
	 */
	/**
	 * 加密算法
	 * @param datasource 明文字符串
	 * @param n    明文中每个ASCII码向前移动的位数,即密钥
	 * @return
	 */
	public static String fileEncode(String datasource,int n){
		if(datasource==null||datasource==""){
			return null;
		}
		char[] data=datasource.toCharArray();
		char[] miwen=new char[data.length];
		for(int i=0;i<data.length;i++){
			miwen[i]=(char)(data[i]-n);
		}
		return String.valueOf(miwen);
	}


	/**
	 * 图片解密算法
	 * @param args
	 */
	/**
	 * 解密算法
	 * @param datasource 密文字符串
	 * @param n    密文中每个ASCII码向后移动的位数，应与加密算法向前移动位数相同
	 * @return
	 */
	public static String fileDecode(String datasource,int n){
		if(datasource==null||datasource==""){
			return null;
		}
		char[] data=datasource.toCharArray();
		char[] str=new char[data.length];
		for(int i=0;i<data.length;i++){
			str[i]=(char)(data[i]+n);
		}
		String i=String.valueOf(str);
		return i;
	}

}
