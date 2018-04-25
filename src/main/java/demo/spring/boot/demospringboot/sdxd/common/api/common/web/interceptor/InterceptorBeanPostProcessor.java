package demo.spring.boot.demospringboot.sdxd.common.api.common.web.interceptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import java.util.List;

public class InterceptorBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private RestInterceptor interceptor;

    private List<PreHandler> defaultPreHandlers;

    public InterceptorBeanPostProcessor(RestInterceptor interceptor, List<PreHandler> defaultPreHandlers) {
        this.interceptor = interceptor;
        this.defaultPreHandlers = defaultPreHandlers;
        defaultPreHandlers.stream().filter(handler -> handler != null).forEach(interceptor::addPreHandler);
    }

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, String beanName) throws BeansException {
        boolean result = super.postProcessAfterInstantiation(bean, beanName);
        if(getInterceptor() == null) {
            return result;
        }

//        Class<?>[] interfaces =  bean.getClass().getInterfaces();
//        if (interfaces == null || interfaces.length == 0) {
//            return result;
//        }

        if (PreHandler.class.isInstance(bean)) {
            getInterceptor().addPreHandler(PreHandler.class.cast(bean));
        } else if (PostHandler.class.isInstance(bean)) {
            getInterceptor().addPostHandler(PostHandler.class.cast(bean));
        } else if (CompleteHandler.class.isInstance(bean)) {
            getInterceptor().addCompleteHandler(CompleteHandler.class.cast(bean));
        }

        return result;
    }

    private RestInterceptor getInterceptor() {
        return interceptor;
    }
}
