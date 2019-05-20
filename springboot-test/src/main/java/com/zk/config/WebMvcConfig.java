package com.zk.config;

import com.zk.common.filter.RepeatableReadHttpServletRequestFilter;
import com.zk.common.filter.RequestLogFilter;
import com.zk.common.interceptor.AuthInterceptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private BeanFactory beanFactory;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(beanFactory.getBean(AuthInterceptor.class))
                .addPathPatterns("/api/**");
        super.addInterceptors(registry);
    }

    // ================== 过滤器配置 - 开始 ==================
    @Bean
    public FilterRegistrationBean requestLogFilterRegistration(RequestLogFilter requestLogFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(requestLogFilter);  //RequestLogFilter类上使用了@Component注解，将自己交给Spring容器管理，可注入其他Spring bean
        registration.addUrlPatterns("/*");
        registration.setOrder(1); //Filter的执行顺序，值越小越先执行
        return registration;
    }
    @Bean
    public FilterRegistrationBean repeatableReadHttpServletRequestFilterRegistration(
            RepeatableReadHttpServletRequestFilter repeatableReadHttpServletRequestFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(repeatableReadHttpServletRequestFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(2); //Filter的执行顺序，值越小越先执行
        return registration;
    }
    // ================== 过滤器配置 - 结束 ==================

    // swagger2
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
