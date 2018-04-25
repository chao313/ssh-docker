package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JsonUtil;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 2017/9/28     melvin                 Created
 */
public class RestValue<T> {

    private String name;
    private T value;

    private boolean body;

    public RestValue(String name, T value) {
        this(name, value, false);
    }

    public RestValue(String name, T value, boolean body) {
        this.name = name;
        this.value = value;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public boolean isBody() {
        return body;
    }

    public String getStatement() {
        String text = value == null ? "" : String.valueOf(value);
        if (value != null && value.getClass().isArray()) {
            text = JsonUtil.toJson(value);
        }
        return getName().concat("=").concat(text);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", name, value);
    }
}
