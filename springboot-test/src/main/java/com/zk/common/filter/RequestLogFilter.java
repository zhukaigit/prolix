package com.zk.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.UUID;

/**
 * 日志请求过滤器
 * 执行顺序：始终放在第一位执行
 */
@Slf4j
@Component
public class RequestLogFilter extends AbstractRequestLoggingFilter {

    @Override
    protected boolean isIncludeQueryString() {
        return true;
    }

    @Override
    protected boolean isIncludeClientInfo() {
        return false;
    }

    @Override
    protected boolean isIncludeHeaders() {
        return true;
    }

    @Override
    protected boolean isIncludePayload() {
        return true;
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        // 给每个请求新增唯一的追踪标识
        // 同时logback.xml文件中用"%X{traceId}"来匹配输出
        MDC.put("traceId", UUID.randomUUID().toString());
        log.info("\r\n============== beforeRequest start ==================");
        String uri = request.getRequestURI().replaceAll("/+", "/");
        log.info("requestURI = {}, beforeRequest msg:{}", uri, message);
        // 注意：查询requestBody的信息，使用关键词payload=
        log.info("【【【beforeRequest msg】】】:{}", message);
        log.info("\r\n============== beforeRequest end ==================");
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        // 记录request params
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuilder paramsStr = new StringBuilder("\r\n==================request params start==================\r\n");
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            paramsStr.append(paramName).append("=").append(paramValue).append("\r\n");
        }
        paramsStr.append("==================request params end==================");
        log.info(paramsStr.toString());
        log.info("【【【afterRequest msg】】】: {}", message);

    }
}
