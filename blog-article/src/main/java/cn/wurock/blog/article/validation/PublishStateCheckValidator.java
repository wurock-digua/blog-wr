package cn.wurock.blog.article.validation;

import cn.wurock.blog.article.anno.PublishStateCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

/**
 * 发布状态校验器
 */
public class PublishStateCheckValidator implements ConstraintValidator<PublishStateCheck, String> {
	
	// 定义允许的状态值
	private static final List<String> VALID_STATES = Arrays.asList("已发布", "草稿");
	
	// 初始化
	@Override
	public void initialize(PublishStateCheck constraintAnnotation) {
	}
	
	// 校验规则
	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		// 检查是否在允许的值中
		return VALID_STATES.contains(value);
	}
}
