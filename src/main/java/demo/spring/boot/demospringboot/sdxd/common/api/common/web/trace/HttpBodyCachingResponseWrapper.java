package demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.RestContext;

import org.apache.commons.io.output.TeeOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

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
 * 17/2/4     melvin                 Created
 */
public class HttpBodyCachingResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream baos;
    private PrintStream ps;

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response The response to be wrapped
     * @throws IllegalArgumentException if the response is null
     */
    public HttpBodyCachingResponseWrapper(HttpServletResponse response) {
        super(response);

        this.baos = new ByteArrayOutputStream();
        this.ps = new PrintStream(baos);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStreamWrapper(new TeeOutputStream(super.getOutputStream(), ps));
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new ServletOutputStreamWrapper(new TeeOutputStream(super.getOutputStream(), ps)));
    }

    public String getBody() {
        return baos.toString();
    }

    public int getBodyLength() {
        return baos.size();
    }

    public boolean isHideBody() {
        return RestContext.isHideResponseBody();
    }

    public String getHiddenBody() {
        return String.format("...[%s bytes]", getBodyLength());
    }
}
