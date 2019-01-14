package com.zk.springbootswagger2.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

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
        return false;
    }

    @Override
    protected boolean isIncludePayload() {
        return true;
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        // 给每个请求新增唯一的追踪标识
        MDC.put("traceId", UUID.randomUUID().toString());
        log.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        log.info(message);
    }
}
