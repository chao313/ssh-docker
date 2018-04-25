package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/3/3     melvin                 Created
 */
public class ConfigureEvent {

    private String type;
    private String name;
    private String value;

    private ConfigureEvent() {}

    public ConfigureEvent(String type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public byte[] bytes() {
        ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            return mapper.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static ConfigureEvent event(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            return mapper.readValue(bytes, ConfigureEvent.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        ConfigureEvent event = new ConfigureEvent("throttling-configure-changed", "/pvt/v1.0.0/loans", "5");
        byte[] bytes = event.bytes();
        ConfigureEvent ue = ConfigureEvent.event(bytes);
        System.out.println(ue.type + ": " + ue.name + "=" + ue.value);
    }
}
