package cn.wurock.blog.user.dto;

import cn.wurock.blog.user.anno.PasswordCheck;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@PasswordCheck
public class PasswordCheckDTO {
	@NotBlank(message = "原密码不能为空")
	private String oldPassword;
	@Pattern(regexp = "^\\S{5,16}$", message = "新密码不允许空格，长度在5-16个字符之间")
	private String newPassword;
	private String confirmPassword;
}
