package demo.spring.boot.demospringboot.sdxd.common.pojo.enums;

/**
 * @author tanxiongying
 */
public enum DeviceType {

    ANDROID("ANDROID", "ANDROID"),
    IOS("IOS", "IOS"),
    ;

    private String code;
    private String msg;

    DeviceType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public static DeviceType getByCode(String code) {
        for(DeviceType value : values()){
            if(value.getCode().equals(code)){
                return value;
            }
        }
        return null;
    }

    public String getMsg() {
        return msg;
    }
}
