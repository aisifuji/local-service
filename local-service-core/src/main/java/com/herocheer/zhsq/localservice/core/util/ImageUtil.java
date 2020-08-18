package com.herocheer.zhsq.localservice.core.util;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.mina.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.imageio.stream.FileImageOutputStream;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtil {

    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 下载图片
     * @param path 图片路径url
     * @param preDownAddress 本地路径
     * @return
     * @throws IOException
     */
    public static String downPic(String path,String preDownAddress) throws IOException {
        Assert.hasText(path, "download path is empty");
        Assert.hasText(preDownAddress, "save path is empty");
        File dir = new File(preDownAddress);
        FileUtil.judeDirExists(dir);
        String imageNm = path.substring(path.lastIndexOf("/"));
        String imageFile = preDownAddress + imageNm;
        URL url = null;
        url = new URL(path);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        //这里就简单的设置了网络的读取和连接时间上线，如果时间到了还没成功，那就不再尝试
        httpURLConnection.setReadTimeout(8000);
        httpURLConnection.setConnectTimeout(8000);
        InputStream inputStream = httpURLConnection.getInputStream();
        FileUtil.getFile(inputStream, imageFile);
        return imageFile;
    }

    /**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片
     * @return
     */
    public static String getImgStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "data:image/png;base64,"+new String(Base64.encodeBase64(data));
    }
    /**
     * 将視頻转换成Base64编码
     * @param imgFile 待处理图片
     * @return
     */
    public static String getVideoStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

    /**
     * byte数组到图片
     * @version: v1.0
     * @author:mqs
     * @param data
     * @param path
     */
    public static void byteToImage(byte[] data,String path){
        if(data.length<3||path.equals("")) return;
        FileImageOutputStream imageOutput = null;
        try{
            imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
        } catch(Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }finally {
            try {
                imageOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 图片编码
     * @method imageToBase64
     * @param file
     * @author wangjian
     * @date 2019年4月25日 下午3:59:08
     * @return String
     */
    public static String imageToBase64(File file) {
        if(file == null || file.isDirectory() || !file.exists()){
            logger.info("图片转换为base64失败，图片不存在。");
            return "";
        }

        InputStream inputStream = null;
        ByteArrayOutputStream os = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] data = new byte[inputStream.available()];
            //图片大于30KB，需进行压缩
            if(data.length > 30 * 1024){
                os = new ByteArrayOutputStream();
                Thumbnails.of(file.getCanonicalPath()).size(250, 350).toOutputStream(os);
                data = os.toByteArray();
            }else {
                inputStream.read(data);
            }
            return java.util.Base64.getEncoder().encodeToString(data);
        } catch (Exception e) {
            logger.warn("图片base64编码时发生错误：{}", e.getMessage());
            return "";
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.warn("图片base64编码时发生错误：{}", e.getMessage());
                }
            }
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    logger.warn("图片base64编码时发生错误：{}", e.getMessage());
                }
            }
        }
    }
}
