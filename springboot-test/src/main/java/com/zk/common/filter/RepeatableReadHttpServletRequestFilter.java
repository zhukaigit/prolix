package com.zk.common.filter;

import com.zk.common.wrapper.RepeatableHttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 将HttpServletRequest转换成RepeatableHttpServletRequest对象，保证可重复读取httpRequestBody
 * 执行顺序：放在{@link com.zk.common.filter.RequestLogFilter}之后执行
 */
@Component
public class RepeatableReadHttpServletRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                     FilterChain filterChain) throws ServletException, IOException {
        RepeatableHttpServletRequest repeatableHttpServletRequest = new RepeatableHttpServletRequest(httpServletRequest);
        filterChain.doFilter(repeatableHttpServletRequest, httpServletResponse);
    }
}
