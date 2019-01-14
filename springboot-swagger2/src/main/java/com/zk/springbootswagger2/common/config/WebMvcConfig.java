package com.zk.springbootswagger2.common.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

  /**
   * DispatchServlet 添加API 请求路径.
   *
   * @param dispatcherServlet .
   */
  @Bean
  public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
    dispatcherServlet.setDispatchOptionsRequest(true);
    ServletRegistrationBean reg = new ServletRegistrationBean(dispatcherServlet);
    reg.getUrlMappings().clear();
    reg.addUrlMappings("/api/*");
    return reg;
  }


  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    //增加swagger相关访问地址
    registry
        .addResourceHandler("/**/swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}
