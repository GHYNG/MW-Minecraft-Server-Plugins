package org.mwage.mcPlugin.main.standard.player;
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
public class CreationBeforeEnabledException extends RuntimeException {
	public CreationBeforeEnabledException(String message) {
		super(message);
	}
}