package cn.wurock.blog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "time-format")
public class TimeFormatProperties {
	private String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
	private String datePattern = "yyyy-MM-dd";
}
