package demo.spring.boot.demospringboot.sdxd.common.exception;

/**
 * 表示数据不一致。
 * 
 * <p style="display:none">modifyRecord</p>
 * @author qiuyangjun
 * @date 2013年12月24日 下午3:12:27
 * @since
 * @version
 */
public class InconsistentException extends BaseException {

	private static final long serialVersionUID = 1669843560642800254L;

	public InconsistentException() {
		super("inconsistent");
	}

	public InconsistentException(String messageKey, Throwable cause) {
		super(messageKey, cause);
	}

	public InconsistentException(String messageKey) {
		super(messageKey);
	}

	public InconsistentException(Throwable cause) {
		super("inconsistent", cause);
	}

}
