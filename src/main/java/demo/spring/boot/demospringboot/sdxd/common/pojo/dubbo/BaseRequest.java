package demo.spring.boot.demospringboot.sdxd.common.pojo.dubbo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by qiuyangjun on 2016/10/26.
 *
 * @packageName:demo.spring.boot.demospringboot.sdxd.common.api.message.dubbo.request
 * @CreateDate:2016/10/26
 * @UpdateDate:2016/10/26
 * @Description:请求参数基类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BaseRequest implements Serializable {
    /**
     * @参数名称:请求序列号
     * @参数用途:用于接口幂等性校验
     */
    @NotNull
    private String requestId;
    /**
     * @参数名称:系统ID
     * @参数用途:用于各系统鉴权
     */
    private String systemId;
}
