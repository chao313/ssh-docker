package demo.spring.boot.demospringboot.sdxd.common.api.common.web.security;

import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.security
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/1/13     melvin                 Created
 */
public class Subject {

    private String value;

    private boolean access;

    private Map<String, Object> params;

    private List<String> prefixKeys;
    private List<String> tokenRequiredKeys;
    private List<String> hidingKeys;

    public Subject(String value) {
        this(value, false, Maps.newHashMap(), null);
    }

    public Subject(String value, boolean access, Map<String, Object> params) {
        this(value, access, params, null);
    }

    public Subject(String value, boolean access, Map<String, Object> params, List<String> prefixKeys) {
        this(value, access, params, prefixKeys, null, null);
    }

    public Subject(String value, boolean access, Map<String, Object> params, List<String> prefixKeys, List<String> tokenRequiredKeys, List<String> hidingKeys) {
        this.value = value;
        this.access = access;
        this.params = params;
        this.prefixKeys = prefixKeys;
        this.tokenRequiredKeys = tokenRequiredKeys;
        this.hidingKeys = hidingKeys;
        if (this.params != null && this.prefixKeys != null) {
            this.params = Maps.newHashMap();
            this.params.putAll(params);
            this.params.put("prefixKeys", prefixKeys);
        }
        if (this.params != null && this.prefixKeys == null) {
            //noinspection unchecked
            this.prefixKeys = (List) this.params.get("prefixKeys");
        }
    }

    public String getKey() {
        if (params == null || prefixKeys == null || prefixKeys.isEmpty()) {
            return getValue();
        }
        String prefix = params.entrySet().stream().
                filter(entry -> prefixKeys.contains(entry.getKey())).
                reduce("", (text, entry) -> {
                    String append = String.valueOf(entry.getValue());
                    return StringUtils.isBlank(text) ? append : text.concat(":").concat(append);
                }, (v1, v2) -> v1);
        return String.format("%s:%s", prefix, getValue());
    }

    public String getValue() {
        return value;
    }

    public <T> T get(String key) {
        if (this.params == null || StringUtils.isBlank(key)) {
            return null;
        }
        //noinspection unchecked
        return (T) this.params.get(key);
    }

    public Map<String, Object> getTokenRequiredParams() {
        if (params == null || tokenRequiredKeys == null || tokenRequiredKeys.isEmpty()) {
            return Maps.newHashMap();
        }
        Map<String, Object> requiredParams = params.entrySet().stream().
                filter(entry -> tokenRequiredKeys.contains(entry.getKey())).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        requiredParams.put("prefixKeys", params.get("prefixKeys"));
        return requiredParams;
    }

    public boolean isAccess() {
        return access;
    }

    public Map<String, Object> getParams() {
        if (params != null && hidingKeys != null && !hidingKeys.isEmpty()) {
            Map<String, Object> hidden = Maps.newHashMap(params);
            hidingKeys.forEach(hidden::remove);
            return hidden;
        }
        return params;
    }
}
