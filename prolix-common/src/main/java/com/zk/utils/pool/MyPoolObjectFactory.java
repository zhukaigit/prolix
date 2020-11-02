package com.zk.utils.pool;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyPoolObjectFactory extends BasePooledObjectFactory<MyPoolObject> {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    @Override
    public MyPoolObject create () throws Exception {
        System.out.println();
        System.out.println(StringUtils.center("执行MyPoolObjectFactory.create - 开始", 50, "=="));
        MyPoolObject object = MyPoolObject.builder().createdTimeString(LocalDateTime.now().format(DateTimeFormatter.ofPattern
                (YYYY_MM_DD_HH_MM_SS))).remainBattery(100).build();
        System.out.println("创建的object=" + object);
        System.out.println(StringUtils.center("执行MyPoolObjectFactory.create - 结束", 50, "=="));
        System.out.println();
        return object;
    }

    @Override
    public PooledObject<MyPoolObject> wrap (MyPoolObject obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public boolean validateObject (PooledObject<MyPoolObject> p) {
        boolean b = super.validateObject(p) && p.getObject().getRemainBattery() > 0;
        if (!b) {
            System.out.println("对象不可用了，object={}" + p.getObject());
        }
        return b;
    }

    @Override
    public void activateObject (PooledObject<MyPoolObject> p) throws Exception {
        super.activateObject(p);
    }

    @Override
    public void destroyObject (PooledObject<MyPoolObject> p) throws Exception {
        System.out.println();
        System.out.println(StringUtils.center("执行MyPoolObjectFactory.destroyObject - 开始", 50, "=="));
        MyPoolObject object = p.getObject();
        System.out.println("销毁的object = " + object);
        System.out.println(StringUtils.center("执行MyPoolObjectFactory.destroyObject - 结束", 50, "=="));
        System.out.println();
    }
}
