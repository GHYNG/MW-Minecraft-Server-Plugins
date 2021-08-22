package org.mwage.mcPlugin.main.standard.logger;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * 未能成功注册记录器。
 * 
 * @author GHYNG
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public class LoggerRegisterFailedException extends RuntimeException {
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1, openToSubPlugin = false))
	private static final long serialVersionUID = 1L;
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public LoggerRegisterFailedException(String message) {
		super(message);
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public LoggerRegisterFailedException() {
		super();
	}
}
