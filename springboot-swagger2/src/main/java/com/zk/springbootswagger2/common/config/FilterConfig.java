package com.zk.springbootswagger2.common.config;

import com.zk.springbootswagger2.common.filter.RequestLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean filterRegistration(RequestLogFilter requestLogFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(requestLogFilter);  //MyFilter类上使用了@Component注解，将自己交给Spring容器管理，可注入其他Spring bean
        registration.addUrlPatterns("/*");
        registration.setOrder(Integer.MIN_VALUE); //Filter的执行顺序，值越小越先执行
        return registration;
    }
}
