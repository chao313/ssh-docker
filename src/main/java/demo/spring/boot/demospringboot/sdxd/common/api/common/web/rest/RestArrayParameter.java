package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

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
public class RestArrayParameter extends RestParameter<String[]> {

    public RestArrayParameter(String name) {
        super(name, false);
    }

    @Override
    public String toString() {
        return getName().concat("[]");
    }

    @Override
    public boolean existValue(NativeWebRequest request) {
        String[] value = getValue(request);
        return value != null && value.length > 0;
    }

    @Override
    protected RestValue<String[]> takeValue(NativeWebRequest request) {
        HttpServletRequest httpServletRequest = request.getNativeRequest(HttpServletRequest.class);
        return new RestValue<>(getName(), getArray(httpServletRequest, getName()));
    }

    private static String[] getArray(HttpServletRequest request, String name) {
        String[] values = request.getParameterValues(name);
        if (values == null || values.length == 0) {
            values = request.getParameterValues(name.concat("[]"));
        }
        return values;
    }
}
