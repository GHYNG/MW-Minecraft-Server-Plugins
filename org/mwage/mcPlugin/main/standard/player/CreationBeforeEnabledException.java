package org.mwage.mcPlugin.main.standard.player;

import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;

/**
 * 当PlayerSettings类在所属插件enabled之前就被创建，
 * 就会产生此错误。
 * <p>
 * PlayerSettings类的实例必须在插件enabled之后产生，
 * 否则无法正常运行。
 * 
 * @author GHYNG
 */
@SuppressWarnings("serial")
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
public class CreationBeforeEnabledException extends RuntimeException {
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	public CreationBeforeEnabledException(String message) {
		super(message);
	}
}