package cn.wurock.blog.article.anno;

import cn.wurock.blog.article.validation.PublishStateCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PublishStateCheckValidator.class)
public @interface PublishStateCheck {
	String message() default "发布状态必须是 '已发布' 或 '草稿'";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
