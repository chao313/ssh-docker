package demo.spring.boot.demospringboot.sdxd.framework.constant.enums;

/**
 * Package Name: demo.spring.boot.demospringboot.sdxd.common.api.framework.constant.enums
 * Description:
 * Author: qiuyangjun
 * Create Date:2015/2/3
 */
public enum PermissionResult {
    PERMISSION_SUCCESS("验证通过"),SESSION_TIMEOUT("登陆超时"),PERMISSION_DENIED("没有权限");

    private String type;

    private PermissionResult(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
