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
 * 2017/9/5     melvin                 Created
 */
public class SettingsEntry {

    private String key;
    private HystrixSettings settings;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HystrixSettings getSettings() {
        return settings;
    }

    public void setSettings(HystrixSettings settings) {
        this.settings = settings;
    }
}
