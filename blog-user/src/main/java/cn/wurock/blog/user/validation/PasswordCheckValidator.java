package cn.wurock.blog.user.validation;

import cn.wurock.blog.user.anno.PasswordCheck;
import cn.wurock.blog.user.dto.PasswordCheckDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 密码校验器
 */
public class PasswordCheckValidator implements ConstraintValidator<PasswordCheck, PasswordCheckDTO> {
	
	// 初始化
	@Override
	public void initialize(PasswordCheck constraintAnnotation) {
	}
	
	// 校验规则
	@Override
	public boolean isValid(PasswordCheckDTO passwordCheckDTO, ConstraintValidatorContext context) {
		
		if (passwordCheckDTO == null) {
			return true; // null 值应由 @NotNull 处理
		}
		
		String oldPassword = passwordCheckDTO.getOldPassword();
		String newPassword = passwordCheckDTO.getNewPassword();
		String confirmPassword = passwordCheckDTO.getConfirmPassword();
		
		// 1. 新密码不能与旧密码相同
		if (newPassword.equals(oldPassword)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("新密码不能与原密码相同")
					.addPropertyNode("newPassword")
					.addConstraintViolation();
			return false;
		}
		
		// 2. 确认密码必须一致
		if (!newPassword.equals(confirmPassword)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("确认密码与新密码不一致")
					.addPropertyNode("confirmPassword")
					.addConstraintViolation();
			return false;
		}
		return true;
	}
}
