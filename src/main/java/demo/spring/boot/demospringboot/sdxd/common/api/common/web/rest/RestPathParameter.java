package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

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
public class RestPathParameter extends RestParameter<String> {

    public RestPathParameter(String name) {
        super(name, false);
    }

    @Override
    public boolean existValue(NativeWebRequest request) {
        return StringUtils.isNotBlank(getValue(request));
    }

    @Override
    protected RestValue<String> takeValue(NativeWebRequest request) {
        //noinspection unchecked
        Map<String, String> uriTemplateVars =
                (Map<String, String>) request.getAttribute(
                        HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        return new RestValue<>(getName(), uriTemplateVars.get(getName()));
    }
}
