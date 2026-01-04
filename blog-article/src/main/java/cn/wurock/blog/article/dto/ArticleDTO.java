package cn.wurock.blog.article.dto;

import cn.wurock.blog.article.anno.PublishStateCheck;
import cn.wurock.blog.common.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
	@NotNull(message = "id不能为空", groups = UpdateGroup.class)
	private Long id;
	@Pattern(regexp = "^\\S{1,10}$", message = "标题不允许空格，长度在1-10个字符之间")
	private String title;
	@NotBlank
	private String content;
	@URL
	@NotBlank
	private String coverImg;
	@PublishStateCheck
	@NotBlank
	private String state;
	@NotNull
	private Long categoryId;
	
	private Long createUser;
}
