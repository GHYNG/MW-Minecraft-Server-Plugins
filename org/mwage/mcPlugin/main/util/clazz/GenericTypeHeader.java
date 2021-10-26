package org.mwage.mcPlugin.main.util.clazz;
import java.lang.annotation.Target;
import java.lang.annotation.*;
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(GenericTypeHeaders.class)
@Documented
public @interface GenericTypeHeader {
	Class<?> superClass();
	String typeParamaterName();
	Class<?> typeParamater();
}