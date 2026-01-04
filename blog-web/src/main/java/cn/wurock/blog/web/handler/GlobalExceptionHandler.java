package cn.wurock.blog.web.handler;

import cn.wurock.blog.common.constant.ResultCodeConstant;
import cn.wurock.blog.common.exception.BaseException;
import cn.wurock.blog.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 * 处理业务异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(BaseException.class)
	public Result<?> handleBaseException(BaseException ex) {
		return Result.error(ResultCodeConstant.FAIL, ex.getMessage());
	}
	
	/**
	 * 处理其他异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public Result<?> handleException(Exception ex) {
		return Result.error(ResultCodeConstant.FAIL, "操作失败");
	}
}
