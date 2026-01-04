package cn.wurock.blog.common.result;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页结果
 */
@Data
@Builder
public class PageResult<T> implements Serializable {
	private long total; // 总记录数
	private List<T> items; // 当前页数据
}
