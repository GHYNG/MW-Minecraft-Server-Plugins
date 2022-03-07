package org.mwage.mcPlugin.main.util;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * 循环树异常。
 * 当尝试将上级树植入下级树时抛出。
 * <p>
 * 该异常通常在尝试在有上下级节点的数据结构（类型）中注册错误节点时抛出。
 * 确切的说，
 * 考虑节点A和B，且A是B的直接或间接上级节点，或A为B本身时，
 * 再将A尝试注册为B的子节点时，
 * 就会抛出此异常。
 * <p>
 * 这个异常的目的是为了防止程序在穷举节点树时出现死循环。
 * 
 * @author GHYNG
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public class CircularRegistrationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public CircularRegistrationException() {
		super();
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public CircularRegistrationException(String message) {
		super(message);
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public CircularRegistrationException(String message, Throwable cause) {
		super(message, cause);
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected CircularRegistrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public CircularRegistrationException(Throwable cause) {
		super(cause);
	}
}
