package org.mwage.mcPlugin.main.util.io.config;
import java.util.Set;
public interface ExpressiveIntValue<A> extends MetaExpressiveValue<Integer, A> {
	default public ExpressiveIntValue<A> add(ExpressiveIntValue<A> another) {
		if(this instanceof NullExpressiveIntValue || another instanceof NullExpressiveIntValue || another == null) {
		}
		return null; // TODO unfinished
	}
}
class ExpressiveIntValueInstance<A> implements ExpressiveIntValue<A> {
	@Override
	public MetaExpressiveValue<Integer, A> getValue(Integer expressiveInstance) {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public Integer parseExpressiveInstanceFromString(String content) {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public MetaExpressiveValue<Integer, A> parseValueFromString(String content) {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public String toContent() {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public Set<String> getKeywords() {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public Integer getExpressiveInstance() {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public A getActualInstance() {
		// TODO 自动生成的方法存根
		return null;
	}
}