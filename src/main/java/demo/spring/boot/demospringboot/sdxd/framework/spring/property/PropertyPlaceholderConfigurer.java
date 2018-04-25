package demo.spring.boot.demospringboot.sdxd.framework.spring.property;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Created by qiuyangjun on 2015/1/15.
 */

/**
 * Description:依赖zookeeperConfig.jar之后,不再使用本类方法获取配置文件的数据
 * 改为使用CacheManager.getStaticMapValue(key)方法获取
 *
 * @author xiongweitao
 * @PackageName:demo.spring.boot.demospringboot.sdxd.common.api.framework.spring.property
 * @ClassName:PropertyPlaceholderConfigurer
 * @date 2016年3月9日 上午11:28:27
 */
@SuppressWarnings("all")
public class PropertyPlaceholderConfigurer extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {
    private static Map ctxPropertiesMap;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        ctxPropertiesMap = new HashMap();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
        }
    }

    /**
     * Description:依赖zookeeperConfig.jar之后,不再使用本类方法获取配置文件的数据
     * 改为使用CacheManager.getStaticMapValue(key)方法获取
     *
     * @author xiongweitao
     * @date 2016年3月9日 上午11:32:28
     */
    public static Object getContextProperty(String name) {
        if (ctxPropertiesMap == null) {
            return null;
        }
        return ctxPropertiesMap.get(name);
//		return CacheManager.getStaticMapValue(name);
    }
}
