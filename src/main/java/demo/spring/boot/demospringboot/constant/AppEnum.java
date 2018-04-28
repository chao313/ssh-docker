package demo.spring.boot.demospringboot.constant;

import org.apache.commons.lang.StringUtils;

/**
 * 2018/4/29    Created by   chao
 */
public enum AppEnum {
    REDIS("redis"),
    MYSQL("mysql"),
    PHP("php"),
    JAVA("java"),
    NODE("node"),
    PYTHON("python"),
    NULL("null"),
    TOMCAT("tomcat"),;
    private String app;

    AppEnum(String app) {
        this.app = app;
    }

    public static String getApp(String apps) {
        String result = null;
        for (AppEnum appEnum : AppEnum.values()) {
            if (StringUtils.isNotBlank(apps) && apps.contains(appEnum.app)) {
                result = appEnum.app;
                break;
            }
        }
        return result;
    }
}
