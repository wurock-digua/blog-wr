package cn.wurock.blog.article.dto;

import cn.wurock.blog.common.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCategoryDTO {
	
	@NotNull(message = "分类ID不能为空", groups = UpdateGroup.class)
	private Long id;
	@NotBlank
	private String categoryName;
	@NotBlank
	private String categoryAlias;
	private Long createUser;
}
