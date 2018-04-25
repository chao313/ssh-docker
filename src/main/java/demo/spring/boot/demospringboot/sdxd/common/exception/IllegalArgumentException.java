package demo.spring.boot.demospringboot.sdxd.common.exception;

/**
 * 表示参数不正确。
 * 
 * <p style="display:none">modifyRecord</p>
 * @author qiuyangjun
 * @date 2013年12月24日 下午3:12:27
 * @since
 * @version
 */
public class IllegalArgumentException extends BaseException {

	private static final long serialVersionUID = -1398555596273694035L;

	public IllegalArgumentException() {
		super("illegalArgument");
	}

	public IllegalArgumentException(String messageKey, Throwable cause) {
		super(messageKey, cause);
	}

	public IllegalArgumentException(String messageKey) {
		super(messageKey);
	}

	public IllegalArgumentException(Throwable cause) {
		super("illegalArgument", cause);
	}

}
