package com.herocheer.zhsq.localservice.core.util;

public class LSStringUtil {
    /**
     2 * 去除首尾指定字符
     3 * @param str 字符串
     4 * @param element 指定字符
     5 * @return
     6 */
 public static String trimFirstAndLastChar(String str, String element){
           boolean beginIndexFlag = true;
           boolean endIndexFlag = true;
            do{
                  int beginIndex = str.indexOf(element) == 0 ? 1 : 0;
                    int endIndex = str.lastIndexOf(element) + 1 == str.length() ? str.lastIndexOf(element) : str.length();str = str.substring(beginIndex, endIndex);
                 beginIndexFlag = (str.indexOf(element) == 0);
                    endIndexFlag = (str.lastIndexOf(element) + 1 == str.length());
                } while (beginIndexFlag || endIndexFlag);
            return str;
         }
}
