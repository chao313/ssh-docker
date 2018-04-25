package demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace;

import com.google.common.collect.Sets;



import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenGenerator;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.token.TokenValidation;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.HttpUtil;
import demo.spring.boot.demospringboot.sdxd.common.utils.BillNoUtils;
import io.reactivex.Observable;


import org.apache.catalina.util.ParameterMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.buf.UDecoder;
import org.apache.tomcat.util.http.Parameters;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.filter
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/1/24     melvin                 Created
 */
public class HttpBodyCachingRequestWrapper extends HttpServletRequestWrapper {

    public static final String HIDE_BODY_KEY = "hideBody";

    private static final String CONTEXT_ID_FORMAT = "%s:%s";

    private static final Set<String> PARSE_IGNORE_CONTENT_TYPE =
            Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE);

    private String id;
    private volatile byte[] body;

    private boolean parsed;
    private Parameters parameters;
    private ParameterMap<String, String[]> parameterMap;

    private long requestTime;

    private String contextId;
    private String principal;
    private String remoteAddress;

    private TokenValidation tokenValidation;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public HttpBodyCachingRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        this.id = BillNoUtils.GenerateBillNo();
        RestContext.setContextId(getContextId(), new Date());
        RestContext.setLoggingHeaders(getLoggingHeaders());
        RestContext.setPrincipal(getPrincipal());
        RestContext.setRemoteAddress(getRemoteAddress());

        this.body = StreamUtils.copyToByteArray(request.getInputStream());

        this.parsed = false;
        this.parameters = new Parameters();
        this.parameters.setQuery(MessageBytes.newInstance());
        this.parameters.setURLDecoder(new UDecoder());
        this.parameterMap = new ParameterMap<>();

        this.requestTime = System.currentTimeMillis();

        this.setAttribute(HIDE_BODY_KEY, isHideBody());

//        Tuple3<Boolean, Boolean, Boolean> hideBody = RestContext.isHideAll(request) ? new Tuple3<>(true, true, true) : getHideBody();
//        RestContext.setHideBody(hideBody);
        Tuple3<Boolean, Boolean, Boolean> logging = getLogging();
        RestContext.setLogging(logging);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamWrapper(body);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return parameters.getParameterNames();
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        if (!parsed) {
            parseParameters();
        }
        return parameters.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> value = super.getParameterMap();
        if (value != null && value.size() > 0) {
            return value;
        }
        if (parameterMap.isLocked()) {
            return parameterMap;
        }

        Enumeration<String> enumeration = getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String[] values = getParameterValues(name);
            parameterMap.put(name, values);
        }

        parameterMap.setLocked(true);

        return parameterMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null && values.length > 0) {
            return values;
        }
        if (!parsed) {
            parseParameters();
        }
        return parameters.getParameterValues(name);
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        try {
            return new String(body, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public String getHiddenBody() {
        return String.format("...[%s bytes]", getBodyLength());
    }

    public int getBodyLength() {
        return body != null ? body.length : 0;
    }

    public boolean isHideAll() {
//        if (RestContext.isHideAll(this)) {
//            return true;
//        }
//        Tuple3<Boolean, Boolean, Boolean> hideBody = getHideBody();
//        return hideBody == null ? false : hideBody.v3();
        Tuple3<Boolean, Boolean, Boolean> hideBody = getLogging();
        return hideBody == null ? true : hideBody.v3();
    }

    public boolean isHideBody() {
        Tuple3<Boolean, Boolean, Boolean> hideBody = getLogging();
        return hideBody == null ? false : hideBody.v1();
    }

    public long getPeriod() {
        long current = System.currentTimeMillis();
        return current - requestTime;
    }

    public String getContextId() {
        if (StringUtils.isBlank(contextId)) {
            contextId = String.format(CONTEXT_ID_FORMAT, id, getRequestPath());
        }
        return contextId;
    }

    public Map<String, String> getLoggingHeaders() {
        Enumeration<String> enumeration = getHeaderNames();
        return Observable.fromIterable(() -> new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public String next() {
                return enumeration.nextElement();
            }
        }).filter(RestContext::isLoggingHeader).toMap(name -> name, this::getHeader).blockingGet();
    }

    public String getPrincipal() {
        if (StringUtils.isBlank(principal)) {
            if (getTokenValidation() != null && getTokenValidation().getSubject() != null) {
                principal = getTokenValidation().getSubject().getValue();
            }
        }
        return principal;
    }

    public String getRemoteAddress() {
        if (StringUtils.isBlank(remoteAddress)) {
            remoteAddress = HttpUtil.getRemoteIp(this);
        }
        return remoteAddress;
    }

    public TokenValidation getTokenValidation() {
        if (tokenValidation == null) {
            String token = HttpUtil.getToken(this);
            if (StringUtils.isBlank(token)) {
                return null;
            }
            tokenValidation = TokenGenerator.isValid(RestContext.getGlobalTokenConfiguration(), token);
        }
        return tokenValidation;
    }

    public String getRequestPath() {
        return this.getPathInfo().concat(":").concat(this.getMethod());
    }

    private Tuple3<Boolean, Boolean, Boolean> getLogging() {
        String path = getRequestPath();
        Tuple2<Boolean, Boolean> logging = RestContext.getLoggingApi(path);
        return new Tuple3<>(logging == null ? false : logging.v1(), logging == null ? false : logging.v2(), logging == null);
    }

    private void parseParameters() {
        if (PARSE_IGNORE_CONTENT_TYPE.contains(this.getContentType())) {
            return;
        }
        String enc = getCharacterEncoding();
        if (StringUtils.isNotBlank(enc)) {
            parameters.setEncoding(enc);
            parameters.setQueryStringEncoding(enc);
        } else {
            parameters.setEncoding(org.apache.coyote.Constants.DEFAULT_CHARACTER_ENCODING);
            parameters.setQueryStringEncoding(org.apache.coyote.Constants.DEFAULT_CHARACTER_ENCODING);
        }
        parameters.handleQueryParameters();
        byte[] bytes = Arrays.copyOf(this.body, this.body.length);
        parameters.processParameters(bytes, 0, bytes.length);
        parsed = true;
    }
}
