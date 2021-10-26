package org.mwage.mcPlugin.main.util.clazz;
import java.lang.annotation.*;
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenericTypeHeaders {
	GenericTypeHeader[] value();
}