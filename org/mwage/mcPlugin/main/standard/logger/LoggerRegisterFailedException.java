package org.mwage.mcPlugin.main.standard.logger;
/**
 * 未能成功注册记录器。
 * 
 * @author GHYNG
 */
public class LoggerRegisterFailedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public LoggerRegisterFailedException(String message) {
		super(message);
	}
	public LoggerRegisterFailedException() {
		super();
	}
}
