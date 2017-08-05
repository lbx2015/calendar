package net.riking;

import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import net.riking.web.filter.StartupListener;

@ServletComponentScan
@SpringBootApplication
// public class Application {
public class Application extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {
		System.out.println("################ TL-API Start ##################");
		System.setProperty("spring.devtools.restart.enabled", "true");

		SpringApplication.run(Application.class, args);
	}

	// @Bean
	// public ServletRegistrationBean indexServletRegistration() {
	// ServletRegistrationBean registration = new ServletRegistrationBean(new
	// IndexServlet());
	// registration.addUrlMappings("/hello");
	// return registration;
	// }

	// @Bean
	// public FilterRegistrationBean indexFilterRegistration() {
	// return null;
	// }

	@Autowired
	StartupListener startupListener;

	@Bean
	public ServletListenerRegistrationBean<ServletContextListener> servletListenerRegistrationBean() {
		ServletListenerRegistrationBean<ServletContextListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<ServletContextListener>();
		servletListenerRegistrationBean.setListener(startupListener);
		return servletListenerRegistrationBean;
	}
}
