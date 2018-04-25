package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.NativeWebRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

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
public class RestHeaderParameter extends RestParameter<String> {

    private static final Logger log = LoggerFactory.getLogger(RestHeaderParameter.class);

    private String[] alias;
    private RestParameterValidator[] validators;

    private List<String> names;

    public RestHeaderParameter(String name, RestParameterValidator... validators) {
        this(name, null, validators);
    }

    public RestHeaderParameter(String name, String[] alias, RestParameterValidator... validators) {
        super(name, true);

        this.alias = alias;
        this.validators = validators;

        this.names = getAlias() == null ? Lists.newArrayList(getName()) : Lists.asList(getName(), getAlias());
    }

    public RestParameterValidator[] getValidators() {
        return validators;
    }

    public String[] getAlias() {
        return alias;
    }

    public List<String> getNames() {
        return names;
    }

    @Override
    public boolean existValue(NativeWebRequest request) {
        return StringUtils.isNotBlank(getValue(request));
    }

    @Override
    public String getStatement(NativeWebRequest request) {
        RestValue<String> restValue = getValuePairs(request);
        String name = restValue == null || restValue.getName() == null ? getName() : restValue.getName();
        String text = restValue == null || restValue.getValue() == null ? "" : restValue.getValue();
        return name.concat("=").concat(text);
    }

    @Override
    protected RestValue<String> takeValue(NativeWebRequest request) {
        HttpServletRequest httpServletRequest = request.getNativeRequest(HttpServletRequest.class);
        RestValue<String> restValue = fetchValue(httpServletRequest);
        String value = restValue.getValue();
        if (StringUtils.isNotBlank(value)) {
            try {
                value = URLDecoder.decode(value, httpServletRequest.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                log.warn("Failed to decode value {} by encoding: {}, error: {}",
                        value, httpServletRequest.getCharacterEncoding(), e.getMessage());
            }
        }
        return new RestValue<>(restValue.getName(), value);
    }

    private RestValue<String> fetchValue(HttpServletRequest httpServletRequest) {
        String name = getName();
        String value = httpServletRequest.getHeader(name);
//        DEBUG(log, "Get header by {}, value is {}", name, value);
        if (StringUtils.isNotBlank(value)) {
            return new RestValue<>(name, value);
        }
        if (StringUtils.isBlank(value) && alias != null) {
            for (String aliasName : alias) {
                value = httpServletRequest.getHeader(aliasName);
//                DEBUG(log, "Get header by alias {}, value is {}", aliasName, value);
                if (StringUtils.isNotBlank(value)) {
                    return new RestValue<>(aliasName, value);
                }
            }
        }
        return new RestValue<>(name, value);
    }
}
