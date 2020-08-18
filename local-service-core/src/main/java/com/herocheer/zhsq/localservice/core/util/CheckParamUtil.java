package com.herocheer.zhsq.localservice.core.util;

import java.lang.reflect.Field;
import java.util.List;

public class CheckParamUtil {

    /**
     * <p>Title: validateFild</p>
     * <p>Description: 这是一个以反射机制为基础的判断对象内部的属性是否为空的方法</p>
     * @param obj      要判断的对象实例
     * @param exclField 放行的属性, 需要做判断的属性
     * @return 布尔类型, 这个可以根据需求做出变更
     */
    public static boolean validateFild(Object obj, List<String> exclField) {

        boolean target = false;

        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                String name = f.getName();
                // 判断属性名称是否在排除属性值中
                if (exclField.contains(name)) {
                    if (f.get(obj) == null || f.get(obj).equals("")) {
                        // 判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                        target = true;
                        System.out.println(name);
                    }
                }
            } catch (IllegalArgumentException e) {
                target = false;
                System.out.println("对象属性解析异常" + e.getMessage());
                return target;
            } catch (IllegalAccessException e) {
                target = false;
                System.out.println("对象属性解析异常" + e.getMessage());
                return target;
            }
        }
        return target;
    }

}
