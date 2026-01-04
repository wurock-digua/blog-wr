package cn.wurock.blog.config.conf;

import cn.wurock.blog.config.properties.AliyunOssProperties;
import cn.wurock.blog.config.properties.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AliyunOssProperties.class, JwtProperties.class})
public class PropertiesConfig {

}
