package com.zk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger-ui.html访问URL为：http://{host}:{port}/{contextPath}/{dispatch-servlet-url}/swagger-ui.html
 * 注意：需要对Swagger2的静态资源访问路径添加对应的映射，
 * 见{@link WebMvcConfig#addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry)}
 *
 *
 * 若访问时，弹出以下窗口
 * ======== 以下为弹出窗口信息 - 开始========
 * Unable to infer base url. This is common when using dynamic
 * servlet registration or when the API is behind an API Gateway.
 * The base url is the root of where all the swagger resources
 * are served. For e.g. if the api is available at http://
 * example.org/api/v2/api-docs then the base url is http://
 * example.org/api/. Please enter the location manually:
 *
 * [ ...... 输入框 ......]
 * ======== 以上为弹出窗口信息 - 开始========
 *
 * 出现的原因很可能是没有dispatch-servlet配置了前缀拦截路径，而访问时的路径为http://{host}:{port}/{contextPath}/swagger-ui.html
 * 解决方法1、该用访问路径http://{host}:{port}/{contextPath}/{dispatch-servlet-url}/swagger-ui.html
 * 解决方法2、在输入框中输入baseUrl即可；baseUrl为：http://{host}:{port}/{contextPath}/api
 * 其中"/api"为DispatchServlet的拦截前缀
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.zk.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot 测试使用 Swagger2 构建RESTful API")
                //创建人
                .contact(new Contact("ZhuKai", "http://www.baidu.com", ""))
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .termsOfServiceUrl("http://localhost:8081")
                .licenseUrl("http://localhost:8081")
                .build();
    }

}
