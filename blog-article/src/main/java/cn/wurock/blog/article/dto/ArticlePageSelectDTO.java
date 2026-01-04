package cn.wurock.blog.article.dto;

import cn.wurock.blog.article.anno.PublishStateCheck;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePageSelectDTO {
	@NotNull
	private Integer pageNum; // 当前页码
	@NotNull
	private Integer pageSize; // 每页条数
	private Long categoryId; // 分类ID
	private Long createUser; // 创建者ID
	@PublishStateCheck
	private String state; // 发布状态
}
