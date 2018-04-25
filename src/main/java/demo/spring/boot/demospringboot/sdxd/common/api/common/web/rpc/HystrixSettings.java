package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rpc;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.rpc
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 2017/8/30     melvin                 Created
 */
public class HystrixSettings {

    static HystrixSettings DEFAULT_CONFIGURE = new HystrixSettings(5, 1000, 100, 30);

    private int circuitBreakerRequestVolumeThreshold; //请求数至少达到多大才进行熔断计算
    private int circuitBreakerSleepWindowInMilliseconds; //半开的触发试探休眠时间
    private int circuitBreakerErrorThresholdPercentage; //熔断器错误比率阈值
    private int threadPoolCoreSize;

    public HystrixSettings() {}

    public HystrixSettings(
            int circuitBreakerRequestVolumeThreshold, int circuitBreakerSleepWindowInMilliseconds,
            int circuitBreakerErrorThresholdPercentage, int threadPoolCoreSize) {
        this.circuitBreakerRequestVolumeThreshold = circuitBreakerRequestVolumeThreshold;
        this.circuitBreakerSleepWindowInMilliseconds = circuitBreakerSleepWindowInMilliseconds;
        this.circuitBreakerErrorThresholdPercentage = circuitBreakerErrorThresholdPercentage;
        this.threadPoolCoreSize = threadPoolCoreSize;
    }

    public int getCircuitBreakerRequestVolumeThreshold() {
        return circuitBreakerRequestVolumeThreshold == 0 ? DEFAULT_CONFIGURE.getCircuitBreakerRequestVolumeThreshold() : circuitBreakerRequestVolumeThreshold;
    }

    public void setCircuitBreakerRequestVolumeThreshold(int circuitBreakerRequestVolumeThreshold) {
        this.circuitBreakerRequestVolumeThreshold = circuitBreakerRequestVolumeThreshold;
    }

    public int getCircuitBreakerSleepWindowInMilliseconds() {
        return circuitBreakerSleepWindowInMilliseconds == 0 ? DEFAULT_CONFIGURE.getCircuitBreakerSleepWindowInMilliseconds() : circuitBreakerSleepWindowInMilliseconds;
    }

    public void setCircuitBreakerSleepWindowInMilliseconds(int circuitBreakerSleepWindowInMilliseconds) {
        this.circuitBreakerSleepWindowInMilliseconds = circuitBreakerSleepWindowInMilliseconds;
    }

    public int getCircuitBreakerErrorThresholdPercentage() {
        return circuitBreakerErrorThresholdPercentage == 0 ? DEFAULT_CONFIGURE.getCircuitBreakerErrorThresholdPercentage() : circuitBreakerErrorThresholdPercentage;
    }

    public void setCircuitBreakerErrorThresholdPercentage(int circuitBreakerErrorThresholdPercentage) {
        this.circuitBreakerErrorThresholdPercentage = circuitBreakerErrorThresholdPercentage;
    }

    public int getThreadPoolCoreSize() {
        return threadPoolCoreSize == 0 ? DEFAULT_CONFIGURE.getThreadPoolCoreSize() : threadPoolCoreSize;
    }

    public void setThreadPoolCoreSize(int threadPoolCoreSize) {
        this.threadPoolCoreSize = threadPoolCoreSize;
    }
}
