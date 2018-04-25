package demo.spring.boot.demospringboot.sdxd.common.api.common.web.configure;


import demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessListener;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.ThrottlingConfigure;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.ThrottlingHandler;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.configure.ConfigureSubscriber;
import demo.spring.boot.demospringboot.sdxd.common.redis.template.RedisClientTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.configure
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/3/29     melvin                 Created
 */
public class CommonConfig {

    @Bean
    public AccessListener accessListener() {
        return null;
    }

    @Bean
    public ConfigureSubscriber configureSubscriber() {
        return null;
    }

    @Bean
    @Autowired
    public ThrottlingConfigure throttlingConfigure(RedisClientTemplate redisClientTemplate, ConfigureSubscriber configureSubscriber) {
        return null;
    }

    @Bean
    @Autowired
    public ThrottlingHandler throttlingHandler(ThrottlingConfigure throttlingConfigure) {
        return null;
    }

//    @Bean
//    public ThrottlingHandler throttlingHandler() {
//        return null;
//    }
}
