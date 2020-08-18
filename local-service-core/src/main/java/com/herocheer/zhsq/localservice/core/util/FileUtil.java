package com.herocheer.zhsq.localservice.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);


    public static void getFile(InputStream is, String fileName) throws IOException {
        BufferedInputStream in=null;
        BufferedOutputStream out=null;
        in=new BufferedInputStream(is);
        out=new BufferedOutputStream(new FileOutputStream(fileName));
        int len=-1;
        byte[] b=new byte[1024];
        while((len=in.read(b))!=-1){
            out.write(b,0,len);
        }
        in.close();
        out.close();
    }

    /**
     * 判断文件夹是否存在，不存在创建之
     * @version: v1.0
     * @author:mqs
     * @param file
     */
    public static void judeDirExists(File file) {

        if (file.exists()) {
            if (file.isDirectory()) {
                //System.out.println("dir exists");
            } else {
                System.out.println("the same name file exists, can not create dir");
            }
        } else {
            System.out.println("dir not exists, create it ...");
            file.mkdirs();
        }

    }

    public static String makeDynmicsDateDir(String prePath){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date capTime = new Date();
        String date = df.format(capTime);
        String path = prePath + date +"/";
        File faceDir = new File(path);
        FileUtil.judeDirExists(faceDir);
        return path;
    }

    /**
     * 获取某个路径下所有文件夹
     * @param path
     * @return
     */
    public static List<String> getFiles(String path) {
            List<String> files = new ArrayList<String>();
            File file = new File(path);
            File[] tempList = file.listFiles();

             for (int i = 0; i < tempList.length; i++) {
//                     if (tempList[i].isFile()) {
//                         files.add(tempList[i].toString());
//                         //文件名，不包含路径
//                         //String fileName = tempList[i].getName();
//                     }
                 if (tempList[i].isDirectory()) {
                         //这里就不递归了，
                     files.add(tempList[i].getName());
                     }
             }
             return files;
         }

}
