package com.zk.common.filter;

import com.zk.common.wrapper.RepeatableHttpServletRequest;
import com.zk.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 日志请求过滤器
 * 执行顺序：始终放在第一位执行
 */
@Slf4j
@Component
public class RequestLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("request parameterMap = {}", JsonUtils.toJsonHasNullKey(parameterMap));

        if (!(request instanceof RepeatableHttpServletRequest)) {
            request = new RepeatableHttpServletRequest(request);
        }

        String requestBody = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
        log.info("\r\n========= requestBody start =========\r\n{}\r\n========= requestBody end =========", requestBody);

        filterChain.doFilter(request, response);
    }
}
