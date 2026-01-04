package cn.wurock.blog.article.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ArticleCategoryVO {
	
	private Long id;
	private String categoryName;
	private String categoryAlias;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
