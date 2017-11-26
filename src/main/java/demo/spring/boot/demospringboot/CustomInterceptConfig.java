package demo.spring.boot.demospringboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 继承了mvc的适配器 复写其中的addInterceptors方法
 */
@Configuration
public class CustomInterceptConfig extends WebMvcConfigurerAdapter {
    private static Logger LOGGER = LoggerFactory.getLogger(CustomInterceptConfig.class);


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /**
         * 自定义拦截器
         */
        HandlerInterceptor handlerInterceptor = new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                LOGGER.info("【拦截请求】: 请求路径 {}", request.getRequestURI());
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

            }
        };

        /**
         * 把创建的拦截器注册
         *
         * /**是拦截所有请求
         */
        registry.addInterceptor(handlerInterceptor).addPathPatterns("/**");

    }
}
