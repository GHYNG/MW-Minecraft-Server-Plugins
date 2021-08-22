package org.mwage.mcPlugin.main.util.methods;
import java.util.List;
import java.util.Set;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * 与字符串有关的工具。
 * 
 * @author GHYNG
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public interface StringUtil {
	/**
	 * 将任意多个字符串合并为同一个。
	 * 
	 * @param parts
	 *            多个字符串。
	 * @return 合并结果。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	default String line(Object... parts) {
		String line = "";
		for(Object part : parts) {
			line += part;
		}
		return line;
	}
	/**
	 * {@link #line(Object...)}
	 * 
	 * @param parts
	 *            多个字符串。
	 * @return 合并结果。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	default String lineList(List<Object> parts) {
		String line = "";
		for(Object part : parts) {
			line += part;
		}
		return line;
	}
	/**
	 * 将多行字符串合并为一个字符串，
	 * 并且用分行符分行。
	 * 
	 * @param lines
	 *            每一行。
	 * @return 合并后的字符串。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	default String page(Object... lines) {
		int length = lines.length;
		if(length == 0) {
			return "";
		}
		String page = "";
		for(int i = 0; i < length - 1; i++) {
			page += lines[i] + "\n";
		}
		page += lines[length - 1];
		return page;
	}
	/**
	 * {@link #page(Object...)}
	 * 
	 * @param lines
	 *            每一行。
	 * @return 合并后的字符串。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	default String pageList(List<Object> lines) {
		int length = lines.size();
		if(length == 0) {
			return "";
		}
		String page = "";
		for(int i = 0; i < length - 1; i++) {
			page += lines.get(i) + "\n";
		}
		page += lines.get(length - 1);
		return page;
	}
	/**
	 * 判断给定的字符串是不是良好的、符合Java规则的标识符。
	 * 注意，这些标识符可以是Java关键字。
	 * 
	 * @param identifier
	 *            给定的字符串。
	 * @return 是否是合法标识符。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	default boolean goodIdentifier(String identifier) {
		if(identifier == null || identifier.length() < 1) {
			return false;
		}
		int index = 0;
		if(Character.isJavaIdentifierStart(identifier.charAt(index))) {
			while(++index < identifier.length()) {
				if(!Character.isJavaIdentifierPart(identifier.charAt(index))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * 判断给定的字符串是不是良好的、符合Java规则的标识符。
	 * 注意，这些标识符可以是Java关键字。
	 * <p>
	 * 与前一个方法不同的是，
	 * 这个方法可以接受一个用户自选的关键字表（不是Java关键字表），
	 * 如果给定的字符串在关键字表内，
	 * 则同样不被判定为合法标识符。
	 * 
	 * @param identifier
	 *            给定的字符串。
	 * @param keywords
	 *            （可选）自定义关键字表。
	 * @return 是否是合法标识符。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	default boolean goodIdentifier(String identifier, Set<String> keywords) {
		if(identifier == null || identifier.length() < 1) {
			return false;
		}
		if(keywords == null) {
			return goodIdentifier(identifier);
		}
		if(keywords.contains(identifier)) {
			return false;
		}
		return goodIdentifier(identifier);
	}
}