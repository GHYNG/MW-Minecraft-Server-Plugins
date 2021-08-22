package org.mwage.mcPlugin.main.util;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * Main util methods.
 * 
 * @author GHYNG
 * @deprecated Functions of this class should be moved elsewhere.
 */
@Deprecated
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0, openToSubPlugin = false))
public interface UtilCollection {
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0, openToSubPlugin = false))
	public default <T> Set<T> copySet(Set<T> set) {
		Set<T> result = new HashSet<T>();
		result.addAll(set);
		return result;
	}
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0, openToSubPlugin = false))
	public default <T> List<T> copyList(List<T> list) {
		List<T> result = new ArrayList<T>();
		result.addAll(list);
		return result;
	}
}