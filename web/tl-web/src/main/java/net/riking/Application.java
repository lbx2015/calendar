package net.riking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import net.riking.core.web.filter.HtmlFilter;

@ServletComponentScan
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {
		System.out.println("#################  Start TL-WEB   ######################");
		System.setProperty("spring.devtools.restart.enabled", "true");
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	HtmlFilter htmlFilter;

	@Bean
	public FilterRegistrationBean registFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();

		htmlFilter.setHtmlCoreDeps("/common/core/depjs.html", "/common/core/depcss.html");
		registration.setFilter(htmlFilter);
		registration.addUrlPatterns("*.html");
		registration.setOrder(2);
		return registration;
	}
}
