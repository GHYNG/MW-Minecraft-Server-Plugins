package org.mwage.mcPlugin.main.util.clazz;
public class ClassDoesNotInheritException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ClassDoesNotInheritException(String message) {
		super(message);
	}
}