package org.mwage.mcPlugin.main.util;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * 循环树异常。
 * 当尝试将上级树植入下级树时抛出
 * 
 * @author GHYNG
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public class DeadLoopTreeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public DeadLoopTreeException() {
		super();
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public DeadLoopTreeException(String message) {
		super(message);
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public DeadLoopTreeException(String message, Throwable cause) {
		super(message, cause);
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected DeadLoopTreeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public DeadLoopTreeException(Throwable cause) {
		super(cause);
	}
}
