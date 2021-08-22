package org.mwage.mcPlugin.main;
import java.util.ArrayList;
import java.util.List;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
import org.mwage.mcPlugin.main.util.methods.LogicUtil;
import org.mwage.mcPlugin.main.util.methods.ServerUtil;
import org.mwage.mcPlugin.main.util.methods.StringUtil;
/*
 * This interface contains methods relate to server generally,
 * like broadcast messages.
 * Also contains other tool methods, like those related to String.
 */
/**
 * 奶路主插件的零散功能。
 * <p>
 * 这是个便于调用的集成接口。
 * 实际功能在{@link org.mwage.mcPlugin.main.util.methods}包中。
 * 
 * @author GHYNG
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0, currentlyAt = 1))
public interface Main_GeneralMethods extends StringUtil, LogicUtil, ServerUtil {
	/**
	 * 返回一个列表的实例{@link ArrayList}，
	 * 让调用类不需要自己调用{@code ArrayList}。
	 * 
	 * @param <T>
	 *            列表的元素类型。
	 * @return 新的列表实例。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	default <T> List<T> getNewList() {
		List<T> list = new ArrayList<T>();
		return list;
	}
	/**
	 * 将数组转化为列表。
	 * 
	 * @param <T>
	 *            元素的类型。
	 * @param array
	 *            需要的数组。
	 * @return 产生的列表。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	default <T> List<T> arrayToList(T[] array) {
		List<T> list = getNewList();
		for(T t : array) {
			list.add(t);
		}
		return list;
	}
}