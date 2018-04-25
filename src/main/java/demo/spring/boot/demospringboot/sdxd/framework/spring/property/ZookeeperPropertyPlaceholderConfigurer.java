package demo.spring.boot.demospringboot.sdxd.framework.spring.property;

import demo.spring.boot.demospringboot.sdxd.framework.constant.SysConstants;
import demo.spring.boot.demospringboot.sdxd.framework.utils.DESWrapper;
import demo.spring.boot.demospringboot.sdxd.framework.zookeeper.ZookeeperClient;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ZooKeeper属性占位符配置
 * 
 * 继承SpringFramework的属性占位符配置方法，从ZooKeeper上获取配置信息并加载
 *
 * @author wangzhaolin
 * 
 * @since 2017年12月26日
 * 
 * @version 1.0
 */
public class ZookeeperPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	
	/**
	 * 日志打印
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ZookeeperPropertyPlaceholderConfigurer.class);
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		
		Object zkAddress = props.get(SysConstants.ZK_ADDRESS_KEY);
		Object appName = props.get(SysConstants.ZK_APP_NAME_KEY);
		if (zkAddress != null && appName != null) {
			
			// properties配置
			Map<String, String> zkProperties = new HashMap<String, String>();
			// ZooKeeper客户端实例
			ZookeeperClient curatorClient = null;
			// Curator实例
			CuratorFramework curator = null;
			
			try {
				// 获取ZooKeeper客户端实例
				curatorClient = ZookeeperClient.getInstance();
				curator = curatorClient.getCurator(String.valueOf(zkAddress));
				
				// 读取ZooKeeper通用properties（app_name=“all”）配置
				zkProperties.putAll(curatorClient.readData(curator, "/".concat(SysConstants.ZK_PROPERTIES_NODE_NAME).concat("/").concat(SysConstants.ZK_PROPERTIES_NODE_ALL)));
				
				// 读取ZooKeeper组件properties配置【注：各组件下的属性配置若与通用属性重复，则会覆盖通用属性配置】
				zkProperties.putAll(curatorClient.readData(curator, "/".concat(SysConstants.ZK_PROPERTIES_NODE_NAME).concat("/").concat(String.valueOf(appName))));
				
				if (LOG.isInfoEnabled()) {
					LOG.info("读取ZooKeeper Properties：{}", zkProperties);
				}
				
				/*
				 * 对Properties进行解密
				 */
				String encryptValue = null;
				for (Map.Entry<String, String> prop : zkProperties.entrySet()) {
					encryptValue = prop.getValue(); // 加密的值
					
					/*
					 *  将解密后的数据添加到Spring启动加载数据
					 * 【注：组件properties文件中的配置会覆盖ZooKeeper上的配置】
					 */
					props.putIfAbsent(prop.getKey(), DESWrapper.decrypt(encryptValue, SysConstants.DES_WRAPPER_KEY));
				}
			} catch (Exception e) {
				if (LOG.isErrorEnabled()) {
					LOG.error("加载ZooKeeper properties数据异常", e);
				}
			}
		} else {
			if (LOG.isErrorEnabled()) {
				LOG.error("配置项【{}】或【{}】缺失，请检查properties.", SysConstants.ZK_ADDRESS_KEY, SysConstants.ZK_APP_NAME_KEY);
			}
		}
		
		super.processProperties(beanFactoryToProcess, props);
	}
}
