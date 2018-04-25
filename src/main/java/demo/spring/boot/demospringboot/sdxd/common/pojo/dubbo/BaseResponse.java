package demo.spring.boot.demospringboot.sdxd.common.pojo.dubbo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by qiuyangjun on 2016/11/8.
 *
 * @packageName:demo.spring.boot.demospringboot.sdxd.common.api.framework.dubbo
 * @CreateDate:2016/11/8
 * @UpdateDate:2016/11/8
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BaseResponse implements Serializable {
    /**
     * @参数名称:请求序列号
     * @参数用途:用于接口幂等性校验
     */
    private String requestId;
}
