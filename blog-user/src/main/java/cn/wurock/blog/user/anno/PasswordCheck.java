package cn.wurock.blog.user.anno;

import cn.wurock.blog.user.validation.PasswordCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordCheckValidator.class)
public @interface PasswordCheck {
	String message() default "密码校验失败：新密码不能与旧密码相同，且确认密码必须一致";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
