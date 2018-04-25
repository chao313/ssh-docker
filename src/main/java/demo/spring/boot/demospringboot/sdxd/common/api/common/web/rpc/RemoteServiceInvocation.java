package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rpc;

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
 * 2017/7/12     melvin                 Created
 */
public class RemoteServiceInvocation {

    private long period;
    private Object result;
    private Throwable throwable;

    RemoteServiceInvocation(long period, Object result) {
        this.period = period;
        this.result = result;
    }

    RemoteServiceInvocation(long period, Throwable throwable) {
        this.period = period;
        this.throwable = throwable;
    }

    public long getPeriod() {
        return period;
    }

    public Object getResult() {
        return result;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
