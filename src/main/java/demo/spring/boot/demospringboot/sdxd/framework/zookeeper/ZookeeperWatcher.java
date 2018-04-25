package demo.spring.boot.demospringboot.sdxd.framework.zookeeper;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZooKeeper节点数据监听
 *
 * @author wangzhaolin
 * 
 * @since 2017年12月27日
 * 
 * @version 1.0
 */
public class ZookeeperWatcher implements CuratorWatcher {
	
	/**
	 * 日志
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ZookeeperWatcher.class);
	
	@Override
	public void process(WatchedEvent event) throws Exception {
		
		LOG.info("-------------Curator ZooKeeper Watcher-------------");
		LOG.info("Watched event：{}", event);
	}

	
}
