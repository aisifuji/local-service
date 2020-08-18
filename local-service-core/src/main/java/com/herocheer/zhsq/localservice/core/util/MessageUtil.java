package com.herocheer.zhsq.localservice.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;

public class MessageUtil {


    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);

    /** Properties */
    private static volatile Properties prop = null;

    /** 国际化文件 */
    private static final String[] messagesFiles = {"classpath*:/message/messages-*.properties" };

    /**
     * 初始化
     * @throws IOException
     *
     * <pre>
     * <b>处理逻辑：</b>
     * </pre>
     */
    public static void init() throws IOException {
        if (prop == null)
            synchronized (MessageUtil.class) {
                if (prop == null) {
                    prop = new Properties();
                    ResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
                    for (String errorFile : messagesFiles)
                        try {
                            Resource[] resources = loader.getResources(errorFile);
                            for (Resource resource : resources)
                                if (resource.exists())
                                    PropertiesLoaderUtils.fillProperties(prop, resource);
                        } catch (IOException e1) {
                            logger.error(e1.getMessage(), e1);
                            throw e1;
                        }
                }
            }
    }

    /**
     * @param code
     * @param values
     * @return
     *
     * <pre>
     * <b>处理逻辑：</b>
     * </pre>
     */
    public static String getCodeMessage(String code, String... values) {
        if (prop == null) {
            try {
                init();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        String valueForPlace = prop.getProperty(code);
        if (StringUtils.hasLength(valueForPlace) && StringUtils.hasLength(code) && values != null) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] != null) {
                    valueForPlace = valueForPlace.replaceAll("\\{" + i + "\\}", values[i]);
                }
            }
        }
        return valueForPlace;
    }

}
