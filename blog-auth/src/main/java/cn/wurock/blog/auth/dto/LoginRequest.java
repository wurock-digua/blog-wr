package cn.wurock.blog.auth.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
	@Pattern(regexp = "^\\S{5,16}$", message = "用户名不允许空格，长度在5-16个字符之间")
	private String username;
	@Pattern(regexp = "^\\S{5,16}$", message = "密码不允许空格，长度在5-16个字符之间")
	private String password;
}
