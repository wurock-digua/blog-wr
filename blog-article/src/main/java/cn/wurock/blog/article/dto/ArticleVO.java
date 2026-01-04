package cn.wurock.blog.article.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ArticleVO {
	private Long id;
	private String title;
	private String content;
	private String coverImg;
	private String state;
	private Long categoryId;
	private Long createUser;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
