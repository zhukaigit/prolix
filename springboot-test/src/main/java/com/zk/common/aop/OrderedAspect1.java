package com.zk.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 1、测试@Order(n)的执行顺序
 * 2、测试before、after、around、afterRetur
 *
 * 注意：不同的注解里有不同的属性，before等可以指定参数类型，AfterReturning可以指定返回类型，afterThrowing指定异常类型等等。
 */
@Order(1)
@Component
@Aspect
@Slf4j
@Profile("aspectjOrder")
public class OrderedAspect1 {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    private void point () {
    }

    @Before(value = "point()")
    public void before (JoinPoint jp) {
        log.info("========== before ==========");
    }

    @After(value = "point()")
    public void after (JoinPoint jp) {
        log.info("========== after ==========");
    }

    @AfterReturning(value = "point()")
    public void afterReturning (JoinPoint jp) {
        log.info("========== afterReturning ==========");
    }

    @AfterThrowing(value = "point()",throwing = "ex")
    public void afterThrowing (JoinPoint jp, Exception ex) throws Exception {
        log.info("========== afterThrowing ==========");
        log.error("错误信息：" + ex.getMessage());
        throw ex;
    }

    // 注意：around可改变返回值
    @Around(value = "point()")
    public Object around (ProceedingJoinPoint jp) {
        log.info("========== around before ==========");
        Object proceed = null;
        try {
            proceed = jp.proceed();
        } catch (Throwable throwable) {
            log.info("========== around target method exception ==========");
        }
        log.info("========== around after ==========");
        return proceed;
    }
}
