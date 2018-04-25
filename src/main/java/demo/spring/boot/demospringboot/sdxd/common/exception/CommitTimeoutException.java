package demo.spring.boot.demospringboot.sdxd.common.exception;

/**
 * 表示提交超时。
 * <p style="display:none">modifyRecord</p>
 * @author qiuyangjun
 * @date 2013年12月30日 上午11:50:00
 * @since
 * @version
 */
public class CommitTimeoutException extends BaseException {

	private static final long serialVersionUID = 1669843560642800254L;

	public CommitTimeoutException() {
		super("commitTimeOut");
	}

	public CommitTimeoutException(String messageKey, Throwable cause) {
		super(messageKey, cause);
	}

	public CommitTimeoutException(String messageKey) {
		super(messageKey);
	}

	public CommitTimeoutException(Throwable cause) {
		super("dataCommitFailed", cause);
	}

}
