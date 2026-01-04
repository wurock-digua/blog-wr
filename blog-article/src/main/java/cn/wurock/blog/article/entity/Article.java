package cn.wurock.blog.article.entity;

import cn.wurock.blog.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article implements BaseEntity, Serializable {
	private static final long serialVersionUID = 1L;
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
