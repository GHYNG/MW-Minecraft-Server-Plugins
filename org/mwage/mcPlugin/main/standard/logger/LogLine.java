package org.mwage.mcPlugin.main.standard.logger;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * 这个类的每一个对象表示在log文件中的每一行。
 * 每一行记录都由两个部分组成：时间和内容。
 * 
 * @author GHYNG
 * @param <C>
 *            记录的内容的类型。
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public class LogLine<C> implements Comparable<LogLine<C>> {
	/**
	 * 默认使用的时间格式。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	/**
	 * 本行记录的时间。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public final Date DATE;
	/**
	 * 本行记录的内容。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public final C CONTENT;
	/**
	 * 根据给定的日期和内容，新建一条记录。
	 * 
	 * @param date
	 *            给定的日期。
	 * @param content
	 *            给定的内容。
	 * @deprecated 不应该在旧的记录中再插入记录。
	 */
	@Deprecated
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public LogLine(Date date, C content) {
		DATE = date;
		CONTENT = content;
	}
	/**
	 * 给予当前时间，新建一条记录。
	 * 
	 * @param content
	 *            给定的内容。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public LogLine(C content) {
		DATE = new Date();
		CONTENT = content;
	}
	/**
	 * 返回本行记录的内容。
	 * <p>
	 * 默认情况下是{@link CONTENT}的{@link Object#toString()}方法的内容。
	 * <p>
	 * 注意：极其不推荐在这个字符串中包含换行符。
	 * 
	 * @return 本行记录的内容。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public String parseContent() {
		return CONTENT.toString();
	}
	/**
	 * 返回这一行记录的文字形式。
	 * <p>
	 * 本行记录应当以时间开始，
	 * 以{@code hh:mm:ss}的格式，
	 * 然后接上“{@code : }”（冒号与空格），
	 * 最后是内容{@link #parseContent()}。
	 * 这个方法实际上等于：
	 * <p>
	 * {@code new SimpleDateFormat("HH:mm:ss").format(DATE) + ": " + parseContent();}
	 * <p>
	 * 除非使用了另外的时间格式。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public final String toString() {
		return timeFormatter.format(DATE) + ": " + parseContent();
	}
	/**
	 * 比较大小，
	 * 使用{@link java.util.Date#compareTo(Date)}方法。
	 * 
	 * @see java.util.Date
	 */
	@Override
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public int compareTo(LogLine<C> o) {
		return DATE.compareTo(o.DATE);
	}
}