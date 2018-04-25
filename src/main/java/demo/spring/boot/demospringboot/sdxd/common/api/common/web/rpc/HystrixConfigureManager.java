package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rpc;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JsonUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.rpc.HystrixSettings.DEFAULT_CONFIGURE;

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
public class HystrixConfigureManager {

    private static final String DEFAULT_PROPERTIES_PATH = "/opt/spring-boot/hystrix-configure.json";

    private String propertiesPath;

    private HystrixConfigure configure = new HystrixConfigure();

    public boolean isOpen() {
        return configure.isOpen();
    }

    public HystrixSettings getSettings(String key) {
        tryInitializeConfigure(key);
        return configure.getSettings().get(key);
    }

    public HystrixConfigure getConfigure() {
        tryInitializeAll();
        return configure;
    }

    public void setOpen(boolean open) {
        this.configure.setOpen(open);
        toProperties();
    }

    public void putConfigure(String key, HystrixSettings configure) {
        this.configure.getSettings().put(key, configure);
        toProperties();
    }

    public void putAll(HystrixSettings configure) {
        this.configure.getSettings().entrySet().forEach(entry -> {
            HystrixSettings settings = entry.getValue();
            settings.setCircuitBreakerErrorThresholdPercentage(configure.getCircuitBreakerErrorThresholdPercentage());
            settings.setCircuitBreakerRequestVolumeThreshold(configure.getCircuitBreakerRequestVolumeThreshold());
            settings.setCircuitBreakerSleepWindowInMilliseconds(configure.getCircuitBreakerSleepWindowInMilliseconds());
            settings.setThreadPoolCoreSize(configure.getThreadPoolCoreSize());
        });
        toProperties();
    }

    public void setPropertiesPath(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }

    private void tryInitializeConfigure(String key) {
        tryInitializeAll();
        if (!configure.getSettings().containsKey(key)) {
            configure.getSettings().put(key, DEFAULT_CONFIGURE);
        }
    }

    private void tryInitializeAll() {
        if (configure.getSettings().isEmpty()) {
            fromProperties();
        }
    }

    private void fromProperties() {
        HystrixConfigure configure = JsonUtil.fromJsonFile(new File(getPropertiesPath()), HystrixConfigure.class);
        if (configure != null) {
            this.configure = configure;
        }
    }

    private void toProperties() {
        JsonUtil.toJsonFile(configure, new File(getPropertiesPath()));
    }

    private String getPropertiesPath() {
        return StringUtils.isBlank(propertiesPath) ? DEFAULT_PROPERTIES_PATH : propertiesPath;
    }
}
