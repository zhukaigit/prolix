package com.zk.aspcet;

import com.zk.annotation.AddLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2017/12/3.
 */
@Component
@Aspect
@Order(2)
public class MyAspect {

    @Pointcut("@within(com.zk.annotation.IpWhiter))")
    public void pointcut(){}
    @Pointcut("@within(com.zk.annotation.AddLog))")
    public void pointcut2(){}

    //测试：com.zk.AppTest.test_within_1()
    //描述：目标类持有注解情况（@IpWhiter || @AddLog）&& @Api
    @Before("(@within(com.zk.annotation.IpWhiter) || @within(com.zk.annotation.AddLog)) && @within(com.zk.annotation.Api)")
//    @Before("(*) || *))")
    public void before_2(JoinPoint joinPoint) {
        System.out.println("joinPoint = "+joinPoint.getThis().getClass());
        System.out.println(String.format("\n【before】 : 【 @within(com.zk.annotation.IpWhiter)) && @within(com.zk.annotation.Encrypt) 】"));
    }

    //测试：com.zk.AppTest.test_within_1()
    //描述：持有@IpWhiter注解的任何类型的任何方法，必须是在目标对象上声明这个注解，在接口上声明的对它不起作用
//    @Before(value = "within(@com.zk.annotation.IpWhiter *)")
    //描述：持有【@IpWhiter和@Encrypt】注解的任何类型的任何方法，必须是在目标对象上声明这个注解，在接口上声明的对它不起作用
//    @Before(value = "within(@com.zk.annotation.IpWhiter @com.zk.annotation.Encrypt *)")
    //描述：持有【@IpWhiter或者@Encrypt】任意一个注解的任何类型的任何方法，必须是在目标对象上声明这个注解，在接口上声明的对它不起作用
//    @Before(value = "within(@(com.zk.annotation.IpWhiter || com.zk.annotation.Encrypt) *)")
    public void before_1() {
        System.out.println("\n【before】 : 【 within(@com.zk.annotation.IpWhiter *) 】");
    }

    /** ==================== execution表达式测试 开始 =====================**/

    //测试：com.zk.AppTest.test_before11()
    //描述：任何带有一个参数（类型为java.util.List）的方法，且该参数类型是有一个泛型参数，该泛型参数类型上持有@com.zk.annotation.Encrypt注解；
    @Before(value = "execution(* *(java.util.List<@com.zk.annotation.Encrypt *>))")
    public void before11() {
        System.out.println("\n【before】 : 【 execution(* *(java.util.List<@com.zk.annotation.Encrypt *>)) 】");
    }

    //测试：com.zk.AppTest.test_before10()
    //描述：任何带有一个参数的方法，且该参数类型持有@com.zk.annotation.Encrypt
    //如：public void test(Model model);且Model类上持有@Encrypt注解
    @Before(value = "execution(* *(@com.zk.annotation.Encrypt *))")
    public void before10() {
        System.out.println("\n【before】 : 【 execution(* *(@com.zk.annotation.Encrypt *)) 】");
    }

    //测试：com.zk.AppTest.test_before9()
    //描述：任何签名带有两个参数的方法，且这个两个参数都被@com.zk.annotation.ParamName标记了，
    @Before(value = "execution(* *(@com.zk.annotation.ParamName (*), @com.zk.annotation.ParamName (*)))")
    public void before9() {
        System.out.println("\n【before】 : 【 execution(* *(@com.zk.annotation.ParamName (*), @com.zk.annotation.ParamName (*))) 】");
    }

    //测试：com.zk.AppTest.test_before8()
    //描述：任何返回值类型持有@com.zk.annotation.Encrypt的方法
//    @Before("execution((@com.zk.annotation.Encrypt *) *(..))")
    public void before8() {
        System.out.println("\n【before】 : 【execution((@com.zk.annotation.Encrypt *) *(..))】");
    }

    //测试：com.zk.AppTest.test_before7()
    //描述：带有这两个注解【@com.zk.annotation.AddLog || @com.zk.annotation.IpWhiter】中的任意一个注解的方法
    @Before("execution(@(com.zk.annotation.AddLog || com.zk.annotation.IpWhiter) * *(..))")
    public void before7() {
        System.out.println("\n【before】 : 【execution(@(com.zk.annotation.AddLog || com.zk.annotation.IpWhiter) * *(..))】");
    }

    //测试：com.zk.AppTest.test_before6()
    //描述：同时带有这两个注解【@com.zk.annotation.AddLog && @com.zk.annotation.IpWhiter】的方法
    @Before("execution(@com.zk.annotation.AddLog @com.zk.annotation.IpWhiter * *(..))")
    public void before6() {
        System.out.println("\n【before】 : 【execution(@com.zk.annotation.AddLog @com.zk.annotation.IpWhiter * *(..))】");
    }

    //测试：com.zk.AppTest.test_before5()
    //描述：带有注解@com.zk.annotation.AddLog的方法
    @Before("execution(@com.zk.annotation.AddLog * *(..))")
    public void before5(JoinPoint jp) {
//        MethodInvocationProceedingJoinPoint joinPoint = (MethodInvocationProceedingJoinPoint) jp;
        //小知识，获取目标方法
        MethodSignature signature = (MethodSignature)jp.getSignature();
        Method method = signature.getMethod();
        System.out.println("\n【before】 : 【execution(@com.zk.annotation.AddLog * *(..))】");
    }

    //测试：com.zk.AppTest.test_before4()
    //描述：运行时接收的参数类型为java.util.Date类型的方法
    //注意与before3()的区别
    @Before(value = "args(param)", argNames = "param")
    public void before4(Date param) {
        System.out.println("\n【before】 : 【@Before(value = \"args(param)\", argNames = \"param\")】");
    }

    //测试：com.zk.AppTest.test_beforePointcut3()
    //描述：只有com包以及子包下的Tiger类的入参为Date类型的方法才会被拦截，注意：Date是方法中声明的类型，而不是传入的类型
    @Before(value = "execution(* com..Tiger.*(java.util.Date)) && args(param)")
    public void before3_1(Date param) {
        System.out.println("\n在before3()基础上，绑定了入参：" + param.toLocaleString());
    }

    //测试：com.zk.AppTest.test_beforePointcut3()
    //描述：只有com包以及子包下的Tiger类的入参为Date类型的方法才会被拦截，注意：Date是方法中声明的类型，而不是传入的类型
    @Before(value = "execution(* com..Tiger.*(java.util.Date))")
    public void before3() {
        System.out.println("\n【before】【execution(* com..Tiger.*(java.util.Date))】");
    }

    //测试：com.zk.AppTest.test_beforePointcut2()
    //描述：只要不是实现People接口的类的所有方法都会拦截
//    @Before("execution(* (!com.zk.inter.People).*(..))")
    public void before2() {
        System.out.println("this is before 【非】 !!!");
    }


    //测试：com.zk.AppTest.test_beforePointcut1()
    //描述：实现了People接口的类的所有方法都会执行拦截
    @Before("execution(* com.zk.inter.People.*(..))")
    public void before1() {
        System.out.println("this is before 【实现接口】 !!!");
    }

    /** ==================== execution表达式测试 结束 =====================**/
}
