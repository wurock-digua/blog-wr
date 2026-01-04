package cn.wurock.blog.common.exception;

public class OssException extends BaseException {
	public OssException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public OssException(String msg) {
		super(msg);
	}
}
