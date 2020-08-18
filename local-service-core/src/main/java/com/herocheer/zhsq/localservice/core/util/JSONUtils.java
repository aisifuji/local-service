package com.herocheer.zhsq.localservice.core.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * json工具类(jackjson)
 * @author yuxh<fycuixi@foxmail.com>
 */
public class JSONUtils {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();



    /**
     * json转map
     * @param jsonStr
     * @return
     * @throws Exception
     */
	public static <T> Map<String, Object> jsonToMap(String jsonStr)
            throws Exception {
        return OBJECT_MAPPER.readValue(jsonStr, Map.class);
    }


	
}
