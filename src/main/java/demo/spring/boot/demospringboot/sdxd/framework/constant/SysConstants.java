package demo.spring.boot.demospringboot.sdxd.framework.constant;

public abstract  class SysConstants {

    public static final String ROOT_ID = "0";

	public static final String NO = "0";

	public static final String USER_SESSION_KEY="userSessionKey";

	public static final String SESSION_MENUS = "menus";

	public static final String COOKIE_MENU_PARENTID = "menuParentId";

	public static final int PAGE_NO = 10;

	public static final String USER_ROLES_SESSION_KEY="userRolesSessionKey";
	public static final String USER_RESOURCES_SESSION_KEY="userResourcesSessionKey";
	public static final String USER_RESOURCES_FOR_PAGE="resources";
	public static final int COOKIES_MAX_AGE = -1; //-1为内存cookie（负数为内存cookie）关闭浏览器,就自动清除

	public static final String REQUEST_PARAM_LIST_SEPARATOR = ",";

	public static final String UPLOAD_NET_FILE_FOLDER = "/fileUpload/net/";
	public static final String UPLOAD_NET_FILE_SEPARATOR = ".";


	public static final String ROOT_URL_PREFIX = "/api";

	public static final String REDIS_KEY_TABLE_PRIMARY_ID  = "FRAMEWORK:TABLE:PRIMARY:${tableName}:${time}";
	
	/**
	 * 系统属性配置ZK地址
	 */
	public static final String ZK_ADDRESS_KEY = "zookeeper.address";
	/**
	 * 系统属性配置：应用名
	 */
	public static final String ZK_APP_NAME_KEY = "app.name";
	/**
	 * 系统属性配置：在ZooKeeper上的根目录
	 */
	public static final String ZK_PROPERTIES_NODE_NAME = "properties";
	/**
	 * 系统属性配置：通用配置目录
	 */
	public static final String ZK_PROPERTIES_NODE_ALL = "all";
	/**
	 * 系统属性配置：加密KEY
	 */
	public static final String DES_WRAPPER_KEY = "d9yeAoPsRlqdk6rAf8+A4fp9ZNpbwvpC";
}
