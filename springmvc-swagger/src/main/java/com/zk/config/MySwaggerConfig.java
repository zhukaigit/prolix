package com.zk.config;


import com.zk.constant.annotation.ApiDocOne;
import com.zk.constant.annotation.ApiDocTwo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.zk.custom.RequestHandlerSelectorsExt.withMethodAnnotations;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class MySwaggerConfig {
    @Bean
    public Docket all() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(basePackage("com.zk"))
                .build();
    }

    /**
     * 获取指定注解的接口
     */
    @Bean
    public Docket JSY() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("JSY")//分组
                .apiInfo(apiInfo())
                .select()
                .apis(withMethodAnnotations(ApiDocOne.class, ApiDocTwo.class))//只加载指定注解的API
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("服务平台 API")
                .description("供开发及测试人员调试")
                .termsOfServiceUrl("http://localhost:8080")
                .version("1.0")
                .build();
    }
}
