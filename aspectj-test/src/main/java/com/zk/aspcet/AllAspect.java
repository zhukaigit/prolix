package com.zk.aspcet;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2017/12/3.
 */
@Component
@Aspect
@Order(10)
public class AllAspect {

    @Before("execution(* com..*(..))")
    public void before(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        System.out.println(String.format("\n================== 方法【%s】 start, order = 10 ====================", methodName));
    }

    @After("execution(* com..*(..))")
    public void after(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        System.out.println(String.format("================== 方法【%s】 end, order = 10  ====================\n", methodName));
    }
}
