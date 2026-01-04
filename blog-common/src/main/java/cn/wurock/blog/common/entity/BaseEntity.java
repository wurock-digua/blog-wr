package cn.wurock.blog.common.entity;

import java.time.LocalDateTime;

/**
 * 自动设置创建时间和更新时间
 * @author wurock
 */
public interface BaseEntity {
	LocalDateTime getCreateTime();
	LocalDateTime getUpdateTime();
	void setCreateTime(LocalDateTime createTime);
	void setUpdateTime(LocalDateTime updateTime);
	
	/**
	 * 插入数据时调用
	 */
	default void prePersist() {
		if (this.getCreateTime() == null) {
			LocalDateTime now = LocalDateTime.now();
			this.setCreateTime(now);
			this.setUpdateTime(now);
		}
	}
	
	/**
	 * 更新数据时调用
	 */
	default void preUpdate() {
		this.setUpdateTime(LocalDateTime.now());
	}
}
