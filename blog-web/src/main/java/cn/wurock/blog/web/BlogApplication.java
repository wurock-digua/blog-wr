package cn.wurock.blog.web;

import cn.wurock.blog.config.properties.TimeFormatProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(TimeFormatProperties.class)
@MapperScan({"cn.wurock.blog.user.mapper", "cn.wurock.blog.article.mapper"})
@SpringBootApplication(scanBasePackages = {
		"cn.wurock.blog.common",
		"cn.wurock.blog.auth",
		"cn.wurock.blog.config",
		"cn.wurock.blog.user",
		"cn.wurock.blog.article",
		"cn.wurock.blog.web"
})
public class BlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
}
