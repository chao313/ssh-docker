package demo.spring.boot.demospringboot.sdxd.common.api.common.web.filter;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.ByteStreams;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.ServletInputStreamWrapper;

import static com.google.common.collect.Iterables.toArray;

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
 * 16/11/16     melvin                 Created
 */
public class GzipBodyDecompressFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(GzipBodyDecompressFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * Analyzes servlet request for possible gzipped body.
     * When Content-Encoding header has "gzip" value and request method is POST we read all the
     * gzipped stream and is it haz any data unzip it. In case when gzip Content-Encoding header
     * specified but body is not actually in gzip format we will throw ZipException.
     *
     * @param servletRequest  servlet request
     * @param servletResponse servlet response
     * @param chain           filter chain
     * @throws IOException      throws when fails
     * @throws ServletException thrown when fails
     */
    @Override
    public final void doFilter(final ServletRequest servletRequest,
                               final ServletResponse servletResponse,
                               final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        boolean isGzipped = request.getHeader(HttpHeaders.CONTENT_ENCODING) != null
                && request.getHeader(HttpHeaders.CONTENT_ENCODING).contains("gzip");
        String contentTransferEncoding = request.getHeader("Content-Transfer-Encoding");
        boolean isBase64 =
                StringUtils.isNotBlank(contentTransferEncoding) &&
                        contentTransferEncoding.toLowerCase().equals("base64");
        boolean requestTypeSupported = HttpMethod.POST.equals(request.getMethod());
        if (isGzipped && !requestTypeSupported) {
            throw new IllegalStateException(request.getMethod()
                    + " is not supports gzipped body of parameters."
                    + " Only POST requests are currently supported.");
        }
        if (isGzipped) {
            log.debug("Gzip request found: {}", request.getRequestURI());
            try {
                request = new GzippedInputStreamWrapper((HttpServletRequest) servletRequest, isBase64);

//                ServletInputStream sis = request.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(sis));
//                StringBuffer buffer = new StringBuffer();
//                String line = null;
//                while ((line = br.readLine()) != null) {
//                    buffer.append(line);
//                }
//                log.debug("Read gzip data: \n{}\n", buffer);
            } catch (Exception e) {
                log.error("Init gzip input stream error.", e);
            }
        }
        chain.doFilter(request, response);

    }

    /**
     * @inheritDoc
     */
    @Override
    public final void destroy() {
    }

    /**
     * Wrapper class that detects if the request is gzipped and ungzipps it.
     */
    private final class GzippedInputStreamWrapper extends HttpServletRequestWrapper {
        /**
         * Default encoding that is used when post parameters are parsed.
         */
        private static final String DEFAULT_ENCODING = "ISO-8859-1";

        /**
         * Serialized bytes array that is a result of unzipping gzipped body.
         */
        private byte[] bytes;

        /**
         * Constructs a request object wrapping the given request.
         * In case if Content-Encoding contains "gzip" we wrap the input stream into byte array
         * to original input stream has nothing in it but hew wrapped input stream always returns
         * reproducible ungzipped input stream.
         *
         * @param request request which input stream will be wrapped.
         * @throws IOException when input stream reqtieval failed.
         */
        private GzippedInputStreamWrapper(final HttpServletRequest request, boolean base64) throws IOException {
            super(request);
            try {
                final InputStream origin = request.getInputStream();
                byte[] originBytes = ByteStreams.toByteArray(origin);
                byte[] decodedBytes = originBytes;
                if (base64) {
                    decodedBytes = Base64.getDecoder().decode(originBytes);
                }
                final ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
                final InputStream in = new GZIPInputStream(bais);
                bytes = ByteStreams.toByteArray(in);
            } catch (EOFException e) {
                bytes = new byte[0];
            }
        }


        /**
         * @return reproduceable input stream that is either equal to initial servlet input
         * stream(if it was not zipped) or returns unzipped input stream.
         * @throws IOException if fails.
         */
        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new ServletInputStreamWrapper(bytes);
        }

        /**
         * Need to override getParametersMap because we initially read the whole input stream and
         * servlet container won't have access to the input stream data.
         *
         * @return parsed parameters list. Parameters get parsed only when Content-Type
         * "application/x-www-form-urlencoded" is set.
         */
        @Override
        public Map<String, String[]> getParameterMap() {
            String contentEncodingHeader = getHeader(HttpHeaders.CONTENT_TYPE);
            if (!Strings.isNullOrEmpty(contentEncodingHeader)
                    && contentEncodingHeader.contains("application/x-www-form-urlencoded")) {
                Map<String, String[]> params = new HashMap<>(super.getParameterMap());
                try {
                    params.putAll(parseParams(new String(bytes)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return params;
            } else {
                return super.getParameterMap();
            }
        }

        /**
         * parses params from the byte input stream.
         *
         * @param body request body serialized to string.
         * @return parsed parameters map.
         * @throws UnsupportedEncodingException if encoding provided is not supported.
         */
        private Map<String, String[]> parseParams(final String body)
                throws UnsupportedEncodingException {
            String characterEncoding = getCharacterEncoding();
            if (null == characterEncoding) {
                characterEncoding = DEFAULT_ENCODING;
            }
            final Multimap<String, String> parameters = ArrayListMultimap.create();
            for (String pair : body.split("&")) {
                if (Strings.isNullOrEmpty(pair)) {
                    continue;
                }
                int idx = pair.indexOf("=");

                String key = null;
                if (idx > 0) {
                    key = URLDecoder.decode(pair.substring(0, idx), characterEncoding);
                } else {
                    key = pair;
                }
                String value = null;
                if (idx > 0 && pair.length() > idx + 1) {
                    value = URLDecoder.decode(pair.substring(idx + 1), characterEncoding);
                } else {
                    value = null;
                }
                parameters.put(key, value);
            }

            return parameters.asMap().entrySet().stream().
                        filter(entry -> entry.getValue() != null).
                        collect(Collectors.toMap(Map.Entry::getKey, entry -> toArray(entry.getValue(), String.class)));
        }
    }
}