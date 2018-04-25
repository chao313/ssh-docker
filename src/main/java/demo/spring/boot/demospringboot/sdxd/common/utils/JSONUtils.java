package demo.spring.boot.demospringboot.sdxd.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by qiuyangjun on 2016/12/2.
 */
public class JSONUtils {
    protected static final Logger logger = LoggerFactory.getLogger(JSONUtils.class);
    private static final String regex = "\\\\\"";

    /**
     * 根据制定key获取value
     *
     * @param key 制定key，多层级用逗号区分
     */
    public static String getValueByKey(String jsonStr, String key) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Object obj = JSON.parse(jsonStr);
        List<String> valueKeys = Arrays.asList(StringUtils.split(key, "."));
        Iterator<String> keysIterator = valueKeys.iterator();
        String resultStr = getValueByKey(jsonStr, keysIterator);

        StringBuffer strBuffer = new StringBuffer(StringUtils.isBlank(resultStr) ? "" : resultStr.replaceAll(regex, "\""));

        if (strBuffer.indexOf("\"") == 0) {
            strBuffer.replace(0, 1, "");
        }
        if ((strBuffer.lastIndexOf("\"") == (strBuffer.length() - 1)) && (strBuffer.length() - 1) >= 0) {
            strBuffer.replace(strBuffer.length() - 1, strBuffer.length(), "");
        }
        return strBuffer.toString();
    }

    public static String getValueByKey(String jsonStr, Iterator<String> keysIterator) {
        if (StringUtils.isBlank(jsonStr)) {
            return "";
        }
        if (keysIterator == null || !keysIterator.hasNext()) {
            return "";
        }
        Object obj = JSON.parse(jsonStr);
//        logger.debug("======>obj:{}, class:{}", obj, obj.getClass());
        if (obj instanceof JSONObject) {
            return parseByObject((JSONObject) obj, keysIterator);
        } else if (obj instanceof JSONArray) {
            return parseByArray((JSONArray) obj, keysIterator);
        } else if (obj instanceof String) {
            try {
                JSONObject jsonObject = JSONObject.parseObject((String) obj);
                return parseByObject(jsonObject, keysIterator);
            } catch (Exception e) {
                JSONArray jsonArray = JSONArray.parseArray((String) obj);
                return parseByArray(jsonArray, keysIterator);
            }
        }

        return "";
    }

    public static String parseByObject(JSONObject jsonObj, Iterator<String> keysIterator) {
//        logger.debug("====>parseByObject JSONObject:{}", jsonObj);
        if (keysIterator.hasNext()) {
            String key = keysIterator.next();
//            logger.debug("======>parseByObject key:{}", key);
            String value = "";
            if (jsonObj.containsKey(key)) {
                Object valueObj = jsonObj.get(key);
                if (valueObj != null) {
                    value = JSON.toJSONString(valueObj);
//                    logger.debug("======>parseByObject key:{},value:{}", key,value);
                }
            }
                /*
                String[] keyStrs = StringUtils.split(key, "[");

                if (keyStrs != null && keyStrs.length > 1) {
                    key = keyStrs[0];
                }
                //如果数组长度大于1则认为获取到的值是json数组
                if (keyStrs != null && keyStrs.length > 1) {
                    String kv1 = StringUtils.replaceEach(keyStrs[1], new String[]{"[", "]"}, new String[]{"", ""});

                    JSONArray jsonArray = JSON.parseArray(value);
                    //判断第一个参数是否是数字，如果是数字则取对应的数组下标的值
                    if (StringUtils.isNotBlank(kv1)) {
                        try {
                            value = jsonArray.getJSONObject(Integer.parseInt(kv1)).toJSONString();
                        } catch (Exception e) {
                            //如果是异常则认为不是下标，目前支持唯一键值对筛选
                        }
                    }
                }*/

            if (keysIterator.hasNext()) {
                return getValueByKey(value, keysIterator);
            } else {
                return value;
            }
        } else {
            return null;
        }
    }

    public static String parseByArray(JSONArray jsonArray, Iterator<String> keysIterator) {
//        logger.debug("====> parseByArray jsonArray:{}，keyIterator:{}", jsonArray, keysIterator);
        if (keysIterator.hasNext()) {
            String key = keysIterator.next();
//            logger.debug("======>parseByArray key:{}", key);
            //todo
            String value = "";
//            if (jsonArray.containsKey(key)) {
//                Object valueObj = jsonObj.get(key);
//                if (valueObj != null) {
//                    value = JSON.toJSONString(valueObj);
//                }
//            }
            if (keysIterator.hasNext()) {
                return getValueByKey(value, keysIterator);
            } else {
                return value;
            }
        } else {
            return null;
        }
    }
}
