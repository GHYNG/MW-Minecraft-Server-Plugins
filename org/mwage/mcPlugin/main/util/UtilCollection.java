package org.mwage.mcPlugin.main.util;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Main util methods.
 * 
 * @author GHYNG
 */
public interface UtilCollection {
	public default <T> Set<T> copySet(Set<T> set) {
		Set<T> result = new HashSet<T>();
		result.addAll(set);
		return result;
	}
	public default <T> List<T> copyList(List<T> list) {
		List<T> result = new ArrayList<T>();
		result.addAll(list);
		return result;
	}
}