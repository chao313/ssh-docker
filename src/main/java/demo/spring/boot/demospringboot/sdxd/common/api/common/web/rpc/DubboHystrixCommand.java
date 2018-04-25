package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rpc;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.netflix.hystrix.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DubboHystrixCommand extends HystrixCommand<Result> {

    private static Logger logger = LoggerFactory.getLogger(DubboHystrixCommand.class);
    private static final int DEFAULT_THREADPOOL_CORE_SIZE = 30;
    private Invoker<?> invoker;
    private Invocation invocation;
    
    public DubboHystrixCommand(HystrixSettings settings, Invoker<?> invoker, Invocation invocation){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(invoker.getInterface().getName()))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(String.format("%s_%d", invocation.getMethodName(),
                                                                                 invocation.getArguments() == null ? 0 : invocation.getArguments().length)))
              .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                                            .withCircuitBreakerRequestVolumeThreshold(settings.getCircuitBreakerRequestVolumeThreshold())//10秒钟内至少19此请求失败，熔断器才发挥起作用
                                            .withCircuitBreakerSleepWindowInMilliseconds(settings.getCircuitBreakerSleepWindowInMilliseconds())//熔断器中断请求30秒后会进入半打开状态,放部分流量过去重试
                                            .withCircuitBreakerErrorThresholdPercentage(settings.getCircuitBreakerErrorThresholdPercentage())//错误率达到50开启熔断保护
                                            .withExecutionTimeoutEnabled(false))//使用dubbo的超时，禁用这里的超时
              .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(getThreadPoolCoreSize(settings, invoker.getUrl()))));//线程池为30

        this.invoker=invoker;
        this.invocation=invocation;
    }

    private static int getThreadPoolCoreSize(HystrixSettings configure, URL url) {
        if (configure.getThreadPoolCoreSize() <= 0) {
            return getThreadPoolCoreSize(url);
        }
        return configure.getThreadPoolCoreSize();
    }
    
    /**
     * 获取线程池大小
     * 
     * @param url
     * @return
     */
    private static int getThreadPoolCoreSize(URL url) {
        if (url != null) {
            int size = url.getParameter("ThreadPoolCoreSize", DEFAULT_THREADPOOL_CORE_SIZE);
            if (logger.isDebugEnabled()) {
                logger.debug("ThreadPoolCoreSize:" + size);
            }
            return size;
        }

        return DEFAULT_THREADPOOL_CORE_SIZE;

    }

    @Override
    protected Result run() throws Exception {
//        HystrixCommandKey commandKey = this.getCommandKey();
//        String key = String.format("%s.%s", invoker.getInterface().getName(), commandKey.name());
//        if (breaking(key)) {
//            count(key);
//            throw new RpcException();
//        }
//        count(key);
        return invoker.invoke(invocation);
    }

//    private static Map<String, Integer> counts = Maps.newConcurrentMap();
//    private static boolean breaking(String key) {
//        Integer value = counts.get(key);
//        if (value == null) {
//            value = 1;
//        }
//        return value % 10 == 0;
//    }
//    private static void count(String key) {
//        Integer value = counts.get(key);
//        if (value == null) {
//            value = 1;
//            counts.put(key, value);
//            return;
//        }
//        value ++;
//        counts.put(key, value);
//        logger.debug("rpc invoke {} count: {}", key, value);
//    }
}
