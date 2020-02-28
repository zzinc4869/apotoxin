package com.imooc.flashsale.access;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ METHOD, TYPE, ANNOTATION_TYPE })
// 此注解可用于方法、类或接口、以及注解类型上
public @interface AccessLimit {

	int seconds() default 5;

	int maxVisit() default 5;

	boolean needLogin() default true;
}
