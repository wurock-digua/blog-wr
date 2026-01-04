package cn.wurock.blog.user.dto;

import cn.wurock.blog.common.validation.AddGroup;
import cn.wurock.blog.common.validation.UpdateGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	@NotNull(message = "用户ID不能为空", groups = UpdateGroup.class)
	private Long id;
	@Pattern(regexp = "^\\S{5,16}$", message = "用户名不允许空格，长度在5-16个字符之间")
	private String username;
	@Pattern(regexp = "^\\S{5,16}$", message = "密码不允许空格，长度在5-16个字符之间", groups = AddGroup.class)
	private String password;
	@Pattern(regexp = "^\\S{1,10}$", message = "昵称不允许空格，长度在1-10个字符之间")
	private String nickname;
	@Email(message = "邮箱格式错误")
	@NotBlank
	private String email;
}
