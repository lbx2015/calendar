package net.riking.spring;

import net.riking.core.web.messageConverter.XDomainMessageConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
@EnableAsync
public class AppConfiguration extends WebMvcConfigurerAdapter {

	@Value("${endpoints.cors.allowed-origins}")
	private String allowedOrigins;
	@Value("${endpoints.cors.allowed-methods}")
	private String allowedMethods;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins(StringUtils.split(allowedOrigins, ","))
						.allowedMethods(StringUtils.split(allowedMethods, ","));
			}
		};
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		converters.add(new XDomainMessageConverter());
	}
}
