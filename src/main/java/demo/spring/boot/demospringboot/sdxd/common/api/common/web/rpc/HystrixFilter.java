package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rpc;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.alibaba.dubbo.rpc.*;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpTracer.DEBUG;

@Activate(group = Constants.CONSUMER)
public class HystrixFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(HystrixFilter.class);

    private HystrixConfigureManager hystrixConfigureManager;

    public HystrixConfigureManager getHystrixConfigureManager() {
        return hystrixConfigureManager;
    }

    public void setHystrixConfigureManager(HystrixConfigureManager hystrixConfigureManager) {
        this.hystrixConfigureManager = hystrixConfigureManager;
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (!isHystrixEnabled()) {
            return invoker.invoke(invocation);
        }
        HystrixSettings settings = getSettings(invoker, invocation);
        if (settings == null) {
            return invoker.invoke(invocation);
        }
        DubboHystrixCommand command = new DubboHystrixCommand(settings, invoker, invocation);
        Result result = command.execute();
//        if (invocation.getMethodName().equals("isRegistered")) {
            if (result.hasException()) {
                log.error("Invoke dubbo error!", result.getException());
            }
            Object value = result.getValue();
            DEBUG(log, "Receive return value from remote: {}", JsonUtil.toJson(value));
//        }
        return result;
    }

    private boolean isHystrixEnabled() {
        HystrixConfigureManager manager = getManager();
        return manager != null && manager.isOpen();
    }

    private HystrixSettings getSettings(Invoker<?> invoker, Invocation invocation) {
        HystrixConfigureManager manager = getManager();
        if (manager == null) {
            return null;
        }
        String key = String.format("%s.%s_%d",
                invoker.getInterface().getName(),
                invocation.getMethodName(),
                invocation.getArguments() == null ? 0 : invocation.getArguments().length);
        return manager.getSettings(key);
    }

    private HystrixConfigureManager getManager() {
        if (this.hystrixConfigureManager == null) {
            ApplicationContext context = ServiceBean.getSpringContext();
            this.hystrixConfigureManager = context == null ? null : context.getBean(HystrixConfigureManager.class);
        }
        return this.hystrixConfigureManager;
    }
}
