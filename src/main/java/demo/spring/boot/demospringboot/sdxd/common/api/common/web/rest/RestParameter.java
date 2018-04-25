package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import org.springframework.web.context.request.NativeWebRequest;

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
 * 17/1/23     melvin                 Created
 */
public abstract class RestParameter<T> {

    private boolean required;
    private String name;

    public RestParameter(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public T getValue(NativeWebRequest request) {
        RestValue<T> value = takeValue(request);
        return value == null ? null : value.getValue();
    }

    public RestValue<T> getValuePairs(NativeWebRequest request) {
        return takeValue(request);
    }

    public String getStatement(NativeWebRequest request) {
        RestValue<T> restValue = getValuePairs(request);
        if (restValue != null) {
            return restValue.getStatement();
        }
        return getName().concat("=").concat("");
//        T value = getValue(request);
//        String text = value == null ? "" : String.valueOf(value);
//        if (value != null && value.getClass().isArray()) {
//            text = JsonUtil.toJson(value);
//        }
//        return getName().concat("=").concat(text);
    }

    @Override
    public String toString() {
        return getName();
    }

    public abstract boolean existValue(NativeWebRequest request);

    protected abstract RestValue<T> takeValue(NativeWebRequest request);
}
