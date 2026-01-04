package cn.wurock.blog.user.entity;

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
public class User implements BaseEntity, Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private String password;
	private String nickname;
	private String email;
	private String userPic;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
