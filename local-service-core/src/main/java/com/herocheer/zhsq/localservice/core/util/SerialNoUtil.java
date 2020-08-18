/*
 * All rights reserved.  http://www.tansun.com.cn
 *
 * This software is the confidential and proprietary information of Tansun Tech Corporation ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Tansun Tech.
 */
package com.herocheer.zhsq.localservice.core.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * <pre>
 *  序列号工具类，用于生成各类序列号
 * </pre>
 * 
 * @author 林凯<br>
 * <b>mail</b> linkai@tansun.com.cn<br>
 * <b>date</b> 2018年9月14日 上午10:27:36<br>
 * @version 1.0.1
 */
public class SerialNoUtil {

	/**
	 * 获取序列号 以当前时间yyMMdd开头的
	 * @return
	 *
	 * <pre>
	 * <b>处理逻辑：</b>
	 * 1、当期时间 yyMMdd 作为前缀
	 * 2、加随机 hashCode 码 保证唯一性
	 * </pre>
	 */
	public static String getSerialNo() {
		String dateStr = dateToString(new Date(), "yyMMddHHmmss");
		int hashCodeV = UUID.randomUUID().toString().hashCode();
		hashCodeV = hashCodeV < 0 ? -hashCodeV : hashCodeV;// 有可能是负数
		return dateStr + String.format("%010d", hashCodeV);
	}

	/**
	 * 获取序列号 以当前时间yyMMdd开头的
	 * @return
	 *
	 * <pre>
	 * <b>处理逻辑：</b>
	 * 1、当期时间 yyMMdd 作为前缀
	 * 2、加随机 hashCode 码 保证唯一性
	 * </pre>
	 */
	public static Integer getSerialIntNo() {
		int hashCodeV = UUID.randomUUID().toString().hashCode();
		hashCodeV = hashCodeV < 0 ? -hashCodeV : hashCodeV;// 有可能是负数
		return hashCodeV;
	}

	/**
	 * 获取UUID字符串
	 * @return 字符串
	 * 
	 * <pre>
	 * <b>处理逻辑：</b>
	 * </pre>
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	/**
	 * 获取以时间截生成的id，可用于hbase的key 排序
	 * @return id
	 * 
	 * <pre>
	 * <b>处理逻辑：</b>
	 *  1、System.currentTimeMillis()
	 *  2、加4位随机数来保证唯一
	 * </pre>
	 */
	public static String getTimestampID() {
		Random ran = new Random(System.currentTimeMillis());
		return System.currentTimeMillis() + (ran.nextInt() + "").replace("-", "").substring(0, 4);// 加4位随机数来保证唯一
	}

	/**
	 * 获取UUID字符串
	 * @return 字符串
	 *
	 * <pre>
	 * <b>处理逻辑：</b>
	 * </pre>
	 */
	public static String get19UUID() {
		UUID uuid = UUID.randomUUID();
		StringBuilder sb = new StringBuilder();
		sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
		sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
		sb.append(digits(uuid.getMostSignificantBits(), 4));
		sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
		sb.append(digits(uuid.getLeastSignificantBits(), 12));
		return sb.toString();
	}
	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Numbers.toString(hi | (val & (hi - 1)), Numbers.MAX_RADIX)
				.substring(1);
	}

	/**
	 * date 转 string
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToString(Date date, String format){
		String str = null;
		DateFormat df = new SimpleDateFormat(format);
		if(date != null ){
			str = df.format(date);
		}
		return str;
	}
}
