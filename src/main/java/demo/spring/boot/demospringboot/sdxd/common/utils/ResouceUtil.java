package demo.spring.boot.demospringboot.sdxd.common.utils;

import java.io.IOException;
import java.util.Properties;

public class ResouceUtil {
	private static Properties p = null;

	public ResouceUtil(String path) {
		if (p == null) {
			p = new Properties();
		}
		try {
			p.load(ResouceUtil.class.getClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据key得到value的值
	 */
	public static String getValue(String key) {
		return p.getProperty(key);
	}
}
