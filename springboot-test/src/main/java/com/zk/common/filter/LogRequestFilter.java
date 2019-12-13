package com.zk.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;

/**
 * 将HttpServletRequest转换成RepeatableHttpServletRequest对象，保证可重复读取httpRequestBody
 */
@Component
@Slf4j
public class LogRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                     FilterChain filterChain) throws ServletException, IOException {
        // 将其设置为可重复读
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        if (!(httpServletRequest instanceof RepeatableHttpServletRequest)) {
            httpServletRequest = new RepeatableHttpServletRequest(httpServletRequest);
        }

        // 打印请求日志
        String ipAddr = IpUtil.getIpAddr(httpServletRequest);
        String requestBody = StreamUtils.copyToString(httpServletRequest.getInputStream(), Charset.forName("utf-8"));
        StringBuilder paramStrBuild = new StringBuilder();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            paramStrBuild.append(name).append("=").append(httpServletRequest.getParameter(name)).append("&");
        }
        String paramStr = "";
        if (paramStrBuild.toString().endsWith("&")) {
            paramStr = paramStrBuild.substring(0, paramStrBuild.lastIndexOf("&"));
        }
        String uri = httpServletRequest.getRequestURI().replaceAll("/+", "/");

        log.info("【remoteIp】={}, 【requestURI】={}, 【requestParam】={}, 【requestBody】={}",
                ipAddr, uri, String.format("[%s]", paramStr), requestBody);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public static class RepeatableHttpServletRequest extends HttpServletRequestWrapper {
        private final byte[] bytes;

        public RepeatableHttpServletRequest (HttpServletRequest request) throws IOException {
            super(request);
            bytes = StreamUtils.copyToByteArray(request.getInputStream());
        }

        @Override
        public ServletInputStream getInputStream () throws IOException {
            return new ServletInputStream() {
                private int lastIndexRetrieved = -1;
                private ReadListener readListener = null;

                @Override
                public boolean isFinished () {
                    return (lastIndexRetrieved == bytes.length - 1);
                }

                @Override
                public boolean isReady () {
                    // This implementation will never block
                    // We also never need to call the readListener from this method, as this method will never return false
                    return isFinished();
                }

                @Override
                public void setReadListener (ReadListener readListener) {
                    this.readListener = readListener;
                    if (!isFinished()) {
                        try {
                            readListener.onDataAvailable();
                        } catch (IOException e) {
                            readListener.onError(e);
                        }
                    } else {
                        try {
                            readListener.onAllDataRead();
                        } catch (IOException e) {
                            readListener.onError(e);
                        }
                    }
                }

                @Override
                public int read () throws IOException {
                    int i;
                    if (!isFinished()) {
                        i = bytes[lastIndexRetrieved + 1];
                        lastIndexRetrieved++;
                        if (isFinished() && (readListener != null)) {
                            try {
                                readListener.onAllDataRead();
                            } catch (IOException ex) {
                                readListener.onError(ex);
                                throw ex;
                            }
                        }
                        return i;
                    } else {
                        return -1;
                    }
                }
            };
        }

        @Override
        public BufferedReader getReader () throws IOException {
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedReader temp = new BufferedReader(new InputStreamReader(is));
            return temp;
        }
    }

    public static class IpUtil {

        /**
         * 获取IP地址
         * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
         * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
         */
        public static String getIpAddr (HttpServletRequest request) {
            String ip = null;
            try {
                ip = request.getHeader("x-forwarded-for");
                if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                }
                if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                }
                if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
            } catch (Exception e) {
                log.warn("IPUtils ERROR ", e);
            }
            return ip;
        }

    }
}
