//package net.riking.spring;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import net.riking.core.web.interceptor.OAuthInterceptor;
//
//@Configuration
//public class OAuthConfig extends WebMvcConfigurerAdapter {
//
//	@Autowired
//	OAuthInterceptor oAuthInterceptor;
//
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(oAuthInterceptor).addPathPatterns("/**")// 要拦截的请求
//				.excludePathPatterns("/dict/*.json", "/accessToken", "/accessPath");// 不拦截的请求
//	}
//
//}
