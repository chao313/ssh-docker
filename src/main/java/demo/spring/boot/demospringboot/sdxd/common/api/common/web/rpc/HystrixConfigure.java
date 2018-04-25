package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rpc;

import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

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
public class HystrixConfigure {

    private boolean open;
    private ConcurrentMap<String, HystrixSettings> settings = Maps.newConcurrentMap();

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public ConcurrentMap<String, HystrixSettings> getSettings() {
        return settings;
    }

    public void setSettings(ConcurrentMap<String, HystrixSettings> settings) {
        this.settings = settings;
    }
}
