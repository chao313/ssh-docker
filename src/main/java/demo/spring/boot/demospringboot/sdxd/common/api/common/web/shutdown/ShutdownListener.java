package demo.spring.boot.demospringboot.sdxd.common.api.common.web.shutdown;

import com.alibaba.dubbo.remoting.transport.netty.NettyClient;


import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.Tasks;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure.ConfigureSubscriber;
import demo.spring.boot.demospringboot.sdxd.common.redis.template.RedisClientTemplate;

import org.jboss.netty.channel.ChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebAppRootListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.servlet.ServletContextEvent;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.api.service
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/2/27     melvin                 Created
 */
public class ShutdownListener extends WebAppRootListener {

    private static final Logger log = LoggerFactory.getLogger(ShutdownListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        log.debug("Start shutdown.");
        super.contextDestroyed(event);
        Tasks.shutdown();
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
        if (applicationContext != null) {
            ConfigureSubscriber configureSubscriber = applicationContext.getBean(ConfigureSubscriber.class);
            log.debug("Try to close configure subscriber {}", configureSubscriber);
            if (configureSubscriber != null) {
                configureSubscriber.close();
                log.debug("Close api configure complete.");
            }
            RedisClientTemplate redis = applicationContext.getBean(RedisClientTemplate.class);
            log.debug("Try to close redis connection {}", redis);
            if (redis != null) {
                redis.disconnect();
                log.debug("Close redis connection complete.");
            }
        }

        try {
            final Class<?> protocolConfig = Class.forName("com.alibaba.dubbo.config.ProtocolConfig");
            final Method method = protocolConfig.getMethod("destroyAll");
            method.invoke(protocolConfig);
        } catch (Exception e) {
            log.error("Release dubbo failed.", e);
        } finally {
//            try {
//                synchronized (this) {
//                    this.wait(500L);
//                }
//            } catch (InterruptedException e) {
//            }
            log.debug("Release dubbo complete.");
        }
        releaseNettyClientExternalResources();
        ThreadLocalImmolater immolater = new ThreadLocalImmolater();
        immolater.immolate();
    }

    private void releaseNettyClientExternalResources() {
        try {
            Field field = NettyClient.class.getDeclaredField("channelFactory");
            field.setAccessible(true);
            ChannelFactory channelFactory = (ChannelFactory) field.get(NettyClient.class);
            channelFactory.releaseExternalResources();
            field.setAccessible(false);
            log.info("Release NettyClient's external resources");
        } catch (Exception e){
            log.error("Release NettyClient's external resources error", e);
        }
    }
}
