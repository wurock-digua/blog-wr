package cn.wurock.blog.web.config;

import cn.wurock.blog.config.properties.TimeFormatProperties;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {
	private final TimeFormatProperties timeFormatProperties;
	
	public JacksonConfig(TimeFormatProperties timeFormatProperties) {
		this.timeFormatProperties = timeFormatProperties;
	}
	
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
		return builder -> {
			// LocalDateTime 格式化
			builder.serializerByType(
					LocalDateTime.class,
					new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(timeFormatProperties.getDateTimePattern()))
			);
			// LocalDate 格式化
			builder.serializerByType(
					LocalDate.class,
					new LocalDateSerializer(DateTimeFormatter.ofPattern(timeFormatProperties.getDatePattern()))
			);
		};
	}
}
