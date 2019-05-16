package com.zk.common.interceptor;

import com.zk.utils.JsonUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String auth = request.getParameter("auth");
        if (Boolean.TRUE.toString().equals(auth)) {
            return true;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("msg", "你无权限访问");
        map.put("dateTime", new Date().toLocaleString());
        response.getWriter().write(JsonUtils.toJsonHasNullKey(map));
        response.flushBuffer();
        return false;
    }

    @Override
    public void postHandle (HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion (HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
