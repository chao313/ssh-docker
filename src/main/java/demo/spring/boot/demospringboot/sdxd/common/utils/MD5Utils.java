/**
 * 营运系统
 * com.ane56.aneos.utils
 * MD5Utils.java
 * 1.0
 * 2014年10月28日-下午7:51:53
 *  2014安能物流-版权所有
 *
 */
package demo.spring.boot.demospringboot.sdxd.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

/**
 *
 * 类名称：MD5Utils 
 * 类描述： 关于MD5的加密算法实现，是不是实现的不一样，结果还不一样啊，以前遇到过
 * 创建人：qiuyangjun 
 * 修改人：qiuyangjun 
 * 修改时间：2014年10月28日 下午7:51:53 
 * 修改备注：
 * 
 * @version 1.0.0
 *
 */

public class MD5Utils {

	// 十六进制下数字到字符的映射数组
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 
	 * generatePassword：获取加密后的字符串
	 * (这里描述这个方法适用条件 – 可选)
	 * 创建人：qiuyangjun
	 * 修改人：qiuyangjun
	 * @param inputString
	 * @return String
	 * @exception
	 * @since  1.0.0
	 */
	public static String generatePassword(String inputString) {
		return encodeByMD5(inputString);
	}

	/**
	 * 
	 * validatePassword：验证输入密码是否正确
	 * (这里描述这个方法适用条件 – 可选)
	 * 创建人：qiuyangjun
	 * 修改人：qiuyangjun
	 * @param password
	 * @param inputString
	 * @return boolean
	 * @exception
	 * @since  1.0.0
	 */
	public static boolean validatePassword(String password, String inputString) {
		if (StringUtils.equals(password.trim(),encodeByMD5(inputString).trim() )) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * encodeByMD5：对字符串进行MD5加密
	 * (这里描述这个方法适用条件 – 可选)
	 * 创建人：qiuyangjun
	 * 修改人：qiuyangjun
	 * @param originString
	 * @return String
	 * @exception
	 * @since  1.0.0
	 */
	public static String encodeByMD5(String originString) {
		if (originString != null) {
			try {
				// 创建具有指定算法名称的信息摘要
				MessageDigest md = MessageDigest.getInstance("MD5");
				// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
				byte[] results = md.digest(originString.getBytes());
				// 将得到的字节数组变成字符串返回
				String resultString = byteArrayToHexString(results);
				return resultString.toUpperCase();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 带字符集的MD5签名加密
	 * @param originString
	 * @param charset
	 * @return
	 */
	public static String encodeByMD5(String originString, String charset) {
		if (originString != null) {
			try {
				// 创建具有指定算法名称的信息摘要
				MessageDigest md = MessageDigest.getInstance("MD5");
				// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
				byte[] results = md.digest(originString.getBytes(charset));
				// 将得到的字节数组变成字符串返回
				String resultString = byteArrayToHexString(results);
				return resultString.toUpperCase();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}


	/**
	 * 
	 * byteArrayToHexString：转换字节数组为十六进制字符串
	 * (这里描述这个方法适用条件 – 可选)
	 * 创建人：qiuyangjun
	 * 修改人：qiuyangjun
	 * @param b
	 * @return String
	 * @exception
	 * @since  1.0.0
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 
	 * byteToHexString：将一个字节转化成十六进制形式的字符串
	 * (这里描述这个方法适用条件 – 可选)
	 * 创建人：qiuyangjun
	 * 修改人：qiuyangjun
	 * @param b
	 * @return String
	 * @exception
	 * @since  1.0.0
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

}
