package com.zk.inter.impl;

import com.zk.annotation.AddLog;
import com.zk.annotation.IpWhiter;
import com.zk.annotation.ParamName;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2017/12/3.
 */
@Component
public class Tiger {

    public String say(String name) {
        System.out.println("i'm a Tiger , name = " + name);
        return name;
    }

    public void day(Date date) {
        System.out.println("Tiger说今天是：" + date.toLocaleString());
    }

    public void any(Object object) {
        System.out.println("Tiger接收到的参数类型为：" + object.getClass().getName());
    }

    @AddLog
    public void addLogAnnotationMethod(String a) {
        System.out.println("Tiger说：我这个方法上有个注解@com.zk.annotation.AddLog");
    }


    public void addLogAnnotationMethod(Date a) {
        System.out.println("Tiger说：我这个方法上有个注解@com.zk.annotation.AddLog");
    }

    @AddLog
    @IpWhiter
    public void twoAnnotation() {
        System.out.println("Tiger说：我这个方法上有两个注解" +
                "@com.zk.annotation.AddLog和com.zk.annotation.IpWhiter");
    }

    @IpWhiter
    public void ipWhiterAnnotationMethod() {
        System.out.println("Tiger说：我这个方法上有个注解com.zk.annotation.IpWhiter");
    }

    public ResponseVo returnValue() {
        System.out.println("Tiger说：我返回类型为：com.zk.inter.impl.ResponseVo，返回类型的类上有个注解com.zk.annotation.Encrypt");
        return new ResponseVo();
    }

    public ResponseVo twoParamHasAnnotation(@ParamName String name1, @ParamName String name2) {
        System.out.println("Tiger说：我的两个参数都被@com.zk.annotation.ParamName修饰");
        return new ResponseVo();
    }

    public ResponseVo oneParamHasAnnotation(RequestVo requestVo) {
        System.out.println("Tiger说：我只有一个参数，类型为com.zk.inter.impl.RequestVo，RequestVo类上有个注解@Encrypt");
        return new ResponseVo();
    }

    public void generic(List<RequestVo> list) {
        System.out.println("Tiger说：参数为：List<RequestVo> list， RequestVo类上有个注解@Encrypt");
    }
}
