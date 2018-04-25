package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import org.apache.commons.lang3.StringUtils;
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
public class RestRequestParameter extends RestParameter<String> {

    public RestRequestParameter(String name) {
        super(name, false);
    }

    @Override
    public boolean existValue(NativeWebRequest request) {
        return StringUtils.isNotBlank(getValue(request));
    }

    @Override
    protected RestValue<String> takeValue(NativeWebRequest request) {
        HttpServletRequest httpServletRequest = request.getNativeRequest(HttpServletRequest.class);
        return new RestValue<>(getName(), httpServletRequest.getParameter(getName()));
    }
}
