package com.zk.common.aop;

import com.zk.common.BaseResponse;
import com.zk.common.annotation.ResponseLog;
import com.zk.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 请求及返回日志记录
 */
@Slf4j
@Component
@Aspect
@Order (1)
public class ResponseLogAspect {

    /**
     * 指定日志切面
     * 只对添加了指定组合注解的类执行切面逻辑
     */
    // 简写 (@Controller || @RestController) && @ResponseLog
    @Pointcut (value = "(@within(org.springframework.stereotype.Controller) " +
            "   || @within(org.springframework.web.bind.annotation.RestController))" +
            "        && @within(com.zk.common.annotation.ResponseLog)")
    public void logPointcut () {
    }

    /**
     * 正常返回时走的逻辑
     *
     * @param baseResponse
     */
    @AfterReturning (value = "logPointcut()", returning = "baseResponse")
    public void afterReturning (JoinPoint joinPoint, BaseResponse baseResponse) {
        log.info("======== 返回报文如下 ==========");
        if (isExcluded(joinPoint)) {
            return;
        }
        log.info(JsonUtils.toJsonHasNullKey(baseResponse));
    }

    /**
     * 异常时走的逻辑
     *
     * @param ex
     * @throws Exception
     */
    @AfterThrowing (value = "logPointcut()", throwing = "ex")
    public void afterReturning (JoinPoint joinPoint, Exception ex) throws Exception {
        log.error("出错了, ex = " + ex.getClass());
        if (isExcluded(joinPoint)) {
            return;
        }
        throw ex;
    }

    private boolean isExcluded (JoinPoint joinPoint) {
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();
        ResponseLog responseLog = method.getAnnotation(ResponseLog.class);
        if (responseLog != null && responseLog.isExcluded()) {
            return true;
        }
        return false;
    }


}
