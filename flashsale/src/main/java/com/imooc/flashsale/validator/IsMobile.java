package com.imooc.flashsale.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 自定义校验器：判定手机格式是否正确
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { IsMobileValidator.class })
public @interface IsMobile {

	// 默认值_false，用于接收注解上自定义的 required
	boolean required() default true;

	// 如果校验不通过返回的提示信息
	String message() default "手机号码格式错误";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}