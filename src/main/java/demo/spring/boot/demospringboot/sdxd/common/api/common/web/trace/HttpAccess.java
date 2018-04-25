package demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 2017/7/12     melvin                 Created
 */
public class HttpAccess {

    private HttpBodyCachingRequestWrapper request;
    private HttpBodyCachingResponseWrapper response;

    public HttpAccess(HttpBodyCachingRequestWrapper request, HttpBodyCachingResponseWrapper response) {
        this.request = request;
        this.response = response;
    }

    public long getPeriod() {
        return request.getPeriod();
    }

    public String getResponseBody() {
        return response.getBody();
    }
}
