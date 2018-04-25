package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.NativeWebRequest;

import java.io.IOException;

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
public class RestBodyParameter extends RestParameter<String> {

    private static final Logger log = LoggerFactory.getLogger(RestBodyParameter.class);

    public RestBodyParameter() {
        super("body", false);
    }

    @Override
    public boolean existValue(NativeWebRequest request) {
        return StringUtils.isNotBlank(getValue(request));
    }

    @Override
    protected RestValue<String> takeValue(NativeWebRequest request) {
        HttpServletRequest httpServletRequest = request.getNativeRequest(HttpServletRequest.class);

        try {
            return new RestValue<>(getName(), httpServletRequest.getReader().lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual), true);
        } catch (IOException e) {
            log.warn("Failed to get http request body, path: {}: {}",
                    httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
        }

        return new RestValue<>(getName(), "", true);
    }
}
