package com.imooc.flashsale.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.imooc.flashsale.util.ValidatorUtil;

public class IsMobileValidator implements ConstraintValidator<IsMobile, Long> {

	private boolean required = false;

	public void initialize(IsMobile constraintAnnotation) {
		// 获得注解
		required = constraintAnnotation.required();
	}

	public boolean isValid(Long value, ConstraintValidatorContext context) {
		if (required) {
			return ValidatorUtil.isMobile(value);
		} else {
			if (StringUtils.isEmpty("" + value)) {
				return true;
			} else {
				return ValidatorUtil.isMobile(value);
			}
		}
	}

}
