package cn.wurock.blog.common.result;

import cn.wurock.blog.common.constant.ResultCodeConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static cn.wurock.blog.common.constant.ResultCodeConstant.SUCCESS;

/**
 * 统一返回结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
	private static final long serialVersionUID = 1L;
	private Integer code; // 状态码，如 200 表示成功.
	private String msg;   // 提示信息
	private T data;       // 具体的数据内容
	
	// 成功响应，带数据
	public static <T> Result<T> success(T data) {
		return new Result<T>(ResultCodeConstant.SUCCESS, "操作成功", data);
	}
	
	// 成功响应，无数据
	public static <T> Result<T> success() {
		return new Result<T>(ResultCodeConstant.SUCCESS, "操作成功", null);
	}
	
	// 失败响应通用方法，带错误信息
	public static <T> Result<T> error(Integer code, String msg) {
		return new Result<T>(code, msg, null);
	}
	
}
