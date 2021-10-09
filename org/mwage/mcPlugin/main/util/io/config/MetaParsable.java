package org.mwage.mcPlugin.main.util.io.config;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 可直接解析注解。
 * 被注解的值应该是可以被直接解析的基础数据类型。
 * 
 * @author GHYNG
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MetaParsable {
}