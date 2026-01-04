package cn.wurock.blog.web.controller;

import cn.wurock.blog.common.result.Result;
import cn.wurock.blog.config.service.AliyunOssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static cn.wurock.blog.common.constant.ResultCodeConstant.FILE_ERROR;

@Slf4j
@RestController
@RequestMapping("/api")
public class FileController {
	
	@Autowired
	private AliyunOssService ossService;
	
	private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
			".png", ".jpg", ".jpeg", ".gif", ".bmp", ".pdf", ".docx", ".xlsx"
	);
	private static final long MAX_SIZE = 10 * 1024 * 1024; // 10MB
	
	@PostMapping("/upload")
	public Result<String> uploadFile(@RequestPart("file") MultipartFile file) {
		// 1. 校验文件是否存在
		if (file == null || file.isEmpty()) {
			return Result.error(FILE_ERROR,"文件不能为空");
		}
		
		// 2. 获取原始文件名并校验
		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null || originalFilename.trim().isEmpty()) {
			return Result.error(FILE_ERROR,"文件名无效");
		}
		
		// 3. 文件大小校验
		if (file.getSize() > MAX_SIZE) {
			return Result.error(FILE_ERROR,"文件大小不能超过10MB");
		}
		
		// 4. 文件类型校验（基于扩展名）
		String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
		if (!ALLOWED_EXTENSIONS.contains(extension)) {
			return Result.error(FILE_ERROR,"不支持的文件类型");
		}
		
		try (InputStream inputStream = file.getInputStream()) {
			// 生成唯一文件名，按日期分目录
			String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "/";
			String fileName = dateDir + UUID.randomUUID().toString().replaceAll("-", "") + extension;
			
			// 上传到阿里云 OSS
			String url = ossService.upload(fileName, inputStream);
			return Result.success(url);
			
		} catch (IOException e) {
			log.error("文件读取失败", e);
			return Result.error(FILE_ERROR,"文件读取失败");
		} catch (Exception e) {
			log.error("OSS上传异常", e);
			return Result.error(FILE_ERROR,"上传失败");
		}
	}
}
