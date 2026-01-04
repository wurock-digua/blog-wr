package cn.wurock.blog.web.config;

import cn.wurock.blog.config.properties.TimeFormatProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
	
	private final TimeFormatProperties timeFormatProperties;
	
	/**
	 * 添加时间格式化转换器
	 * @param registry
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timeFormatProperties.getDateTimePattern());
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(timeFormatProperties.getDatePattern());
		
		registry.addFormatterForFieldType(LocalDateTime.class, new Formatter<LocalDateTime>() {
			@Override
			public LocalDateTime parse(String text, Locale locale) throws ParseException {
				return LocalDateTime.parse(text, dateTimeFormatter);
			}
			
			@Override
			public String print(LocalDateTime object, Locale locale) {
				return object.format(dateTimeFormatter);
			}
		});
		
		registry.addFormatterForFieldType(LocalDate.class, new Formatter<LocalDate>() {
			@Override
			public LocalDate parse(String text, Locale locale) throws ParseException {
				return LocalDate.parse(text, dateFormatter);
			}
			
			@Override
			public String print(LocalDate object, Locale locale) {
				return object.format(dateFormatter);
			}
		});
	}
}
