package org.mwage.mcPlugin.main.util.io.config;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 可直接使用的类型。
 * 用Java程序比喻的话，
 * 被该注解注解的Value类型是非抽象的。
 * 
 * @author GHYNG
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DirectlyUsable {
}