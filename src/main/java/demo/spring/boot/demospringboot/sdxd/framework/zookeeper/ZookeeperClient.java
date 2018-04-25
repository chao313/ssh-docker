package demo.spring.boot.demospringboot.sdxd.framework.zookeeper;



import org.apache.commons.codec.Charsets;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import demo.spring.boot.demospringboot.sdxd.framework.constant.SysConstants;

/**
 * ZooKeeper客户端
 *
 * @author wangzhaolin
 * 
 * @since 2017年12月26日
 * 
 * @version 1.0
 */
public class ZookeeperClient {
	
	/**
	 * 日志
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ZookeeperClient.class);
	
	/**
	 * 连接超时时间，单位：毫秒
	 */
	private static final int CONNECTION_TIMEOUT = 10000;
	
	/**
	 * 重试次数
	 */
	private static final int RETRY_TIMES = 5;
	
	/**
	 * 重试间隔时间，单位：毫秒
	 */
	private static final int RETRY_INTERVAL = 1000;

	/**
	 * CuratorFramework实例
	 */
	private static final Map<String, CuratorFramework> curators = new ConcurrentHashMap<>();
	
	/**
	 * 静态内部类
	 */
	private static class ZooKeeperClientHolder {
		private static final ZookeeperClient INSTANCE = new ZookeeperClient();
	}
	
	/**
	 * 获取ZK客户端实例
	 * 
	 * @param serverAddress ZK服务器地址
	 * @return ZK客户端实例
	 */
	public static final ZookeeperClient getInstance() {
		
		return ZooKeeperClientHolder.INSTANCE;
	}
	
	/**
	 * 私有构造方法
	 */
	private ZookeeperClient() {
	}
	
	/**
	 * 获取Curator实例
	 * 
	 * @param serverAddress ZooKeeper地址
	 * @return
	 */
	public CuratorFramework getCurator(String serverAddress) {

		// Curator实例
		CuratorFramework curator = curators.get(serverAddress);

		if (curator == null) {
			curator = CuratorFrameworkFactory.builder().connectString(serverAddress)
					.retryPolicy(new RetryNTimes(RETRY_TIMES, RETRY_INTERVAL)).connectionTimeoutMs(CONNECTION_TIMEOUT)
					.build();
			curators.put(serverAddress, curator);

			// ZooKeeper状态监听
			curator.getConnectionStateListenable().addListener(new ConnectionStateListener() {

				@Override
				public void stateChanged(CuratorFramework arg0, ConnectionState state) {
					if (state == ConnectionState.LOST) {
						if (LOG.isWarnEnabled()) {
							LOG.warn("ZooKeeper连接丢失.");
						}
					} else if (state == ConnectionState.CONNECTED) {
						if (LOG.isInfoEnabled()) {
							LOG.info("ZooKeeper建立连接.");
						}
					} else if (state == ConnectionState.RECONNECTED) {
						if (LOG.isInfoEnabled()) {
							LOG.info("ZooKeeper连接重连.");
						}
					}
				}
			});

			curator.start();
		}

		return curator;
	}
	
	/**
	 * 读取ZK节点数据
	 * 
	 * @param zkPath ZK节点路径
	 * @return
	 * @throws Exception 
	 */
	public Map<String, String> readData(CuratorFramework curator, String zkPath) throws Exception {
		
		Map<String, String> zkData = new HashMap<String, String>();
		
		/*
		 * 检查ZooKeeper节点是否存在
		 */
		if (curator.checkExists().forPath(zkPath) == null) {
			if (LOG.isErrorEnabled()) {
				LOG.warn("ZooKeeper节点：{}，不存在", zkPath);
			}
			return zkData;
		}
		
		// 获取子节点
		List<String> children = curator.getChildren().usingWatcher(new ZookeeperWatcher()).forPath(zkPath);
		if (CollectionUtils.isEmpty(children)) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("ZooKeeper节点：{}，无子节点", zkPath);
			}
			return zkData;
		}
		
		/*
		 * 遍历子节点，读取子节点数据
		 */
		String childPath = null; // 子节点名称
		byte[] data = null; // 子节点数据
		String dataStr = null;
		for (String child : children) {
			
			childPath = zkPath.concat("/").concat(child);
			data = curator.getData().usingWatcher(new ZookeeperWatcher()).forPath(childPath);
			dataStr = new String(data, Charsets.UTF_8);
			
			if (!StringUtils.isEmpty(dataStr)) {
				zkData.put(child, dataStr);
			}
		}
		
		return zkData;
	}
	
	/**
	 * 同步数据到ZK节点：节点不存在则新增，节点存在则更新
	 * 
	 * @param zkPath 同步的ZK节点路径
	 * @param data 同步的数据
	 * @throws Exception
	 */
	public void syncData(CuratorFramework curator, Map<String, String> data) throws Exception {
		
		// 先删除properties根目录所有节点配置
		curator.delete().deletingChildrenIfNeeded().forPath("/".concat(SysConstants.ZK_PROPERTIES_NODE_NAME));
		
		/*
		 * 将同步的数据写入ZooKeeper
		 */
		for (Map.Entry<String, String> node : data.entrySet()) {
			
			// 保存节点数据
			curator.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(node.getKey(), node.getValue().getBytes(Charsets.UTF_8));
		}
	}
	
	/**
	 * 向ZK节点写数据
	 * 
	 * @param zkPath  ZK节点路径
	 * @param data 写入的数据
	 * @return 写入的路径
	 * @throws Exception
	 */
	public String writeData(CuratorFramework curator, String zkPath, String data) throws Exception {
		
		// 写入节点数据
		String writePath = null;
		if (curator.checkExists().forPath(zkPath) == null) {
			writePath = curator.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zkPath, data.getBytes(Charsets.UTF_8));
			
			if (LOG.isInfoEnabled()) {
				LOG.info("ZooKeeper节点：{} 数据保存成功", zkPath);
			}
		} else {
			if (LOG.isInfoEnabled()) {
				LOG.info("ZooKeeper节点：{}已存在，不能重复写入", zkPath);
			}
		}
		
		return writePath;
	}
	
	/**
	 * 更新ZK节点数据
	 * 
	 * @param zkPath ZK节点路径
	 * @param data 更新的数据
	 * @throws Exception
	 */
	public void updateData(CuratorFramework curator, String zkPath, String data) throws Exception {
		
		// 修改节点数据
		curator.setData().forPath(zkPath, data.getBytes(Charsets.UTF_8));
		
		if (LOG.isInfoEnabled()) {
			LOG.info("ZooKeeper节点：{} 数据修改成功", zkPath);
		}
	}
	
	/**
	 * 删除ZK节点
	 * 
	 * @param zkPath ZK节点路径
	 * @throws Exception
	 */
	public void delete(CuratorFramework curator, String zkPath) throws Exception {
		
		/*
		 * 检查删除的节点是否存在
		 */
		if (curator.checkExists().forPath(zkPath) == null) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("删除的ZooKeeper节点不存在");
			}
			return;
		}
		
		// 删除节点
		curator.delete().inBackground().forPath(zkPath);
		
		if (LOG.isInfoEnabled()) {
			LOG.info("ZooKeeper节点：{} 数据删除成功", zkPath);
		}
	}
	
	/**
	 * 获取ZK数据节点路径
	 * 
	 * @param zkNodeNames 节点名称
	 * @return 节点路径
	 */
	public static String getZkDataPath(List<String> zkNodeNames) {
		
		StringBuilder zkDataPath = new StringBuilder();
		
		for (String nodeName : zkNodeNames) {
			zkDataPath.append("/").append(nodeName);
		}
		
		return zkDataPath.toString();
	}
}
